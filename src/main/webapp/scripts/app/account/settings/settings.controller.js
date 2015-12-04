'use strict';

angular.module('jHipsterApplicationApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate) {
        $scope.success = null;
        $scope.error = null;

        $scope.responseResults = {
            serverError: false,

            success: false,

            validationError: false,
            errorEmailExists: false,

            clearAll: function () {
                $scope.responseResults.serverError = false;

                $scope.responseResults.success = false;

                $scope.responseResults.validationError = false;
                $scope.responseResults.errorEmailExists = false;
            }
        };

        Principal.identity(true).then(function (account) {
            $scope.responseResults.clearAll();
            $scope.settingsAccount = account;
            if (null === account) {
                $scope.responseResults.serverError = true;
            }
        }).catch(function (errorObject) {
            $scope.responseResults.serverError = true;
        });

        $scope.save = function () {
            $scope.responseResults.clearAll();
            Auth.updateAccount($scope.settingsAccount).then(function () {
                $scope.responseResults.success = true;
                Principal.identity().then(function (account) {
                    $scope.settingsAccount = account;
                    if (null === account) {
                        $scope.responseResults.serverError = true;
                    }
                }).catch(function (errorObject) {
                    $scope.responseResults.serverError = true;
                });
                Language.getCurrent().then(function (current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                    if (null === current) {
                        $scope.responseResults.serverError = true;
                    }
                }).catch(function (errorObject) {
                    $scope.responseResults.serverError = true;
                });
            }).catch(function (errorObject) {
                for (var index = 0; index < errorObject.data.errorStatuses.length; index++) {
                    var errorStatus = errorObject.data.errorStatuses[index];
                    if (errorStatus.code === 4000001) {
                        $scope.responseResults.errorEmailExists = true;
                    }
                    if (errorStatus.code === 4000100) {
                        $scope.responseResults.validationError = true;
                    }
                    if (errorStatus.code === 5000000) {
                        $scope.responseResults.serverError = true;
                    }
                }

                if (!$scope.responseResults.errorEmailExists && !$scope.responseResults.validationError) {
                    $scope.responseResults.serverError = true;
                }
            });
        };
    });
