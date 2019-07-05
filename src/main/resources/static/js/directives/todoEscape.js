/*global angular */

/**
 * Directive that executes an expression when the element it is applied to gets
 * an `escape` keydown event.
 */
angular.module('aoacustomers')
	.directive('todoEscape', function () {
		'use strict';

		var ESCAPE_KEY = 27;

		return function (scope, elem, attrs) {
			elem.bind('keydown', function (event) {
				if (event.keyCode === ESCAPE_KEY) {
					scope.$apply(attrs.todoEscape);
				}
			});
		};
	});



  angular.module('aoacustomers').directive('onlyDigits', function () {
  return {
    require: 'ngModel',
    restrict: 'A',
    link: function (scope, element, attr, ctrl) {
      function inputValue(val) {
        if (val) {
          var digits = val.replace(/[^0-9.]/g, '');

          if (digits.split('.').length > 2) {
            digits = digits.substring(0, digits.length - 1);
          }

          if (digits !== val) {
            ctrl.$setViewValue(digits);
            ctrl.$render();
          }
          //console.log(digits);
          //console.log(parseFloat(digits));
          //return parseFloat(digits);
          return digits;
        }
        return undefined;
      }
      ctrl.$parsers.push(inputValue);
    }
  };
});
