/*global angular */

/**
 * The main TodoMVC app module
 *
 * @type {angular.Module}
 */
angular.module('aoacustomers', ['ngRoute', 'flux'])
	.config(function ($routeProvider) {
		'use strict';

		$routeProvider.when('/', {
			controller: 'UserInfoCtrl',
			templateUrl: 'js/views/UserInfo.html'
		}).when('/customerData', {
			controller: 'CustomerDataCtrl',
			templateUrl: 'js/views/CustomerData.html'
		}).when('/checkUserInfo', {
			controller: 'checkUserInfoCtrl',
			templateUrl: 'js/views/checkUserInfo.html'
		}).when('/customerRegister', {
			controller: 'customerRegisterCtrl',
			templateUrl: 'js/views/UserForm.html'
		}).when('/warrantyProccess', {
			controller: 'warrantyProccessCtrl',
			templateUrl: 'js/views/warrantyProccess.html'
		}).otherwise({
			redirectTo: '/'
		});
	});
