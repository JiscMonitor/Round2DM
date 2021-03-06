(function() {
  var app = angular.module('kirwf',['search', 'lookupOrCreate','ui.bootstrap','ngDialog']);

  app.config(['ngDialogProvider', function (ngDialogProvider) {
    ngDialogProvider.setDefaults({
        className: 'ngdialog-theme-default',
        plain: false,
        showClose: true,
        closeByDocument: false,
        closeByEscape: true
    });
  }]);

  app.controller('KIObjectEditor', function($scope,$http,ngDialog,$log) {
    $scope.root={};

    $scope.init = function(oid, modelChangeFunc, notifyRecordFunc) {

      $log.debug("init "+oid+","+modelChangeFunc);

      $scope.root.__oid = oid;
      $scope.notifyRecordFunc = notifyRecordFunc;

      // If we are passed a modelChangeFunc, register a watch to be notified...
      $log.debug("Testing modelChangeFunc..");
      if ( modelChangeFunc !== undefined ) {
        $scope.$watch("root", function(newValue, oldValue) {
           modelChangeFunc(newValue);
        }, true);
      }
    };

    $scope.processForm = function() {
      // alert('processForm::'+lorixBaseUrl+'activity/ngEdit');

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
             // the ngEdit should pass us back the root data with corrected __oid entries. Replace what we had here. 
             // It may also send back error messages for problem areas.
             $scope.root=data.root;

             if ( $scope.notifyRecordFunc != undefined ) {
               $log.debug("calling notifyRecordFunc... %o",$scope.notifyRecordFunc);
               $scope.notifyRecordFunc(data.root);
             }
             else {
               $log.debug("No  notifyRecordFunc... continue");
             }
        });
    };

    // $scope.updateSearchResults = function () {
    //   $log.debug("EDITOR:updateSearchResults");
    // }


    $scope.addNew = function(cls,prop) {
      var listprop = eval("$scope."+prop);
      if ( listprop == null ) {
        eval("$scope."+prop+"=[{\"__oid\":'"+cls+":NEW'}]");
      }
      else {
        listprop.push({"__oid":cls+':NEW'});
      }
    }
  });

  app.controller('KIRwfDesktop', function($scope) {
    $scope.tabs = [
        { id:1, title:'One', active:true, view:'/lorix/assets/partials/one.html'  },
        { id:2, title:'Two', active:false, view:'/lorix/assets/partials/two.html' },
        { id:3, title:'Three', active:false, view:'/lorix/assets/partials/three.html' }
    ];

    var setAllInactive = function() {
      angular.forEach($scope.tabs, function(workspace) {
        workspace.active = false;
      });
    };
 
    var addNewWorkspace = function(theview, cfg) {
          var id = $scope.tabs.length + 1;
          $scope.tabs.push({
              id: id,
              title: "Workspace " + id,
              active: true,
              view: theview,
              cfg: cfg,
              root : {}
          });
    };
 
    // Called to add to workspace, passes in config from currentFolder
    $scope.addWorkspace = function (item) {
      setAllInactive();
      addNewWorkspace(item.view, item.cfg);
    };       

    var appState = {
      username:'Fred'
    };

    $scope.currentFolder = [
      { type:'folder', name:'My Workspace', icon:'fa-folder-open', config:{}},
      { type:'tool', name:'Global Search', icon:'fa-search', config:{}},
      { type:'tool', name:'Describe Output ', icon:'pencil-square-o', view:'/lorix/assets/partials/objectCreatePanel.html', 
              cfg:{template:'/lorix/assets/partials/researchOutput.html', heading:'Describe Research Output'}}
    ];

  });

  app.directive('referenceProperty', function() {

    return {
      restrict:'E',
      scope: {
        ctx:'=ctx',   // ctx is the JS map of the parent object
        disp:'=disp',
        prop:'=prop',
        createOptions:'=createOptions'
      },
      templateUrl:'/lorix/assets/partials/referenceProperty.html',
      controller: function($scope, $element, $attrs, $location, ngDialog) {
        this.changeValue=function() {
          ngDialog.open({ template: '/lorix/assets/partials/lookupOrCreate.html',
                          className: 'ngdialog-theme-default width800',
                          scope: $scope });
        };
      },
      controllerAs:'referenceProperty'
    }
  });

})();
