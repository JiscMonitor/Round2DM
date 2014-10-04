(function() {
  var app = angular.module('kirwf',['ui.bootstrap']);

  app.controller('KIRwfDesktop', function($scope) {
    $scope.tabs = [
        { id:1, title:'One', active:true, view:'/lorix/assets/partials/one.html' },
        { id:2, title:'Two', active:false, view:'/lorix/assets/partials/two.html' },
        { id:3, title:'Three', active:false, view:'/lorix/assets/partials/three.html' }
    ]

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
              view: theview
          });
    };
 
    $scope.addWorkspace = function (theview) {
      setAllInactive();
      addNewWorkspace(theview);
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
