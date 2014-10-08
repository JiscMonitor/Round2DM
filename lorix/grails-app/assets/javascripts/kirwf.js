(function() {
  var app = angular.module('kirwf',['ui.bootstrap']);

  app.controller('KIObjectEditor', function($scope,$http) {
    $scope.root={};
    $scope.init = function(oid) {
      $scope.root.__oid = oid;
    };

    $scope.processForm = function() {
      alert('processForm::'+lorixBaseUrl+'activity/ngEdit');

      requestdata = {}
      requestdata.root = $scope.root
      $http({
        method  : 'POST',
        url     : lorixBaseUrl+'activity/ngEdit',
        data    : requestdata  // Send JSON
        // data    : $.param(requestdata),  // pass in data as strings
        // headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
      })
        .success(function(data) {
             console.log(data);
      //       if (!data.success) {
      //       	// if not successful, bind errors to error variables
      //           $scope.errorName = data.errors.name;
      //           $scope.errorSuperhero = data.errors.superheroAlias;
      //       } else {
      //       	// if successful, bind success message to message
      //           $scope.message = data.message;
      //       }
        });
    };

    $scope.addNew = function(cls,prop) {
      alert("addNew("+cls+","+prop+")");
    }
  });

  app.controller('KIRwfDesktop', function($scope) {
    $scope.tabs = [
        { id:1, title:'One', active:true, view:'/lorix/assets/partials/one.html' },
        { id:2, title:'Two', active:false, view:'/lorix/assets/partials/two.html' },
        { id:3, title:'Three', active:false, view:'/lorix/assets/partials/three.html' }
    ];

    var setAllInactive = function() {
      angular.forEach($scope.tabs, function(workspace) {
        workspace.active = false;
      });
    };
 
    var addNewWorkspace = function(theview) {
          var id = $scope.tabs.length + 1;
          $scope.tabs.push({
              id: id,
              title: "Workspace " + id,
              active: true,
              view: theview,
              root : {}
          });
    };
 
    // Called to add to workspace, passes in config from currentFolder
    $scope.addWorkspace = function (item) {
      setAllInactive();
      addNewWorkspace(item.view);
    };       

    var appState = {
      username:'Fred'
    };

    $scope.currentFolder = [
      { type:'folder', name:'My Workspace', icon:'fa-folder-open'},
      { type:'tool', name:'Global Search', icon:'fa-search'},
      { type:'tool', name:'Describe Output ', icon:'pencil-square-o', view:'/lorix/assets/partials/researchOutput.html'}
    ];

  });

})();
