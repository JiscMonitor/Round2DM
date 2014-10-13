(function() {

  var app = angular.module('search',['ui.bootstrap']);

  app.directive('searchPanel', function() {
    return {
      restrict:'E',
      scope: {
        searchId:'=searchId'
      },
      templateUrl:'/lorix/assets/partials/searchPanel.html',
      controller: function($scope, $element, $attrs, $location, $http, ngDialog) {
        $http({
               method  : 'GET',
               url     : lorixBaseUrl+'definitions/lookup/'+$scope.searchId
             })
               .success(function(data) {
                    console.log(data);
                    $scope.searchDefn=data;
                });

      },
      controllerAs:'searchCtrl'
    }
  });

})();
