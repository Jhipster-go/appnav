(function() {
    'use strict';

    angular
        .module('appnavApp')
        .factory('WxAppSearch', WxAppSearch);

    WxAppSearch.$inject = ['$resource'];

    function WxAppSearch($resource) {
        var resourceUrl =  'api/_search/wx-apps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
