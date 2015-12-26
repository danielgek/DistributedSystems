fundingApp.controller('loginController', ['$scope', '$log', '$http', '$mdDialog', '$state', 'storageService', function($scope, $log, $http, $mdDialog, $state, storageService){



    $scope.username = 'daniel';
    $scope.password = 'daniel';

    $scope.loading = false;

    $scope.loginRequest = function(){
        if($scope.loading == true){
            return;
        }
        $scope.loginData = {
            username: $scope.username,
            password: $scope.password
        };

        $scope.headers = {
            'Accept': 'application/json'
        }

        $scope.showWrongCardentialsAlert = function(data) {
            // Appending dialog to document.body to cover sidenav in docs app
            // Modal dialogs should fully cover application
            // to prevent interaction outside of dialog
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.body))
                    .title('Wrong username or password')
                    .content(data)
                    .ariaLabel('Wrong username or password')
                    .ok('Try again!')
            );
        };
        $scope.showInternetConnectionAlert = function() {
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.body))
                    .title('Internet Connection Error')
                    .content('Please verify you internet connection!!')
                    .ariaLabel('Internet Connection Error')
                    .ok('Try again!')
            );
        };


        $http.post('/login', $scope.loginData, {headers : $scope.headers}).
            success(function(data, status, headers, config) {
                $log.info(data);
                $scope.loading = false;

                if(data.response.success === true){
                    storageService.user = data.response.object;
                    storageService.WebSocket();
                    $state.go('dashboard');
                }else{
                    $scope.password = '';
                    if(status < 200){
                        $scope.showInternetConnectionAlert();
                    }else {
                        $scope.showWrongCardentialsAlert(data.response.message);
                    }

                }
                


            }).
            error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.password = '';
                if(status < 200){
                    $scope.showInternetConnectionAlert();
                }else {
                    $scope.showWrongCardentialsAlert(data.response.message);
                }
            });
    };



    /*$scope.$watch(
        function () { return storageService.loaded; },
        function (data) {if(storageService.loaded == true){$state.go('home.markets');}},
        true
    );*/
}]);