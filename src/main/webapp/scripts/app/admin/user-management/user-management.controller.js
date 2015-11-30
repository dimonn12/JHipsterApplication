'use strict';

angular.module('jHipsterApplicationApp')
    .controller('UserManagementController', function ($scope, User, ParseLinks, Language) {
        $scope.users = [];
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN"];

        $scope.success = null;
        $scope.error = null;
        $scope.validationError = null;

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
            user.activated = isActivated;
            User.update(user, function () {
                $scope.loadAll();
                $scope.clear();
            });
        };

        $scope.showUpdate = function (login) {
            User.get({login: login}, function (result) {
                $scope.user = result;
                $scope.success = null;
                $('#saveUserModal').modal('show');
            });
        };

        $scope.save = function () {
            User.update($scope.user,
                function () {
                    $scope.success = true;
                    $scope.error = null;
                    $scope.validationError = null;
                    $scope.refresh();
                }, function (errorObject) {
                    $scope.success = null;
                    $scope.error = true;

                    for (var indx = 0; indx < errorObject.data.errorStatuses.length; indx++) {
                        var errorStatus = errorObject.data.errorStatuses[indx];
                        if (errorStatus.code === 4000100) {
                            $scope.error = null;
                            $scope.validationError = true;
                        }
                    }
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
