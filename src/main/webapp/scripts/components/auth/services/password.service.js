'use strict';

angular.module('jHipsterApplicationApp')
    .factory('Password', function ($resource) {
        return $resource('api/account/change_password', {}, {});
    });

angular.module('jHipsterApplicationApp')
    .factory('PasswordResetInit', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {})
    });

angular.module('jHipsterApplicationApp')
    .factory('PasswordResetFinish', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {})
    });
