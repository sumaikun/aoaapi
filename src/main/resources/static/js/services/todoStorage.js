/*global angular */

/**
 * Services that persists and retrieves TODOs from localStorage
 */

const checkLastActivity = () =>{
	let hours = 0.25; // Reset when storage is more than 24hours
	let now = new Date().getTime();
	let setupTime = localStorage.getItem('setupTime');
	if (setupTime == null) {
	    localStorage.setItem('setupTime', now)
	} else {
	    if(now-setupTime > hours*60*60*1000) {
	        localStorage.clear()
	        localStorage.setItem('setupTime', now);
	    }
	}
}

angular.module('aoacustomers')
	.factory('todoStorage', function () {
		'use strict';

		return {
			get: function (key) {
				checkLastActivity();
				return JSON.parse(localStorage.getItem(key) || null );
			},

			put: function (key , value) {
				localStorage.setItem(key, JSON.stringify(value));
			},

			delete: function(key){
				localStorage.removeItem(key);
			}
		};
	});


	angular.module('aoacustomers')
		.factory('httpRequest', function ($http) {
			'use strict';

			return {
				get: function (url,successCallBack,errorCallBack) {
					$http.get(BASE_URL+url).then(res=>{
						successCallBack ? successCallBack(res):false;
		      }).catch(err=>{
							errorCallBack ? errorCallBack(err):false;
		          console.log(err);
		          Swal.fire({
		            type: 'error',
		            title: 'Oops...',
		            text: '¡Al parecer el sistema no esta disponible intentalo mas tarde!',
		          })
		      });
				},

				put: function (url,data,successCallBack,errorCallBack) {
					$http.put(BASE_URL+url,data).then(res=>{
						successCallBack ? successCallBack(res):false;
					}).catch(err=>{
							errorCallBack ? errorCallBack(err):false;
							console.log(err);
							Swal.fire({
								type: 'error',
								title: 'Oops...',
								text: '¡Al parecer el sistema no esta disponible intentalo mas tarde!',
							})
					});
				},

				post: function (url,data,successCallBack,errorCallBack) {
					$http.post(BASE_URL+url,data).then(res=>{
						successCallBack ? successCallBack(res):false;
		      }).catch(err=>{
							errorCallBack ? errorCallBack(err):false;
		          console.log(err);
		          Swal.fire({
		            type: 'error',
		            title: 'Oops...',
		            text: '¡Al parecer el sistema no esta disponible intentalo mas tarde!',
		          })
		      });
				},

			};
		});
