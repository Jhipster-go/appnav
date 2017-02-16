'use strict';

describe('Controller Tests', function() {

    describe('WxApp Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWxApp, MockComment, MockCategory, MockTag, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWxApp = jasmine.createSpy('MockWxApp');
            MockComment = jasmine.createSpy('MockComment');
            MockCategory = jasmine.createSpy('MockCategory');
            MockTag = jasmine.createSpy('MockTag');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WxApp': MockWxApp,
                'Comment': MockComment,
                'Category': MockCategory,
                'Tag': MockTag,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("WxAppDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'appnavApp:wxAppUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
