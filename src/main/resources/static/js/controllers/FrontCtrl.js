/*global angular */

/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */
angular.module('aoacustomers')
  .controller('FrontCtrl', function TodoCtrl (store, actions, $scope) {
    'use strict';
    store.addStateTo($scope);

});


angular.module('aoacustomers')
  .controller('ProfileCtrl', function TodoCtrl (store, actions, $scope) {
    'use strict';
    store.addStateTo($scope);
    $scope.signOut = () => {

      
      console.log("sign out");
      actions.signOut();
    }
});
