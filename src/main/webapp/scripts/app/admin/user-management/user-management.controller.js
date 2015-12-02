'use strict';

angular.module('jHipsterApplicationApp')
    .controller('UserManagementController', function ($scope, $translate, User, UserLock, UserUnlock, ParseLinks, Language) {
        $scope.users = [];
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN"];

        $scope.responseResults = {
            serverError: false,

            success: false,
            error: false,
            validationError: false,

            activationSuccess: false,
            deactivationSuccess: false,
            activationError: false,
            deactivationError: false,

            userLockSuccess: false,
            userUnlockSuccess: false,
            userLockError: false,
            userUnlockError: false,

            clearAll: function () {
                $scope.responseResults.serverError = false;

                $scope.responseResults.success = false;
                $scope.responseResults.error = false;
                $scope.responseResults.validationError = false;

                $scope.responseResults.activationSuccess = false;
                $scope.responseResults.deactivationSuccess = false;
                $scope.responseResults.activationError = false;
                $scope.responseResults.deactivationError = false;

                $scope.responseResults.userLockSuccess = false;
                $scope.responseResults.userUnlockSuccess = false;
                $scope.responseResults.userLockError = false;
                $scope.responseResults.userUnlockError = false;
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
            }, function () {
                $scope.responseResults.serverError = true;
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
            $scope.user = user;
            User.update(user, function () {
                if ($scope.user.activated) {
                    $scope.responseResults.activationSuccess = true;
                } else {
                    $scope.responseResults.deactivationSuccess = true;
                }
                $scope.loadAll();
            }, function (errorObject) {
                $scope.user.activated = !isActivated;
                if ($scope.user.activated) {
                    $scope.responseResults.activationError = true;
                } else {
                    $scope.responseResults.deactivationError = true;
                }
            });
        };

        $scope.showUpdate = function (login) {
            $scope.responseResults.clearAll();
            $scope.clear();
            User.get({login: login}, function (result) {
                $scope.user = result;
                $('#saveUserModal').modal('show');
            }, function () {
                $scope.responseResults.serverError = true;
            });
        };

        $scope.save = function () {
            $scope.responseResults.clearAll();
            User.update($scope.user,
                function () {
                    $scope.responseResults.success = true;
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
            $scope.user = user;
            UserLock.get({login: user.login}, function (result) {
                $scope.responseResults.userLockSuccess = true;
                $scope.loadAll();
            }, function (errorObject) {
                $scope.responseResults.userLockError = true;
            });
        };

        $scope.unlockUser = function (user) {
            $scope.responseResults.clearAll();
            $scope.user = user;
            UserUnlock.get({login: user.login}, function (result) {
                $scope.responseResults.userUnlockSuccess = true;
                $scope.loadAll();
            }, function (errorObject) {
                $scope.responseResults.userUnlockError = true;
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveUserModal').modal('hide');
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
