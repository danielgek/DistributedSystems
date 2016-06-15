var fundingApp = angular.module('fundingApp', ['ui.router','ngMaterial', 'ngMessages','ngSanitize'  ]);
fundingApp.config(function($stateProvider, $urlRouterProvider, $mdThemingProvider) {


    $urlRouterProvider.otherwise("/login");

    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: "pages/login.html",
            controller: 'loginController'
        }).state('register', {
            url: "/register",
            templateUrl: "pages/register.html",
            controller: 'registerController'
        }).state('dashboard',{
            url:'/dashboard',
            templateUrl : 'pages/main.html',
            controller : 'dashboardController'
        }).state('new-project',{
            url:'/new-project',
            templateUrl : 'pages/new-project.html',
            controller : 'newProjectController'
        }).state('project',{
            url:'/project/:id',
            templateUrl : 'pages/project.html',
            controller : 'projectController'
        }).state('edit-project',{
            url:'/edit-project/:id',
            templateUrl : 'pages/edit-project.html',
            controller : 'editProjectController'
        });

    $mdThemingProvider.theme('default')
        .primaryPalette('blue-grey')
        .accentPalette('orange');

    
});

fundingApp.service('storageService', function($http, $log, $rootScope, $mdToast, $state){
    
    storageService = this;
    storageService.logged = false;

    if(window.tumblrUser != undefined){
        //that's a tumblr user so lets add user to the service and proceed to dashboard
        console.log("here2");
        storageService.user = window.user;
        storageService.WebSocket();
        $state.go('dashboard'); 
    }// no need fo else because it's a normal user

    $rootScope.$on('$locationChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        if(storageService.logged === false ){
            console.log("here 3");
            $state.go('login');
        }
    });

    storageService.headers = {
        'Accept': 'application/json'
    }

    storageService.WebSocket = function(){
        if ("WebSocket" in window)
        {
            
           
            // Let us open a web socket
            var ws = new WebSocket("ws://" + window.location.hostname + ":8080/ws/"+storageService.user.id);
            
            ws.onopen = function()
            {
                // Web Socket is connected, send data using send()
                //ws.send("Message to send");
                //alert("Message is sent...");
            };
                
            ws.onmessage = function (evt) 
            { 
                var toast = null;
                
                console.log(evt);

                if(evt.data.response.message === "pledged"){
                    toast = $mdToast.simple()
                    .content('New Pledge!')
                    .action('Go')
                    .highlightAction(true);
                }else{
                    toast = $mdToast.simple()
                    .content('New Message!')
                    .action('Go')
                    .highlightAction(true);
                }
                

                $mdToast.show(toast).then(function(response) {
                    if ( response == 'ok' ) {
                        $state.go('project',{id : evt.data.response.object});
                    }
                });
                  

               };
                
               ws.onclose = function()
               { 
                  // websocket is closed.
                  //alert("Connection is closed..."); 
               };
            }
            
            else
            {
               // The browser doesn't support WebSocket
               console.log("WebSocket NOT supported by your Browser!");
            }
         }

    
});

