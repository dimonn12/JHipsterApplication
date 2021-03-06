'use strict';

angular.module('jHipsterApplicationApp')
    .factory('MonitoringService', function ($rootScope, $http) {
        return {
            getMetrics: function () {
                return $http.get('metrics/metrics').then(function (response) {
                    return response.data;
                });
            },

            checkHealth: function () {
                return $http.get('health').then(function (response) {
                    return response.data;
                });
            },

            threadDump: function () {
                return $http.get('dump').then(function (response) {
                    return response.data;
                });
            },

            clearCache: function (cacheName, callThen) {
                return $http.post('api/cache', {cacheName: cacheName}).then(callThen);
            }
        };
    });
