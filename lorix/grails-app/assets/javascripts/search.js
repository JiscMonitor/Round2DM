(function() {

  var app = angular.module('search',['ui.bootstrap']);

  app.directive('searchPanel', function() {
    return {
      restrict:'E',
      scope: {
        searchId:'=searchId',
        root:{}
      },
      templateUrl:'/lorix/assets/partials/searchPanel.html',
      controller: function($scope, $element, $attrs, $location, $http, $log, ngDialog) {

        $scope.$watch("root", function(newValue, oldValue) {
          console.log("boo");
        }, true);

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
