(function() {
    'use strict';

    angular
        .module('appnavApp')
        .controller('WxAppDialogController', WxAppDialogController);

    WxAppDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WxApp', 'Comment', 'Category', 'Tag', 'User'];

    function WxAppDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WxApp, Comment, Category, Tag, User) {
        var vm = this;

        vm.wxApp = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.comments = Comment.query();
        vm.categories = Category.query();
        vm.tags = Tag.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.wxApp.id !== null) {
                WxApp.update(vm.wxApp, onSaveSuccess, onSaveError);
            } else {
                WxApp.save(vm.wxApp, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('appnavApp:wxAppUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.publicationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
