(function() {

  var app = angular.module('lookupOrCreate',['ui.bootstrap']);

  app.controller('LookupOrCreateCtrl', function($scope,$http,$log) {

    $scope.init = function(baseclass, proplist) {
      $scope.baseclass = baseclass;
      $scope.proplist = proplist;
    }

    $scope.notifyRecord = function(value) {
      $log.debug("lookupOrCreate::notifyRecordFunc(%o)",value);
    }

    $scope.updateSearchResults = function (value) {

      $log.debug("lookupOrCreate::updateSearchResults %o",value);

      var requestdata = {
        baseClass:$scope.baseclass, // 'uk.ac.jisc.lorix.Person',
        templateValues:value,
        proplist:$scope.proplist
      };

      $http({
        method  : 'POST',
        url     : lorixBaseUrl+'activity/search',
        data    : requestdata  // Send JSON
      })
        .success(function(data) {
          $scope.root=data.root;
        });

    }

    
  });

})();
