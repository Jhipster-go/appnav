(function() {
    'use strict';

    angular
        .module('appnavApp')
        .controller('WxAppDetailController', WxAppDetailController);

    WxAppDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WxApp', 'Comment', 'Category', 'Tag', 'User'];

    function WxAppDetailController($scope, $rootScope, $stateParams, previousState, entity, WxApp, Comment, Category, Tag, User) {
        var vm = this;

        vm.wxApp = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('appnavApp:wxAppUpdate', function(event, result) {
            vm.wxApp = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
