'use strict';

angular.module('jHipsterApplicationApp')
    .controller('PasswordController', function ($scope, Auth, Principal) {

        $scope.responseResults = {
            serverError: false,

            success: false,

            error: false,
            doNotMatch: false,
            tooWeak: false,

            clearAll: function () {
                $scope.responseResults.serverError = false;

                $scope.responseResults.success = false;

                $scope.responseResults.error = false;
                $scope.responseResults.doNotMatch = false;
                $scope.responseResults.tooWeak = false;
            }
        };

        Principal.identity().then(function (account) {
            $scope.responseResults.clearAll();
            $scope.account = account;
        }).catch(function (errorObject) {
            $scope.responseResults.serverError = true;
        });

        $scope.changePassword = function () {
            $scope.responseResults.clearAll();
            if ($scope.password !== $scope.confirmPassword) {
                $scope.responseResults.doNotMatch = true;
            } else {
                Auth.changePassword($scope.password).then(function () {
                    $scope.responseResults.success = true;
                }, function (errorObject) {
                    for (var index = 0; index < errorObject.data.errorStatuses.length; index++) {
                        var errorStatus = errorObject.data.errorStatuses[index];
                        if (errorStatus.code === 4000003) {
                            $scope.responseResults.tooWeak = true;
                        }
                        if (errorStatus.code === 5000000) {
                            $scope.responseResults.serverError = true;
                        }
                    }

                    if (!$scope.responseResults.tooWeak && !$scope.responseResults.serverError) {
                        $scope.responseResults.error = true;
                    }
                });
            }
        };
    });
