var fundingApp = angular.module('fundingApp', ['ui.router','ngMaterial']);
fundingApp.config(function($stateProvider, $urlRouterProvider,$mdThemingProvider) {


    $urlRouterProvider.otherwise("/login");

    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: "pages/login.html",
            controller: 'loginController'
        }).state('dashboard',{
            url:'/dashboard',
            templateUrl : 'pages/main.html',
            controller : 'dashboardController'
        });
    $mdThemingProvider.theme('default')
        .primaryPalette('blue-grey')
        .accentPalette('orange');
});

fundingApp.service('storageService', function($http,$log,$rootScope){
    storageService = this;

    $rootScope.$on('$stateChangeStart',
    function(event, toState, toParams, fromState, fromParams){
        
        if(fromState.name.contains('home')){
            storageService.currentlyOpen = null;
        }

    });

    storageService.headers = {
        'Accept': 'application/json'
    }

    
});

