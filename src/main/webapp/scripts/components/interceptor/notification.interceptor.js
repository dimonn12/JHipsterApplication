'use strict';

angular.module('jHipsterApplicationApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function (response) {
                var alertKey = response.headers('X-jHipsterApplicationApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, {param: response.headers('X-jHipsterApplicationApp-params')});
                }
                return response;
            }
        };
    });
