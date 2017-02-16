(function() {
    'use strict';

    angular
        .module('appnavApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wx-app', {
            parent: 'entity',
            url: '/wx-app',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'appnavApp.wxApp.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wx-app/wx-apps.html',
                    controller: 'WxAppController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wxApp');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('wx-app-detail', {
            parent: 'wx-app',
            url: '/wx-app/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'appnavApp.wxApp.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wx-app/wx-app-detail.html',
                    controller: 'WxAppDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('wxApp');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WxApp', function($stateParams, WxApp) {
                    return WxApp.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wx-app',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wx-app-detail.edit', {
            parent: 'wx-app-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wx-app/wx-app-dialog.html',
                    controller: 'WxAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WxApp', function(WxApp) {
                            return WxApp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wx-app.new', {
            parent: 'wx-app',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wx-app/wx-app-dialog.html',
                    controller: 'WxAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                icon: null,
                                name: null,
                                description: null,
                                publicationDate: null,
                                screenshot: null,
                                qcode: null,
                                score: null,
                                views: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wx-app', null, { reload: 'wx-app' });
                }, function() {
                    $state.go('wx-app');
                });
            }]
        })
        .state('wx-app.edit', {
            parent: 'wx-app',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wx-app/wx-app-dialog.html',
                    controller: 'WxAppDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WxApp', function(WxApp) {
                            return WxApp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wx-app', null, { reload: 'wx-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wx-app.delete', {
            parent: 'wx-app',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wx-app/wx-app-delete-dialog.html',
                    controller: 'WxAppDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WxApp', function(WxApp) {
                            return WxApp.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wx-app', null, { reload: 'wx-app' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
