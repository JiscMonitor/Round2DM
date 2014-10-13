(function() {

  var app = angular.module('search',['ui.bootstrap']);

  app.directive('searchPanel', function() {
    return {
      restrict:'E',
      scope: {
        searchId:'=searchId'
      },
      templateUrl:'/lorix/assets/partials/searchPanel.html',
      controller: function($scope, $element, $attrs, $location, ngDialog) {
      },
      controllerAs:'searchCtrl'
    }
  });

})();
