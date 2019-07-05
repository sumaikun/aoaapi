angular.module('aoacustomers').service('store', function (actions, flux, $rootScope, $routeParams, todoStorage, $http, $location, httpRequest, $window, $timeout, $filter) {

  return flux.createStore(function () {

    this.addState({
      isFetching:false,
      siniestro:todoStorage.get('siniestro'),
      cita:todoStorage.get('cita'),
      aseguradora:todoStorage.get('aseguradora'),
      autorizaciones:todoStorage.get('autorizaciones'),
      ciudad:todoStorage.get('ciudad'),
      cliente:todoStorage.get('cliente'),
      departamentos:todoStorage.get('departamentos'),
      ciudades:null,
      customerInfo:todoStorage.get('customerInfo'),
      franquicias:[]
    });

    this.getUserWarrantyStatus = (data) =>{

      if(this.getState('isFetching'))
      {
        return null;
      }


      this.setState("isFetching",true);

      let successCallBack = (res) => {

        //console.log(res);
        this.setState("isFetching",false);

        if(manageUserWarrantyInfo(res))
        {
          console.log("here");
          console.log(res.data);
          this.setState("siniestro",res.data.Siniestro);
          todoStorage.put('siniestro',res.data.Siniestro);
          this.setState("cita",res.data.Cita);
          todoStorage.put("cita",res.data.Cita);
          this.setState("aseguradora",res.data.Aseguradora);
          todoStorage.put("aseguradora",res.data.Aseguradora);
          this.setState("autorizaciones",res.data.Autorizaciones);
          todoStorage.put("autorizaciones",res.data.Autorizaciones);
          this.setState("ciudad",res.data.Ciudad);
          todoStorage.put("ciudad",res.data.Ciudad);

          console.log(this.getState("siniestro"));

          $location.path("customerData");
        }
      }

      let errorCallBack = (err) => {
         this.setState("isFetching",false);
      }

      httpRequest.post("/getUserWarrantyStatus",data,successCallBack,errorCallBack);

    }

    this.signOut = () => {
      event.preventDefault();
      Swal.fire({
        title: decode_utf8('¿Seguro?'),
        text: decode_utf8("¡Tendras que volver a realizar todo el proceso!"),
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        cancelButtonText: 'No',
        confirmButtonText: decode_utf8('Si voy a cerrar sesión')
      }).then((result) => {
        if (result.value) {
          localStorage.clear();
          $window.location.assign('#/');
          location.reload();
        }
      });
    }

    this.checkCustomerStatus = (identification) => {

      if(this.getState('isFetching'))
      {
        return null;
      }



      this.setState("isFetching",true);

      let successCallBack = (res) => {
        this.setState("isFetching",false);
        if(res.status == "200")
        {
          Swal.fire({
            title: 'Ok',
            text: decode_utf8("¡Tenemos tus datos en nuestro sistema! asi que"),
            type: 'success',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            cancelButtonText: 'Continua a garantia',
            confirmButtonText: 'Actualiza o verifica tus datos personales'
          }).then((result) => {
            if (result.value) {
              console.log("good andswer");

              todoStorage.delete("customerInfo");
              this.setState("customerInfo",null);

              this.setState("cliente",res.data);
              todoStorage.put("cliente",res.data);
              this.setState("isFetching",true);
              $window.location.assign('#/customerRegister');

              //$location.path("customerRegister");
            }
            else{

              let cliente = this.getState("cliente");

              this.setState("customerInfo",res.data.identificacion);
              todoStorage.put("customerInfo",res.data.identificacion);

              todoStorage.delete('cliente');
              this.setState("cliente",null);

              console.log("Continuar a autorizaciones");
              $window.location.assign('#/warrantyProccess');
            }
          })
        }

        if(res.status == "204")
        {
          todoStorage.delete('cliente');
          this.setState("cliente",null);

          todoStorage.delete('customerInfo');
          this.setState("customerInfo",null);

          Swal.fire(
            'Listo',
            decode_utf8('¡Es hora de ingresar tus datos!'),
            'warning'
          );
          $location.path("customerRegister");
        }

      }
      let errorCallBack = (err) => {
        this.setState("isFetching",false);
      }
      httpRequest.get("/getCustomerByCode/"+identification,successCallBack,errorCallBack);
    }

    this.cancelFetching = () => {
        this.setState("isFetching",false);
    }

    this.loadUserForm = () => {
      if(this.getState('departamentos') == null)
      {
        let successCallBack = res => {
          //console.log(res.data);
          this.setState("departamentos",res.data);
          this.setState("isFetching",false);
        }

        let errorCallBack = (err) => {
          this.setState("isFetching",false);
        }

        httpRequest.get("/getDepartments",successCallBack,errorCallBack);
      }
      else{
        let self = this;
        $timeout(function(){self.setState("isFetching",false);}, 2000);
      }
    }

    this.findCities = (selectedDeparment) => {

      this.setState("isFetching",true);

      let successCallBack = res => {
        //console.log(res.data);
        this.setState("ciudades",res.data);
        this.setState("isFetching",false);

        $('#citiesSelect').select2({
					placeholder: "Seleccione la ciudad",
					allowClear: true
				});
      }

      let errorCallBack = (err) => {
        this.setState("isFetching",false);
      }

      httpRequest.get("/getCitiesByDeparment/"+selectedDeparment,successCallBack,errorCallBack);
    }

    this.persistCustomer = (customerData) => {

      //let customerData = this.getState('cliente');

      if(this.getState('isFetching'))
      {
        return null;
      }

      let selectedCity = customerData.ciudad;

      customerData.ciudad = selectedCity.codigo;
      console.log("action to create or update customer");
      console.log(customerData);

      this.setState("isFetching",true);


      let successCallBack = (res) => {

        todoStorage.delete('cliente');
        this.setState("customerInfo",customerData.identificacion);
        todoStorage.put("customerInfo",customerData.identificacion);

        Swal.fire(
          'Bien',
          decode_utf8(res.data.message),
          'success'
        );
        this.setState("isFetching",false);
        $location.path("warrantyProccess");

      }

      let errorCallBack = (err) => {

        customerData.ciudad = selectedCity;
        this.setState("isFetching",false);

      }

      httpRequest.post("/persistCustomer",customerData,successCallBack,errorCallBack);
      
    }

    this.getFranchises = () => {
      console.log("in get franchises");
      console.log(this.getState('cita'));

      let successCallBack = res => {
        //console.log(res.data);
        let franquicias = null;

        if(res.data)
        {
          franquicias = $filter('filter')(res.data,function(franq){
              return franq.id != 5 && franq.id != 6 && franq.id != 9 && franq.id != 10  && franq.nombre != "EFECTIVO"
          });
        }

        console.log(franquicias);

        this.setState("franquicias",franquicias);
        this.setState("isFetching",false);
      }

      let errorCallBack = (err) => {
        this.setState("isFetching",false);
      }

      let cita = this.getState('cita');

      console.log(cita);

      httpRequest.get("/getCardfranchiseByOffice/"+cita.oficina,successCallBack,errorCallBack);

    }

    this.generateCreditCardWarranty = (data) => {

      if(this.getState('isFetching'))
      {
        return null;
      }

      this.setState("isFetching",true);

      let successCallBack = res => {

        Swal.fire(
          'Bien',
          res.data.message,
          'success'
        );

        $location.path("/");

        this.setState("isFetching",false);


      }
      let errorCallBack = (err) => {
        this.setState("isFetching",false);
      }

      let siniestro = this.getState('siniestro');

      httpRequest.put("/generateCreditCardWarranty/"+siniestro.id,data,successCallBack,errorCallBack);

    }

    this.generateConsignmentCardWarranty = (data) => {

      if(this.getState('isFetching'))
      {
        return null;
      }

      this.setState("isFetching",true);

      let successCallBack = res => {
        Swal.fire(
          'Bien',
          res.data.message,
          'success'
        );

        $location.path("/");

        this.setState("isFetching",false);



      }
      let errorCallBack = (err) => {
        this.setState("isFetching",false);
      }
      let siniestro = this.getState('siniestro');

      httpRequest.put("/generateConsignmentWarranty/"+siniestro.id,data,successCallBack,errorCallBack);
    }

    this.listenTo(actions.getUserWarrantyStatus, this.getUserWarrantyStatus);
    this.listenTo(actions.signOut, this.signOut);
    this.listenTo(actions.checkCustomerStatus, this.checkCustomerStatus);
    this.listenTo(actions.cancelFetching, this.cancelFetching);
    this.listenTo(actions.loadUserForm, this.loadUserForm);
    this.listenTo(actions.findCities, this.findCities);
    this.listenTo(actions.persistCustomer, this.persistCustomer );
    this.listenTo(actions.getFranchises, this.getFranchises );
    this.listenTo(actions.generateCreditCardWarranty, this.generateCreditCardWarranty );
    this.listenTo(actions.generateConsignmentCardWarranty, this.generateConsignmentCardWarranty );
  });

});


function manageUserWarrantyInfo(res)
{
  //console.log(res);

  if(res.status == 204){

    Swal.fire({
      type: 'error',
      title: 'Oops...',
      text: decode_utf8('¡Parece que no tenemos tu información!'),
    });

    return;
  }

  if(res.data.Siniestro.estado)
  {
    //console.log(res.data.estado);
    switch(parseInt(res.data.Siniestro.estado))
    {
      case 1:
        Swal.fire({
          type: 'warning',
          title: 'Oops...',
          text: decode_utf8("La placa "+res.data.Siniestro.placa+" se encuentra en estado NO ADJUDICADO"),
        });
        break;
      case 3:

        console.log(res.data.Autorizaciones);

        if(res.data.Autorizaciones != null)
        {
            Swal.fire({
              type: 'success',
              title: 'Hurra',
              text: decode_utf8('¡Hemos encontrado la información de tu garantía!'),
            });
        }
        else{
            Swal.fire({
              type: 'success',
              title: 'Bien',
              text: decode_utf8('¡Procede a ingresar la información de tu garantía!'),
            });
        }

        return true;
      case 5:
        Swal.fire({
          type: 'warning',
          title: 'Oops...',
          text: decode_utf8("La placa "+res.data.Siniestro.placa+" se encuentra en estado PENDIENTE, no le ha sido adjudicado un servicio"),
        });
        break;
      case 7:
        Swal.fire({
          type: 'warning',
          title: 'Oops...',
          text: decode_utf8("La placa "+res.data.Siniestro.placa+" se encuentra en servicio no requiere garantia"),
        });
        break;
      case 8:
        Swal.fire({
          type: 'warning',
          title: 'Oops...',
          text: decode_utf8("La placa "+res.data.Siniestro.placa+" se encuentra en servicio concluido"),
        });
        break;
      default:
        Swal.fire({
          type: 'warning',
          title: 'Oops...',
          text: decode_utf8("La placa "+res.data.Siniestro.placa+" no se encuentra disponible"),
        });
        break;
    }

    return false;

  }

}
