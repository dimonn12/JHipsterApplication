'use strict';

angular.module('jHipsterApplicationApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate) {
        $scope.success = null;
        $scope.error = null;
        Principal.identity(true).then(function (account) {
            $scope.settingsAccount = account;
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function () {
                $scope.error = null;
                $scope.success = 'OK';
                Principal.identity().then(function (account) {
                    $scope.settingsAccount = account;
                });
                Language.getCurrent().then(function (current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function (errorObject) {
                $scope.success = null;
                $scope.error = 'ERROR';

                for (var indx = 0; indx < errorObject.data.errorStatuses.length; indx++) {
                    var errorStatus = errorObject.data.errorStatuses[indx];
                    if (errorStatus.code === 4000001) {
                        $scope.error = null;
                        $scope.errorEmailExists = true;
                    }
                }
            });
        };
    });
