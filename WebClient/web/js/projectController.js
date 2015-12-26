fundingApp.controller('projectController', ['$scope', '$log', '$http', '$mdDialog', '$state', 'storageService', '$stateParams', '$mdToast' ,function($scope, $log, $http, $mdDialog, $state, storageService, $stateParams, $mdToast){
	$scope.project = {};
	$scope.levels = [];
	$scope.polls = [];
	$scope.rewards = [];
	$scope.messages = [];
	$scope.user = storageService.user;
	$scope.pledgeRequestData = {};




	$http.post("http://localhost:8080/getProject", { project : { id: $stateParams.id } },storageService.headers).
        success(function(data, status, headers, config) {
        	$log.info($scope.user);
            $log.info(data);
            data.project.limit = new Date(data.project.limit);
            $scope.project = data.project;

			$scope.levels = data.levels;
			$scope.polls = data.polls;
			$scope.rewards = data.rewards;
			$scope.messages = data.messages;

        }).
        error(function(data, status, headers, config) {
           $log.info(data +"");
        });


    $scope.showPledgeDialog = function($event) {
       	var parentEl = angular.element(document.body);
       	$mdDialog.show({
       		parent: parentEl,
         	targetEvent: $event,
    		clickOutsideToClose : true,
    		autoWrap: false,
         	templateUrl: 'pages/dialogs/pledge-dialog.html',
	        locals: {
	          project: $scope.project,
	          user: $scope.user
	        },
	        controller: function($scope, $mdDialog, $mdToast, project, user) {
	        	$scope.amount = 1;
	        	$scope.pledge = function(amount){
	        		$http.post("/pledge", { pledge : { projectId: project.id, amount: amount, userId: user.id } }, storageService.headers).
				        success(function(data, status, headers, config) {
				        	
				            $log.info(data);
				            if(data.response.success === true){
				            	$mdToast.show($mdToast.simple().content('Pledged!'));


				            }else{
				            	$mdToast.show($mdToast.simple().content(data.response.message));
				            }

				        }).
				        error(function(data, status, headers, config) {
				           $log.info(data +"");
				        });
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
    }

    $scope.sendMessage = function(){
    	$scope.message.sender = $scope.user.id;
		$scope.message.receiver = $scope.project.adminId;
		$scope.message.projectId = $scope.project.id;

		
    	$http.post("/message", { message : $scope.message }, storageService.headers).
	        success(function(data, status, headers, config) {
	        	
	            $log.info(data);
	            if(data.response.success === true){
	            	$scope.messages.push(clone($scope.message));
	            	$scope.message.message = "";
	            	$mdToast.show($mdToast.simple().content('Message Sended!'));


	            }else{
	            	$mdToast.show($mdToast.simple().content(data.response.message));
	            }

	        }).
	        error(function(data, status, headers, config) {
	           $log.info(data +"");
	        });
    }
    
    function clone(obj) {
	    if(obj === null || typeof(obj) !== 'object' || 'isActiveClone' in obj)
	        return obj;

	    var temp = obj.constructor(); // changed

	    for(var key in obj) {
	        if(Object.prototype.hasOwnProperty.call(obj, key)) {
	            obj['isActiveClone'] = null;
	            temp[key] = clone(obj[key]);
	            delete obj['isActiveClone'];
	        }
	    }    

	    return temp;
	}

    $scope.removeProject = function(){
    	$http.post("/cancelProject", { projectId : $stateParams.id }, storageService.headers).
	        success(function(data, status, headers, config) {
	        	
	            $log.info(data);
	            if(data.response.success === true){
	            	
	            	$state.go('dashboard');


	            }else{
	            	$mdToast.show($mdToast.simple().content(data.response.message));
	            }

	        }).
	        error(function(data, status, headers, config) {
	           $log.info(data +"");
	        });
    }
	

}]);