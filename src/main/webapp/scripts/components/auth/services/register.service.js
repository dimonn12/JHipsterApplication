'use strict';

angular.module('jHipsterApplicationApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {});
    });


