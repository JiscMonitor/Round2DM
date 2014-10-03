(function() {
  var app = angular.module('kirwf',[]);

  app.controller('KIRwfDesktop', function() {
    this.appState = appState;
    this.currentFolder = homeFolder;
  });

  var appState = {
    username:'Fred'
  };

  var homeFolder = [
    { type:'folder', name:'My Workspace', icon:'fa-folder-open'},
    { type:'tool', name:'Global Search', icon:'fa-search'},
    { type:'tool', name:'Describe Output ', icon:'pencil-square-o'}
  ];

})();
