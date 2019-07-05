package com.aoa.app.controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
//@RequestMapping("/aoaapi")
@Api(value="Servicio de información de ciudades plataforma aoa", description="Operaciones con las ciudades")


public class CityController {
	
	private final SQLMANAGER manager;
	
	public CityController() {
		this.manager = new SQLMANAGER();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "Obtiene todos los departamentos")	
	 
	@GetMapping(value = "/getDepartments", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getDeparments() throws SQLException
	  {
		System.out.println("get deparments");
		
		 String query = "select distinct departamento from ciudad order by departamento";
        List<Map<String, String>>  departamentos = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
        
        return ((departamentos == null) ? new ResponseEntity(departamentos, HttpStatus.NO_CONTENT) : new ResponseEntity(departamentos, HttpStatus.OK)) ;          
        	   
	  }
	
	@ApiOperation(value = "Obten todas las ciudades por departamento")
	@GetMapping(value = "/getCitiesByDeparment/{department}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getCitiesByDeparment(@PathVariable(value = "department") String department) throws SQLException
	  {
		System.out.println("get deparments");
		
		String query = "Select * from ciudad where departamento = '"+department+"' order by nombre ";      
        List<Map<String, String>> ciudades = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
      
        return ((ciudades == null) ? new ResponseEntity(ciudades, HttpStatus.NO_CONTENT) : new ResponseEntity(ciudades, HttpStatus.OK)) ;          
      	   
	  }
}
