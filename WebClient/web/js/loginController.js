fundingApp.controller('loginController', ['$scope', '$log', '$http', '$mdDialog', '$state', function($scope, $log, $http, $mdDialog, $state){



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
                    .content("Please verify that you've typed the correct login details.")
                    .ariaLabel('Wrong username or password')
                    .ok('Try again!')
            );
        };
        $scope.showInternetConnectionAlert = function() {
            // Appending dialog to document.body to cover sidenav in docs app
            // Modal dialogs should fully cover application
            // to prevent interaction outside of dialog
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.body))
                    .title('Internet Connection Error')
                    .content('Please verify you internet connection!!')
                    .ariaLabel('Internet Connection Error')
                    .ok('Try again!')
            );
        };

        $scope.showUnknownErrorAlert = function() {
            // Appending dialog to document.body to cover sidenav in docs app
            // Modal dialogs should fully cover application
            // to prevent interaction outside of dialog
            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.body))
                    .title('Unknown Error')
                    .content('Unknown Error!!')
                    .ariaLabel('Unknown Error')
                    .ok('Try again!')
            );
        };
        $http.post('http://localhost:8080/login', $scope.loginData, {headers : $scope.headers}).
            success(function(data, status, headers, config) {
                $log.info(data);
                $scope.loading = false;
                $state.go('dashboard');


            }).
            error(function(data, status, headers, config) {
                $scope.loading = false;
                $scope.password = '';
                if(status < 200){
                    $scope.showInternetConnectionAlert();
                }else if(data.status == "INVALID_USERNAME_OR_PASSWORD"){
                    $scope.showWrongCardentialsAlert(data.error);
                }else if(status > 200 && status != 401){
                    $scope.showUnknownErrorAlert();
                }
            });
    };



    /*$scope.$watch(
        function () { return storageService.loaded; },
        function (data) {if(storageService.loaded == true){$state.go('home.markets');}},
        true
    );*/
}]);