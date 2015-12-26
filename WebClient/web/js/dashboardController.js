fundingApp.controller('dashboardController', ['$scope', '$log', '$http', '$mdDialog', '$state','storageService', function($scope, $log, $http, $mdDialog, $state, storageService){
	$scope.projects = [];
	$scope.currentProjects = [];
	$scope.oldProjects = [];
    $scope.loadingProjects = false;
	$scope.getProjects = function(){
        if(!$scope.loadingProjects){
            $scope.loadingProjects = true;
            $http.post("/dashboard",{},storageService.headers)
            .success(function(data, status, headers, config){
                $log.info(data);
                if(typeof(data.response.success) != 'undefined'){
                    if(data.response.success === true){
                        $log.info(data);
                        $scope.projects = data.projects;
                        $scope.currentProjects = data.currentProjects;
                        $scope.oldProjects = data.oldProjects;

                    }
                }else{
                    $state.go('login');
                }
                $scope.loadingProjects = false;
            }).error(function(data, status, headers, config){
                $state.go('login');
                $scope.loadingProjects = false;
            });
        }
    };

    $scope.getProjects();
}]);