package com.aoa.app.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.aoa.app.helpers.SQLMANAGER;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController

@Api(value="Servicios de citas", description="Operaciones para saber las citas del sistema")

public class AppointmentsController {
	
	private final SQLMANAGER manager;
	private final auditController auditController; 
	
	@Autowired	
	public AppointmentsController(auditController auditController) {
		this.manager = new SQLMANAGER();
		this.auditController = auditController;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "obtener citas en proceso de finalización para la app")	
	 
	@GetMapping(value = "/getAppPreparedAppointmentsDeliver/{office}/{date}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getAppPreparedAppointments(@PathVariable(value = "office") String office,
			  @PathVariable(value = "date") String date
			  ) throws SQLException
	  {
		 System.out.println("get appointments");
		
		 String query = "SELECT * FROM cita_servicio AS c INNER JOIN siniestro AS s ON c.siniestro = s.id \r\n" + 
		 		"INNER JOIN sin_autor AS a ON a.siniestro = s.id WHERE c.oficina = '"+office+"' and c.fecha = '"+date+"' and c.estado = 'P' and a.estado = 'A' "
		 	+ " ORDER BY s.id DESC LIMIT 1;";
		 
		 List<Map<String, String>>  appointments = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
      
		 return (( appointments == null) ? new ResponseEntity( appointments , HttpStatus.NO_CONTENT) : new ResponseEntity(appointments, HttpStatus.OK)) ;          
      	   
	  }
	
	
	@GetMapping(value = "/getAppPreparedAppointmentsDevol/{office}/{date}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getAppPreparedAppointments2(@PathVariable(value = "office") String office,
			  @PathVariable(value = "date") String date
			  ) throws SQLException
	  {
		 System.out.println("get appointments");
		
		 String query = "SELECT * FROM cita_servicio AS c INNER JOIN siniestro AS s ON c.siniestro = s.id \r\n" + 
		 		"WHERE c.oficina = '"+office+"' and c.fec_devolucion = '"+date+"' AND c.estadod = 'P' ORDER BY s.id DESC LIMIT 1";
		 
		 List<Map<String, String>>  appointments = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
    
		 return (( appointments == null) ? new ResponseEntity( appointments , HttpStatus.NO_CONTENT) : new ResponseEntity(appointments, HttpStatus.OK)) ;          
    	   
	  }
	
}
