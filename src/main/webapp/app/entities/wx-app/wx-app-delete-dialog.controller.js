(function() {
    'use strict';

    angular
        .module('appnavApp')
        .controller('WxAppDeleteController',WxAppDeleteController);

    WxAppDeleteController.$inject = ['$uibModalInstance', 'entity', 'WxApp'];

    function WxAppDeleteController($uibModalInstance, entity, WxApp) {
        var vm = this;

        vm.wxApp = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WxApp.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
