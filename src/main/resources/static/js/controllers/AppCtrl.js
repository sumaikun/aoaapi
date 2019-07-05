/*global angular */

/**
 * The main controller for the app. The controller:
 * - retrieves and persists the model via the todoStorage service
 * - exposes the model to the template and provides event handlers
 */
angular.module('aoacustomers')
  .controller('UserInfoCtrl', function TodoCtrl (store, actions, $scope) {
    'use strict';
    store.addStateTo($scope);

    $scope.info = {};

    $scope.getUserWarrantyStatus = () =>
    {
      event.preventDefault();
      //console.log($scope.info);
      actions.getUserWarrantyStatus($scope.info);
    }


});


angular.module('aoacustomers')
  .controller('CustomerDataCtrl', function TodoCtrl (store, actions, $scope, $location, $filter) {
    'use strict';
    store.addStateTo($scope);

    $scope.warrantyDone = false;

    const init = () => {

       console.log($scope.siniestro);

      !$scope.siniestro ?  (() => {

        Swal.fire({
          type: 'error',
          title: 'Oops...',
          text: decode_utf8('¡Necesitas ingresar tu información para acceder al sistema!'),
        });

        $location.path("/");

      })(): (()=>{

        let filteredAuths = $filter('filter')($scope.autorizaciones,function(autorizacion){
            return autorizacion.estado != 'R'
        });

        if (filteredAuths){

          if (filteredAuths.length > 0){
            $scope.warrantyDone = true;


            let html = "¡ Estimado Usuario!, Ya se encuentra una o más garantías procesadas.";


            html += "<table>";
            html += "  <tbody>";
            html += "    <tr>";
            html += "        <th>Responsable</th>";
            html += "        <th>Fecha de Solicitud</th>";
            html += "        <th>Fecha de Proceso</th>";
            html += "        <th>Resultado</th>";
            html += "    </tr>";
            $scope.autorizaciones.forEach((autorizacion)=>{
              html+="<tr>";
              html+="<td style='font-size:12px' >"+autorizacion.nombre+"</td>";
              html+="<td style='font-size:12px'>"+autorizacion.fecha_solicitud+"</td>";

              let proceso = autorizacion.fecha_devolucion ? autorizacion.fecha_devolucion : '';

              html+="<td style='font-size:12px'>"+proceso+"</td>";

              let estado = autorizacion.estado == 'A' ? 'Aceptada' :
              autorizacion.estado == 'R' ? 'Rechazada' :
              autorizacion.estado == 'E' ? 'Espera' : ''

              html+="<td style='font-size:12px'>"+estado+"</td>";
              html+="</tr>";
            });
            html += "    <tbody>";
            html += "    </tbody>";
            html += "</table>";

            Swal.fire({
                title: decode_utf8('<strong>Garantías actuales</strong>'),
                type: 'info',
                html:decode_utf8(html),
                showCloseButton: true,
                showCancelButton: true,
                focusConfirm: false,
                confirmButtonText:
                  '<i class="fa fa-thumbs-up"></i> Super!',
                confirmButtonAriaLabel: 'Bien',
                cancelButtonText:
                  'Ok',
                cancelButtonAriaLabel: 'Thumbs down',
              })

          }

        }

      })();
    }

    init();

  $scope.goToCheck = () => {
    $location.path("checkUserInfo");
  }



});



angular.module('aoacustomers')
  .controller('checkUserInfoCtrl', function TodoCtrl (store, actions, $scope, $location) {
    'use strict';
    store.addStateTo($scope);

    $scope.userToSearch = {};

    const init = () => {
       console.log("CHECK USER INFO");
      !$scope.siniestro ? (() => {
        Swal.fire({
          type: 'error',
          title: 'Oops...',
          text: decode_utf8('¡Necesitas ingresar tu información para acceder al sistema!'),
        });
        $location.path("/");
      })(): false;
    }

    init();

    $scope.checkCustomerStatus = () =>{
      event.preventDefault();
      actions.checkCustomerStatus($scope.userToSearch.identification);
    }

});



angular.module('aoacustomers')
  .controller('customerRegisterCtrl', function TodoCtrl (store, actions, $scope, $location, $timeout, $window) {
    'use strict';
    store.addStateTo($scope);

    console.log($scope.customerInfo);

    $scope.selectedDeparment = "";

    $scope.agreementPersonalData = false;

    const init = () => {
       console.log("customer register");
       console.log($scope.cliente);
      !$scope.siniestro  ? (() => {
        Swal.fire({
          type: 'error',
          title: 'Oops...',
          text: decode_utf8('¡Necesitas ingresar tu información para acceder al sistema!'),
        });
        $location.path("/");
      })(): (()=>{
        //$timeout(function(){actions.loadUserForm()}, 2000);
        actions.loadUserForm()
      })();
    }

    init();


    const init2 = () => {

      $scope.customerInfo != null   ? (() => {
        Swal.fire({
          title: 'Oops...',
          text: decode_utf8("¡Ya estabas en garantía :( , tendras que volver a verificar tu información!"),
          type: 'warning',
          showCancelButton: true,
          confirmButtonColor: '#3085d6',
          cancelButtonColor: '#d33',
          cancelButtonText: 'Ok',
          confirmButtonText: 'quiero ir al inicio'
        }).then((result) => {
          if (result.value) {
            console.log("here");
            $window.location.assign('#/');
          }
          else{
              $window.location.assign('#/checkUserInfo');
          }
        });

      })(): false;
    }

    init2();

    $scope.findCities = () => {
      console.log($scope.selectedDeparment);
      actions.findCities($scope.selectedDeparment);
    }

    $scope.persistCustomer = () =>{
      event.preventDefault();
      if($scope.agreementPersonalData)
      {
        actions.persistCustomer($scope.cliente);
      }else{
        Swal.fire({
          type: 'warning',
          title: 'Espera',
          text: decode_utf8('¡Debes aceptar los terminos y condiciones de uso de tratamiento de datos personales para continuar!'),
        });
      }
    }

});



angular.module('aoacustomers')
  .controller('warrantyProccessCtrl', function TodoCtrl (store, actions, $scope, $location, $timeout, $filter) {
    'use strict';
    store.addStateTo($scope);
    $scope.currentView = null;
    $scope.existMessage = false;
    $scope.toContinue = false;

    $scope.warranty = {franquicia:""};


    $scope.warrantyType = null;

    console.log($scope.customerInfo);
    console.log($scope.autorizaciones);
    const init = () => {
       console.log("customer register");
       console.log($scope.cliente);
      !$scope.siniestro || !$scope.customerInfo || !$scope.cita  ? (() => {
        Swal.fire({
          type: 'error',
          title: 'Oops...',
          text: decode_utf8('¡Necesitas ingresar tu información para acceder al sistema!'),
        });
        $location.path("/");
      })(): (()=>{
          let filteredAuths = $filter('filter')($scope.autorizaciones,function(autorizacion){
              return autorizacion.estado != 'R'
          });

          if (filteredAuths){
            if(filteredAuths.length > 0)
            {
              $scope.existMessage = true;
              $scope.toContinue = true;
            }
            else{
              actions.getFranchises();
            }
          }
          else{
            actions.getFranchises();
          }
            //actions.getFranchises();
            //console.log($scope.aseguradora);
      })();
    }

    init();

    $scope.warrantyType = (type) => {

      if($scope.toContinue)
      {
        Swal.fire({
          type: 'warning',
          title: 'espera',
          text: decode_utf8('¡Ya ingresaste datos de garantía, para continuar visitenos en nuestras oficinas o marque al 7448655!'),
        });

        return;
      }


      switch(type)
      {
        case "credit":
          $scope.aseguradora.garantia > 0 ?
              $scope.currentView = 'js/views/subviews/creditCardWarranty.html' : Swal.fire({
              type: 'warning',
              title: 'espera',
              text: decode_utf8('¡Tu aseguradora no tiene este tipo de garantía!'),
            });
            break;
        case "consignment":
          $scope.aseguradora.garantia_consignada > 0 ?
            $scope.currentView = 'js/views/subviews/consignmentWarranty.html' : Swal.fire({
              type: 'warning',
              title: 'espera',
              text: decode_utf8('¡Tu aseguradora no tiene este tipo de garantía!'),
            });
            break;
        case "full":
          $scope.aseguradora.valor_no_reembols > 0 ?
            $scope.currentView = 'js/views/subviews/fullCoverageWarranty.html' : Swal.fire({
              type: 'warning',
              title: 'espera',
              text: decode_utf8('¡Tu aseguradora no tiene este tipo de garantía!'),
            });
            break;
        default:
            break;
      }

    }

    $scope.generateCreditWarranty = () => {
        console.log($scope.warranty);
        $scope.warrantyType = 1;
        /*Swal.fire({
          type: 'success',
          title: 'Bien',
          text: decode_utf8('¡Procede a ingresar tus datos de devolución!'),
        });*/
        $scope.sendWarrantyInfo();
    }

    $scope.generateConsignmentWarranty = () => {
        console.log($scope.warranty);
        $scope.warrantyType = 2;
        Swal.fire({
          type: 'success',
          title: 'Bien',
          text: decode_utf8('¡Procede a ingresar tus datos de devolución!'),
        });
    }

    $scope.generateFullWarranty = () => {
        console.log($scope.warranty);
        $scope.warrantyType = 3;
        Swal.fire({
          type: 'success',
          title: 'Bien',
          text: decode_utf8('¡Procede a ingresar tus datos de devolución!'),
        });
    }

    $scope.sendWarrantyInfo = () => {


      if($scope.warrantyType == 1)
      {
        $scope.warranty.franquicia = $scope.warranty.franquicia.id;
        $scope.warranty.identificacionCliente = $scope.customerInfo;
        $scope.warranty.valor_garantia = $scope.aseguradora.garantia;
        actions.generateCreditCardWarranty($scope.warranty);

        return;
      }
      if($scope.warrantyType == 2)
      {
        $scope.warranty.identificacionCliente = $scope.customerInfo;
        $scope.warranty.valor_garantia = $scope.aseguradora.garantia_consignada;
        actions.generateConsignmentCardWarranty($scope.warranty);
        return;
      }
      if($scope.warrantyType == 3)
      {
        $scope.warranty.identificacionCliente = $scope.customerInfo;
        $scope.warranty.valor_garantia = $scope.aseguradora.valor_no_reembols;
        actions.generateConsignmentCardWarranty($scope.warranty);
        return;
      }

      else{
        Swal.fire({
          type: 'success',
          title: 'Bien',
          text: decode_utf8('¡Ahora ingresa tus datos de garantía!'),
        });
      }
    }

    $scope.exit = () =>{
      Swal.fire({
        type: 'success',
        title: 'Gracias',
        text: decode_utf8('¡por utilizar nuestros servicios para continuar acercate a las oficinas de AOA o marca al al 7448655!'),
      });
      localStorage.clear();
      $location.path("/");
    }

});
