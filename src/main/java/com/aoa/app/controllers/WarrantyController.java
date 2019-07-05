package com.aoa.app.controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.aoa.app.helpers.SQLMANAGER;
import com.aoa.app.models.Autorizacion;
import com.aoa.app.models.Cliente;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
//@RequestMapping("/aoaapi")
@Api(value="Procesos de garantia en siniestros", description="Operaciones de garantia")


public class WarrantyController {
	
	private final SQLMANAGER manager;
		
	public WarrantyController() {
		this.manager = new SQLMANAGER();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "Verifica las franquicias por la ciudad de la cita")	
	 
	@GetMapping(value = "/getCardfranchiseByOffice/{office}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> findService(@PathVariable(value = "office") String office) throws SQLException
	  {
		
		String query = "select * from ciudad_franq where oficina = '"+office+"'";
		
        List<Map<String, String>>  ciu_franquicias = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
        
        String franqs = "";
        
        for (Map<String, String> item : ciu_franquicias) {
            if(item.get("franquicia") != null)
            {
                franqs += item.get("franquicia")+",";
            }            
        }
        franqs = franqs.substring(0, franqs.length()-1);
        query = "Select * from franquisia_tarjeta where id in ("+franqs+")";
        List<Map<String, String>>  franquicias = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
		
        return ((franquicias == null) ? new ResponseEntity(franquicias, HttpStatus.NO_CONTENT) : new ResponseEntity(franquicias, HttpStatus.OK)) ;          
        	   
	  }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "Generar garantia por tarjeta de credito")	
	@PutMapping(value = "/generateCreditCardWarranty/{siniestro}", consumes = "multipart/form-data" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> generateCreditWarranty(@PathVariable(value = "siniestro") String siniestro,
			  @RequestPart(value = "identificacionCliente") String identificacionCliente,
			  @RequestPart(value = "numero_tarjeta") String numero_tarjeta,
			  @RequestPart(value = "franquicia") String franquicia,
			  @RequestPart(value = "banco") String banco,
			  @RequestPart(value = "month_expi") String month_expi,
			  @RequestPart(value = "year_expi") String year_expi,
			  @RequestPart(value = "cvv") String cvv,
			  @RequestPart(value = "devol_tipo_cuenta") String devol_tipo_cuenta,
			  @RequestPart(value = "devol_cuenta_bancaria") String devol_cuenta_bancaria,
			  @RequestPart(value = "devol_banco") String devol_banco,
			  @RequestPart(value = "devol_nombre_titular") String devol_nombre_titular,
			  @RequestPart(value = "devol_iden_titular") String devol_iden_titular,
			  @RequestPart(value = "valor_garantia") String valor_garantia) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	  {		 
		Map<String, Object> responseObj = this.generateCreditResponse(siniestro, identificacionCliente , numero_tarjeta, franquicia, banco, month_expi, year_expi, cvv, devol_tipo_cuenta, devol_cuenta_bancaria,
				devol_banco, devol_nombre_titular, devol_iden_titular, valor_garantia);
      
      return ((responseObj == null) ? new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.OK));		 
	   
	  }
	
	@ApiIgnore
	@PutMapping(value = "/generateCreditCardWarranty/{siniestro}", consumes = "application/json" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> generateCreditWarranty(@PathVariable(value = "siniestro") String siniestro,@RequestBody Map<String, ?> input) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	  {		 
		Map<String, Object> responseObj = this.generateCreditResponse(siniestro, String.valueOf(input.get("identificacionCliente")) , String.valueOf(input.get("numero_tarjeta")), String.valueOf(input.get("franquicia")) , String.valueOf(input.get("banco")), String.valueOf(input.get("month_expi")), String.valueOf(input.get("year_expi")), String.valueOf(input.get("cvv")), String.valueOf(input.get("devol_tipo_cuenta")), String.valueOf(input.get("devol_cuenta_bancaria")),
				String.valueOf(input.get("devol_banco")), String.valueOf(input.get("devol_nombre_titular")), String.valueOf(input.get("devol_iden_titular")), String.valueOf(input.get("valor_garantia")));
    
		return ((responseObj == null) ? new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.OK));		 
	   
	  }
	
	
	 private Map<String, Object> generateCreditResponse(final String siniestro, final String identificacionCliente, final String numero_tarjeta,
				final String franquicia, final String banco, final String month_expi, final String year_expi, final String cvv, final String devol_tipo_cuenta, final String devol_cuenta_bancaria,final String devol_banco,
				final String devol_nombre_titular, final String devol_iden_titular, final String valor_garantia
				) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	 {
		 //auditController auditController = new auditController();
		 
		 
		 Map<String, Object> responseObject = new HashMap<String, Object>();
		 
		 Autorizacion autorizacion = new Autorizacion();
			
		 String query = "Select * from sin_autor where siniestro = '"+siniestro+"'  and estado != 'R'";
		 
		 Map<String, String>  sin_auto = this.manager.ExecuteSql(query).fetch_query(null).first_row();
        
         Timestamp currentTime = new Timestamp(System.currentTimeMillis());	
         String now = currentTime.toString();
         
     	 query = "Select * from cliente where identificacion = '"+identificacionCliente+"' ";
		 
		 Map<String, String>  Usuario = this.manager.ExecuteSql(query).fetch_query(null).first_row();
         
         if(sin_auto == null)
         {               
             autorizacion.siniestro = siniestro;
             autorizacion.nombre = Usuario.get("nombre")+" "+Usuario.get("apellido");
             autorizacion.identificacion = Usuario.get("identificacion");          
             
             autorizacion.numero = numero_tarjeta;
             autorizacion.franquicia = franquicia;
             autorizacion.banco = banco;
             autorizacion.vencimiento_mes =  month_expi;
             autorizacion.vencimiento_ano = year_expi;
             autorizacion.codigo_seguridad = cvv;
             autorizacion.solicitado_por = "Pagina web version 2";
            
             
             autorizacion.fecha_solicitud = now;                 
             autorizacion.solicitado_por = "Pagina web version 2";
             autorizacion.estado = "E";
             autorizacion.email = Usuario.get("email_e");
             autorizacion.devol_tipo_cuenta = devol_tipo_cuenta;
             autorizacion.devol_cuenta_banco = devol_cuenta_bancaria;
             autorizacion.devol_banco = devol_banco;
             autorizacion.devol_ncuenta = devol_nombre_titular;
             autorizacion.identificacion_devol = devol_iden_titular;
             autorizacion.formulario_web = "2"; 
             autorizacion.valor = valor_garantia;               
             autorizacion.save();
             
             query = "Select * from sin_autor where siniestro = '"+siniestro+"'  and estado != 'R'";    		 
    		 sin_auto = this.manager.ExecuteSql(query).fetch_query(null).first_row();
             responseObject.put("id",sin_auto.get("id"));
             responseObject.put("message","Garantía por tarjeta de credito registrada");
            
         }
         else
         {
             autorizacion.map_to_object(sin_auto);
             autorizacion.setId(Integer.parseInt(sin_auto.get("id")));
             autorizacion.fecha_solicitud = now; 
             
             autorizacion.numero = numero_tarjeta;
             autorizacion.franquicia = franquicia;
             autorizacion.banco = banco;
             autorizacion.vencimiento_mes =  month_expi;
             autorizacion.vencimiento_ano = year_expi;
             autorizacion.codigo_seguridad = cvv;
             autorizacion.solicitado_por = "Pagina web version 2";


             autorizacion.devol_tipo_cuenta = devol_tipo_cuenta;
             autorizacion.devol_cuenta_banco = devol_cuenta_bancaria;
             autorizacion.devol_banco = devol_banco;
             autorizacion.devol_ncuenta = devol_nombre_titular;
             autorizacion.identificacion_devol = devol_iden_titular;
             autorizacion.valor = valor_garantia;
             autorizacion.update();
             responseObject.put("id",sin_auto.get("id"));
             responseObject.put("message","Garantía por tarjeta de credito actualizada");
             
         }
		return responseObject;
	 }	 
	
	@ApiOperation(value = "Generar garantia por consignacion")	
	@PutMapping(value = "/generateConsignmentWarranty/{siniestro}", consumes = "multipart/form-data" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> generateConsignmentWarranty(@PathVariable(value = "siniestro") String siniestro,
			  @RequestPart(value = "identificacionCliente") String identificacionCliente,
			  @RequestPart(value = "fecha_consignacion") String fecha_consignacion,
			  @RequestPart(value = "numero_consignacion") String numero_consignacion,
			  @RequestPart(value = "devol_tipo_cuenta") String devol_tipo_cuenta,
			  @RequestPart(value = "devol_cuenta_bancaria") String devol_cuenta_bancaria,
			  @RequestPart(value = "devol_banco") String devol_banco,
			  @RequestPart(value = "devol_nombre_titular") String devol_nombre_titular,
			  @RequestPart(value = "devol_iden_titular") String devol_iden_titular,
			  @RequestPart(value = "valor_garantia") String valor_garantia) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	  {		 
		Map<String, Object> responseObj = this.generateConsignmentResponse(siniestro, identificacionCliente , fecha_consignacion, numero_consignacion, devol_tipo_cuenta, devol_cuenta_bancaria,
				devol_banco, devol_nombre_titular, devol_iden_titular, valor_garantia);
      
      return ((responseObj == null) ? new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.OK));		 
	   
	  }
	
	@ApiIgnore
	@PutMapping(value = "/generateConsignmentWarranty/{siniestro}", consumes = "application/json" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> generateConsignmentWarranty(@PathVariable(value = "siniestro") String siniestro,@RequestBody Map<String, ?> input) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	  {		 
		Map<String, Object> responseObj = this.generateConsignmentResponse(siniestro,String.valueOf(input.get("identificacionCliente")) ,String.valueOf(input.get("fecha_consignacion")), String.valueOf(input.get("numero_consignacion")) , String.valueOf(input.get("devol_tipo_cuenta")), String.valueOf(input.get("devol_cuenta_bancaria")),
				String.valueOf(input.get("devol_banco")),String.valueOf(input.get("devol_nombre_titular")),String.valueOf(input.get("devol_iden_titular")),String.valueOf(input.get("valor_garantia")));
      
      return ((responseObj == null) ? new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity<Map<String, Object>>(responseObj, HttpStatus.OK));		 
	   
	  }
	
	 private Map<String, Object> generateConsignmentResponse(final String siniestro, final String identificacionCliente, final String fecha_consignacion,
			final String numero_consignacion, final String devol_tipo_cuenta, final String devol_cuenta_bancaria,final String devol_banco,
			final String devol_nombre_titular, final String devol_iden_titular, final String valor_garantia
			) throws SQLException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	 {
		 
		 Map<String, Object> responseObject = new HashMap<String, Object>();
		 
		 Autorizacion autorizacion = new Autorizacion();
			
		 String query = "Select * from sin_autor where siniestro = '"+siniestro+"'  and estado != 'R'";
		 
		 Map<String, String>  sin_auto = this.manager.ExecuteSql(query).fetch_query(null).first_row();
        
         Timestamp currentTime = new Timestamp(System.currentTimeMillis());	
         String now = currentTime.toString();
         
     	 query = "Select * from cliente where identificacion = '"+identificacionCliente+"' ";
		 
		 Map<String, String>  Usuario = this.manager.ExecuteSql(query).fetch_query(null).first_row();
         
         if(sin_auto == null)
         {               
             autorizacion.siniestro = siniestro;
             autorizacion.nombre = Usuario.get("nombre")+" "+Usuario.get("apellido");
             autorizacion.identificacion = Usuario.get("identificacion");          
             autorizacion.fecha_consignacion = fecha_consignacion;
             autorizacion.numero_consignacion = numero_consignacion;
             autorizacion.franquicia = "6";
             autorizacion.fecha_solicitud = now;                
             //toca para evitar warning en bd
             autorizacion.vencimiento_ano = null;
             
             autorizacion.solicitado_por = "Pagina web version 2";
             autorizacion.estado = "E";
             autorizacion.email = Usuario.get("email_e");
             autorizacion.devol_tipo_cuenta = devol_tipo_cuenta;
             autorizacion.devol_cuenta_banco = devol_cuenta_bancaria;
             autorizacion.devol_banco = devol_banco;
             autorizacion.devol_ncuenta = devol_nombre_titular;
             autorizacion.identificacion_devol = devol_iden_titular;
             autorizacion.formulario_web = "2"; 
             autorizacion.valor = valor_garantia;               
             autorizacion.save();
             
             query = "Select * from sin_autor where siniestro = '"+siniestro+"'  and estado != 'R'";    		 
    		 sin_auto = this.manager.ExecuteSql(query).fetch_query(null).first_row();
             responseObject.put("id",sin_auto.get("id"));
             responseObject.put("message","Garantía por tarjeta de credito registrada");
            
         }
         else
         {
             autorizacion.map_to_object(sin_auto);
             autorizacion.setId(Integer.parseInt(sin_auto.get("id")));
             autorizacion.fecha_solicitud = now; 
             autorizacion.fecha_consignacion = fecha_consignacion;
             autorizacion.numero_consignacion = numero_consignacion;
             
             //evitar warning en bd
             autorizacion.vencimiento_ano = null;
             
             autorizacion.franquicia = "6";
             autorizacion.devol_tipo_cuenta = devol_tipo_cuenta;
             autorizacion.devol_cuenta_banco = devol_cuenta_bancaria;
             autorizacion.devol_banco = devol_banco;
             autorizacion.devol_ncuenta = devol_nombre_titular;
             autorizacion.identificacion_devol = devol_iden_titular;
             autorizacion.valor = valor_garantia;
             autorizacion.update();
             responseObject.put("id",sin_auto.get("id"));
             responseObject.put("message","Garantía por tarjeta de credito actualizada");
             
         }
		return responseObject;
	 }
	 
	 
	 
	
	 
	 
}
