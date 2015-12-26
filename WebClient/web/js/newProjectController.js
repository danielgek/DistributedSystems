fundingApp.controller('newProjectController', ['$scope', '$log', '$http', '$mdDialog', '$state', function($scope, $log, $http, $mdDialog, $state){
	$scope.project;
	$scope.levels = [];
	$scope.polls = [];
	$scope.rewards = [];

	$scope.showAddRewardDialog = function($event) {
       	var parentEl = angular.element(document.body);
       	$mdDialog.show({
       		parent: parentEl,
         	targetEvent: $event,
    		clickOutsideToClose : true,
    		autoWrap: false,
         	templateUrl: 'pages/dialogs/reward-dialog.html',
	        locals: {
	          rewards: $scope.rewards
	        },
	        controller: function($scope, $mdDialog, rewards) {
	        	$scope.description ='';
	        	$scope.value = 1;
	        	$scope.addReward = function(description, value){
	        		rewards.push({description: description, value: value});
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
      	console.log($scope.rewards);
      	
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
	          polls: $scope.polls
	        },
	        controller: function($scope, $mdDialog, polls) {
	        	
	        	$scope.addPoll = function(title, description, answer1,answer2){
	        		polls.push({title: title, description: description, answer1: answer1, answer2: answer2});
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
	          levels: $scope.levels
	        },
	        controller: function($scope, $mdDialog, levels) {
	        	$scope.description ='';
	        	$scope.goal = 1;
	        	$scope.addLevel = function(description, goal){
	        		levels.push({description: description, goal: goal});
	        		$mdDialog.hide();
	        	}
		       $scope.closeDialog = function() {
		         	$mdDialog.hide();
		       }
		    }
      	}); 
    }

    $scope.save = function(){
      $scope.project.adminId = storageService.user.id;
    	$scope.postData = {
    		project : $scope.project,
  			levels : $scope.levels,
  			polls : $scope.polls,
  			rewards : $scope.rewards
    	}
    	

    	$http.post('http://localhost:8080/createProject', $scope.postData, {headers : $scope.headers}).
            success(function(data, status, headers, config) {
                if(data.response.success == true){
                    $state.go('project',{id : data.project.id});
                }
            }).
            error(function(data, status, headers, config) {
               $log.info(data +"");
            });
    }

    $scope.removeReward = function(id){
    	for (var i = 0; i < $scope.rewards.length; i++) {
    		if($scope.rewards[i] === id){
    			$scope.rewards.splice(i, 1);		
    		}
    	};
    }

    $scope.removePoll = function(id){
    	for (var i = 0; i < $scope.polls.length; i++) {
    		if($scope.polls[i] === id){
    			$scope.polls.splice(i, 1);		
    		}
    	};
    }

    $scope.removeLevel = function(id){
    	for (var i = 0; i < $scope.levels.length; i++) {
    		if($scope.levels[i] === id){
    			$scope.levels.splice(i, 1);		
    		}
    	};
    }



}]);