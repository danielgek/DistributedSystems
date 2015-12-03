fundingApp.controller('dashboardController', ['$scope', '$log', '$http', '$mdDialog', '$state','storageService', function($scope, $log, $http, $mdDialog, $state, storageService){
	$scope.projects = [];
	$scope.currentProjects = [];
	$scope.oldProjects = [];
	$scope.getProjects = function(){
        $http.post("/dashboard",{},storageService.headers)
            .success(function(data, status, headers, config){
                $log.info(data);
                if(data.response.success === true){
                    $log.info(data);
                    $scope.projects = data.projects;
					$scope.currentProjects = data.currentProjects;
					$scope.oldProjects = data.oldProjects;
                }
            }).error(function(data, status, headers, config){

            });
    };

    $scope.getProjects();
}]);