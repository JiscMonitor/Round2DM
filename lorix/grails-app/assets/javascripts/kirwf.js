(function() {
  var app = angular.module('kirwf',['ui.bootstrap']);

  app.controller('KIRwfDesktop', function($scope) {
    $scope.tabs = [
        { id:1, title:'One', content:'One Content', active:true, view:'/lorix/assets/partials/one.html' },
        { id:2, title:'Two', content:'Two Content', active:false, view:'/lorix/assets/partials/two.html' },
        { id:3, title:'Three', content:'Three Content', active:false, view:'/lorix/assets/partials/three.html' }
    ]

    var setAllInactive = function() {
      angular.forEach($scope.tabs, function(workspace) {
        workspace.active = false;
      });
    };
 
    var addNewWorkspace = function() {
          var id = $scope.tabs.length + 1;
          $scope.tabs.push({
              id: id,
              title: "Workspace " + id,
              active: true,
              content: "This is a new tab...",
              view:'/lorix/assets/partials/one.html'
          });
    };
 
    $scope.addWorkspace = function () {
      setAllInactive();
      addNewWorkspace();
    };       

    var appState = {
      username:'Fred'
    };

    $scope.currentFolder = [
      { type:'folder', name:'My Workspace', icon:'fa-folder-open'},
      { type:'tool', name:'Global Search', icon:'fa-search'},
      { type:'tool', name:'Describe Output ', icon:'pencil-square-o'}
    ];

  });

})();
