(function() {

  var app = angular.module('lookupOrCreate',['ui.bootstrap']);

  app.controller('LookupOrCreateCtrl', function($scope,$http,$log) {

    $log.debug("lookupOrCreate controller...");

    $scope.wibble='hello';

    $scope.updateSearchResults = function (value) {
      $log.debug("lookupOrCreate::updateSearchResults %o",value);
    }

    
  });

})();
