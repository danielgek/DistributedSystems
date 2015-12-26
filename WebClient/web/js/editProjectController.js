fundingApp.controller('editProjectController', ['$scope', '$log', '$http', '$mdDialog', '$state', '$stateParams', 'storageService', function($scope, $log, $http, $mdDialog, $state, $stateParams, storageService){
	$scope.project = {};
	$scope.levels = [];
	$scope.polls = [];
	$scope.rewards = [];
	$scope.user = storageService.user;
	$http.post("http://localhost:8080/getProject", { project : { id: $stateParams.id  } },storageService.headers).
        success(function(data, status, headers, config) {
            $log.info(data);
            data.project.limit = new Date(data.project.limit);
            $scope.project = data.project;

			$scope.levels = data.levels;
			$scope.polls = data.polls;
			$scope.rewards = data.rewards;

        }).
        error(function(data, status, headers, config) {
           $log.info(data +"");
        });

    $scope.save = function(){
    	$scope.saveData = {
    		project : $scope.project,
			levels : $scope.levels,
			polls : $scope.polls,
			rewards : $scope.rewards
    	}
    	

    	$http.post('http://localhost:8080/editProject', $scope.saveData, {headers : $scope.headers}).
            success(function(data, status, headers, config) {
                $log.info(data);
            }).
            error(function(data, status, headers, config) {
               $log.info(data +"");
            });
    }

    $scope.removeReward = function(id){
    	for (var i = 0; i < $scope.rewards.length; i++) {
    		if($scope.rewards[i].id === id){
    			$scope.rewards.splice(i, 1);		
    		}
    	}
    }

    $scope.removePoll = function(id){
    	for (var i = 0; i < $scope.polls.length; i++) {
    		if($scope.polls[i].id === id){
    			$scope.polls.splice(i, 1);		
    		}
    	}
    }

    $scope.removeLevel = function(id){
    	for (var i = 0; i < $scope.levels.length; i++) {
    		if($scope.levels[i].id === id){
    			$scope.levels.splice(i, 1);		
    		}
    	}
    }

    $scope.showAddRewardDialog = function($event) {
       	var parentEl = angular.element(document.body);
       	$mdDialog.show({
       		parent: parentEl,
         	targetEvent: $event,
    		clickOutsideToClose : true,
    		autoWrap: false,
         	templateUrl: 'pages/dialogs/reward-dialog.html',
	        locals: {
	          rewards: $scope.rewards,
	          project: $scope.project
	        },
	        controller: function($scope, $mdDialog, rewards, project) {
	        	$scope.description ='';
	        	$scope.value = 1;
	        	$scope.addReward = function(description, value){
	        		rewards.push({description: description, value: value, projectId: project.id});
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
      	
    }
    $scope.showAddPollDialog = function($event) {
       	var parentEl = angular.element(document.body);
       	$mdDialog.show({
       		parent: parentEl,
         	targetEvent: $event,
    		clickOutsideToClose : true,
    		autoWrap: false,
         	templateUrl: 'pages/dialogs/poll-dialog.html',
	        locals: {
	          polls: $scope.polls,
	          project: $scope.project
	        },
	        controller: function($scope, $mdDialog, polls, project) {
	        	
	        	$scope.addPoll = function(title, description, answer1,answer2){
	        		polls.push({title: title, description: description, answer1: answer1, answer2: answer2,projectId: project.id});
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
    }
    $scope.showAddLevelDialog = function($event) {
       	var parentEl = angular.element(document.body);
       	$mdDialog.show({
       		parent: parentEl,
         	targetEvent: $event,
    		clickOutsideToClose : true,
    		autoWrap: false,
         	templateUrl: 'pages/dialogs/level-dialog.html',
	        locals: {
	          	levels: $scope.levels,
	          	project: $scope.project
	        },
	        controller: function($scope, $mdDialog, levels, project) {
	        	$scope.description ='';
	        	$scope.goal = 1;
	        	$scope.addLevel = function(description, goal){
	        		levels.push({description: description, goal: goal, projectId: project.id});
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
    }
	

}]);