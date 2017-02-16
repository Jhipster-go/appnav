(function() {
    'use strict';
    angular
        .module('appnavApp')
        .factory('WxApp', WxApp);

    WxApp.$inject = ['$resource', 'DateUtils'];

    function WxApp ($resource, DateUtils) {
        var resourceUrl =  'api/wx-apps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.publicationDate = DateUtils.convertLocalDateFromServer(data.publicationDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.publicationDate = DateUtils.convertLocalDateToServer(copy.publicationDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.publicationDate = DateUtils.convertLocalDateToServer(copy.publicationDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
