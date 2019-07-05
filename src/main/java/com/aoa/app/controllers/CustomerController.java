package com.aoa.app.controllers;

//own libraries
import com.aoa.app.helpers.*;
import com.aoa.app.models.Cliente;

//spring and swagger
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.InvocationTargetException;
//sql
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
//java
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/aoaapi")
@Api(value="Servicio de información de siniestros", description="Operaciones de los clientes con la base de datos aoa")

public class CustomerController {
	
	private final SQLMANAGER manager;
	private final auditController auditController; 
	

	
	@Autowired	
	public CustomerController(auditController auditController) {
		this.manager = new SQLMANAGER();
		this.auditController = auditController;
	}
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "Verifica estado de la garantia del cliente")	
	 
	@PostMapping(value = "/getUserWarrantyStatus", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> findService(@RequestParam(value = "licensePlate") String licensePlate,@RequestParam(value = "phoneNumber") String phoneNumber) throws SQLException
	  {
		System.out.println("got in NOT json ver user warranty info");
		
		Map<String, Object> responseObj = this.findServiceResponse(licensePlate, phoneNumber);
        
        return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK)) ;          
        	   
	  }
	
	 @ApiIgnore
	 @PostMapping(value = "/getUserWarrantyStatus", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> findServiceJson(@RequestBody Map<String, ?> input) throws SQLException
	  {
		 System.out.println("got in json ver user warranty info");		 
		 
		 Map<String, Object> responseObj = this.findServiceResponse((String)input.get("licensePlate"),(String)input.get("phoneNumber"));
	        
         return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK));		 
		   
	  }
	 
	 
	 private Map<String, Object> findServiceResponse(String licensePlate, String phoneNumber) throws SQLException
	 {
		
		Map<String, Object> responseObject = new HashMap<String, Object>();
		
		String query = "select s.* from siniestro as s inner join cita_servicio as c on c.siniestro = s.id where s.placa = '"+licensePlate+"' and ( declarante_celular = '"+phoneNumber+"' or declarante_telefono = '"+phoneNumber+"' or declarante_tel_resid = '"+phoneNumber+"' ) and c.estado = 'P' order by id desc limit 1 "; 		
        
		
        Map<String, String> Siniestro = this.manager.ExecuteSql(query).fetch_query(null).first_row();        
        
        responseObject.put("Siniestro", Siniestro);
        
        if(Siniestro != null)
        {
        	query = "Select * from aseguradora where id = "+Siniestro.get("aseguradora");  
            
        	Map<String, String> Aseguradora = this.manager.ExecuteSql(query).fetch_query(null).first_row();
        
        	responseObject.put("Aseguradora", Aseguradora);
        	
        	query = "Select * from cita_servicio where siniestro = "+Siniestro.get("id")+" and estado = 'P'";      
            
        	Map<String, String> Cita = this.manager.ExecuteSql(query).fetch_query(null).first_row();
        	
        	responseObject.put("Cita", Cita);
        	
        	query = "select concat(departamento,' ',nombre) as nombreCiudad from ciudad where codigo='"+Siniestro.get("ciudad")+"'";
            
            Map<String, String> Ciudad = this.manager.ExecuteSql(query).fetch_query(null).first_row();        
            
            responseObject.put("Ciudad", Ciudad);
            
            query = "select * from sin_autor where siniestro= "+Siniestro.get("id");        
            
            List<Map<String, String>>  Autorizaciones = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
            
            responseObject.put("Autorizaciones", Autorizaciones);            
            
        }
        
        return ((Siniestro == null) ? null : responseObject );
        
	}
	 
	
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		@ApiOperation(value = "Buscar cliente por identificación")	
		 
		@GetMapping(value = "/getCustomerByCode/{identification}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		  public ResponseEntity<?> customerByCode(@PathVariable(value = "identification") String identification ) throws SQLException
		  {
			System.out.println("get customer by code");
			
			String query = "Select * from cliente where identificacion = "+identification;      
	        Map<String, String> Usuario = this.manager.ExecuteSql(query).fetch_query(null).first_row();
	        
	        return ((Usuario == null) ? new ResponseEntity(Usuario, HttpStatus.NO_CONTENT) : new ResponseEntity(Usuario, HttpStatus.OK)) ;          
	        	   
		  }
	 
	 
	 
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		@ApiOperation(value = "Buscar cliente por identificación")	
		 
		@PostMapping(value = "/persistCustomer", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		  public ResponseEntity<?> persistCustomer(@RequestPart(value = "nombre") String nombre,@RequestPart(value = "apellido") String apellido,
				  @RequestPart(value = "tipo_id") String tipo_id,
				  @RequestPart(value = "lugar_expdoc") String lugar_expdoc,
				  @RequestPart(value = "identificacion") String identificacion,
				  @RequestPart(value = "ciudad") String ciudad,
				  @RequestPart(value = "direccion") String direccion,
				  @RequestPart(value = "barrio") String barrio,
				  @RequestPart(value = "celular") String celular,
				  @RequestPart(value = "email_e") String email_e,
				  @RequestPart(value = "sexo") String sexo,
				  @RequestParam(value = "id", required=false)  Long id,
				  @RequestParam(value = "telefono_oficina", required=false)  String telefono_oficina,
				  @RequestParam(value = "direccion_oficina", required=false)  String direccion_oficina 
				  ) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
		  {
			
		  Map<String, Object> responseObj = this.persistCustomerResponse(nombre,apellido,tipo_id,lugar_expdoc,identificacion,ciudad,direccion,barrio,celular,email_e,sexo,id,telefono_oficina,direccion_oficina);	     
		 
		  return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK));
		 	
		 
    }
	 
	 @ApiIgnore
	 @PostMapping(value = "/persistCustomer", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> persistCustomerJson(@RequestBody Map<String, ?> input) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	  {
		 System.out.println("got in json ver user warranty info");
		 
		 System.out.println(input.get("id"));
		 
		 //System.out.println((Long) input.get("id"));
		 
		 Long id = null;
		 
		 if(input.get("id") != null)
		 {
			 id = Long.valueOf((String)input.get("id"));
		 }
		 
		 
		 
		 Map<String, Object> responseObj = this.persistCustomerResponse(
				 String.valueOf(input.get("nombre")),
				 (String)input.get("apellido"),
				 (String)input.get("tipo_id"),
				 (String)input.get("lugar_expdoc"),
				 String.valueOf(input.get("identificacion")),
				 String.valueOf(input.get("ciudad")),
				 (String)input.get("direccion"),
				 (String)input.get("barrio"),
				 String.valueOf(input.get("celular")),
				 (String)input.get("email_e"),
				 (String)input.get("sexo"),
				 id,
				 (String)input.get("direccion_oficina"),
				 String.valueOf(input.get("telefono_oficina"))
	     );
	        
         return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK));		 
		   
	  }
	 
	 
	 private Map<String, Object> persistCustomerResponse(String nombre, String apellido, String tipo_id, String lugar_expdoc, String identificacion,
			 String ciudad, String direccion, String barrio, String celular, String email_e, String sexo, Long id,
			 String direccion_oficina, String telefono_oficina) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	 {
		 
		
		
		
		 
		System.out.println("id corresponde a "+id);
		
		Map<String, Object> responseObject = new HashMap<String, Object>();
		
		Cliente cliente = new Cliente();
        
        cliente.nombre = nombre.toUpperCase();
        cliente.apellido = apellido.toUpperCase();
        cliente.tipo_id = tipo_id;
        cliente.lugar_expdoc = lugar_expdoc.toUpperCase();
        cliente.pais = "CO";
        cliente.ciudad = ciudad;
        cliente.direccion = direccion.toUpperCase();
        cliente.barrio = barrio.toUpperCase();
        cliente.celular = celular;
        cliente.email_e = email_e;
        cliente.sexo = sexo;
        cliente.identificacion = identificacion;
        
        cliente.telefono_oficina = telefono_oficina;
        cliente.direccion_oficina = direccion_oficina;
        
        String query = "Select * from cliente where identificacion = "+identificacion;
        
        Map<String, String> ClienteValidation = this.manager.ExecuteSql(query).fetch_query(null).first_row();
		
           
        
		if(ClienteValidation == null) {
			cliente.save();
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            query = "Select * from cliente where identificacion = "+identificacion;  
            
            Map<String, String> createdCustomer = this.manager.ExecuteSql(query).fetch_query(null).first_row();
		
            responseObject.put("message","Usuario creado");
            responseObject.put("id",createdCustomer.get("id"));
            
            System.out.println("creación de cliente");
		}
		else {
			
			cliente.update("identificacion");
			responseObject.put("message","Usuario actualizado");
			System.out.println("edición de cliente");
			
			//Reconocer el id por la validación , pueden haber casos donde no se manda el id y hay que conocerlo		    
			 responseObject.put("id",ClienteValidation.get("id"));
		}				
		
		
		 Map<String, Object> auditData = new  HashMap<String, Object>();
		 
		 auditData.put("tabla", "cliente");
		 
		 auditData.put("accion", "A");
		 
		 auditData.put("registro", "");
		 
		 auditData.put("ip", "");
		 
		 auditData.put("detalle", "Usuario acepta terminos y condiciones para manejar datos personales"
		 		+ " desde plataforma de clientes AOA ");		 
			
		 this.auditController.saveAudit(auditData);
		
		
		return responseObject;
	 }

}
