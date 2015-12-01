'use strict';

angular.module('jHipsterApplicationApp')
    .controller('UserManagementController', function ($scope, $translate, User, UserLock, UserUnlock, ParseLinks, Language) {
        $scope.users = [];
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN"];

        $scope.responseResults = {
            success: false,
            error: false,
            validationError: false,

            activationSuccess: false,
            activationError: false,

            userLockSuccess: false,
            userLockError: false,

            clearAll: function () {
                $scope.responseResults.success = false;
                $scope.responseResults.error = false;
                $scope.responseResults.validationError = false;

                $scope.responseResults.activationSuccess = false;
                $scope.responseResults.activationError = false;

                $scope.responseResults.userLockSuccess = false;
                $scope.responseResults.userLockError = false;
            }
        };

        $scope.responseResults.clearAll();

        Language.getAll().then(function (languages) {
            $scope.languages = languages;
        });

        $scope.page = 0;
        $scope.loadAll = function () {
            User.query({page: $scope.page, per_page: 20}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.users = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.setActive = function (user, isActivated) {
            $scope.responseResults.clearAll();
            user.activated = isActivated;
            User.update(user, function () {
                $scope.responseResults.activationSuccess = true;


                $scope.loadAll();
                $scope.clear();
            }, function (errorObject) {
                $scope.responseResults.activationError = true;

                for (var indx = 0; indx < errorObject.data.errorStatuses.length; indx++) {
                    var errorStatus = errorObject.data.errorStatuses[indx];
                    /*if (errorStatus.code === 4000100) {
                     $scope.responseResults.clearAll();
                     $scope.responseResults.validationError = true;
                     }*/
                }
            });
        };

        $scope.showUpdate = function (login) {
            $scope.responseResults.clearAll();
            User.get({login: login}, function (result) {
                $scope.user = result;
                $('#saveUserModal').modal('show');
            });
        };

        $scope.save = function () {
            $scope.responseResults.clearAll();
            User.update($scope.user,
                function () {
                    $scope.responseResults.success = true;

                    $translate('user-management.messages.success', {login: $scope.user.login})
                        .
                        then(function (title) {
                            $translate.refresh($scope.user.langKey);
                        })
                    ;

                    $scope.refresh();
                }, function (errorObject) {
                    $scope.responseResults.error = true;

                    for (var indx = 0; indx < errorObject.data.errorStatuses.length; indx++) {
                        var errorStatus = errorObject.data.errorStatuses[indx];
                        if (errorStatus.code === 4000100) {
                            $scope.responseResults.clearAll();
                            $scope.responseResults.validationError = true;
                        }
                    }
                });
        };

        $scope.lockUser = function (user) {
            $scope.responseResults.clearAll();
            UserLock.get({login: user.login}, function (result) {
                $scope.loadAll();
                $scope.clear();
            });
        };

        $scope.unlockUser = function (user) {
            $scope.responseResults.clearAll();
            UserUnlock.get({login: user.login}, function (result) {
                $scope.loadAll();
                $scope.clear();
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.user = {
                id: null, login: null, firstName: null, lastName: null, email: null,
                activated: null, langKey: null, createdBy: null, createdDate: null,
                lastModifiedBy: null, lastModifiedDate: null, resetDate: null,
                resetKey: null, authorities: null
            };
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
