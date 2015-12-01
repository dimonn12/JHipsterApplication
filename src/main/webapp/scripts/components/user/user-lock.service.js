'use strict';

angular.module('jHipsterApplicationApp')
    .factory('UserLock', function ($resource) {
        return $resource('api/users/:login/lock', {}, {
            'get': {
                method: 'GET', params: {}, isArray: false, transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    }).factory('UserUnlock', function ($resource) {
        return $resource('api/users/:login/unlock', {}, {
            'get': {
                method: 'GET', params: {}, isArray: false, transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });


