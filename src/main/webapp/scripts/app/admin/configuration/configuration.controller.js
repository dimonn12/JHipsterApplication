'use strict';

angular.module('jHipsterApplicationApp')
    .controller('ConfigurationController', function ($scope, ConfigurationService) {
        ConfigurationService.get().then(function (configuration) {
            $scope.configuration = configuration;
        });
    });
