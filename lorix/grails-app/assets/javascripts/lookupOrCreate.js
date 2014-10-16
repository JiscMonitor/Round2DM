(function() {

  var app = angular.module('lookupOrCreate',['ui.bootstrap']);

  app.controller('LookupOrCreateCtrl', function($scope,$http,$log) {

    $log.debug("lookupOrCreate controller...");

    $scope.wibble='hello';

    $scope.updateSearchResults = function (value) {
      $log.debug("lookupOrCreate::updateSearchResults %o",value);

      var requestdata = {
        baseClass:'uk.ac.jisc.lorix.Person',
        templateValues:value
      }
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
