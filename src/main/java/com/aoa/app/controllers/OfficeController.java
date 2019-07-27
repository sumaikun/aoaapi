package com.aoa.app.controllers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aoa.app.helpers.SQLMANAGER;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController

@Api(value="Servicios de oficinas", description="Operaciones en las tablas de oficinas y asociados")

public class OfficeController
{
	
	private final SQLMANAGER manager;
	private final auditController auditController; 
	
	@Autowired	
	public OfficeController(auditController auditController) {
		this.manager = new SQLMANAGER();
		this.auditController = auditController;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "obtener oficinas")	
	 
	@GetMapping(value = "/getOffices", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getOffices() throws SQLException
	  {
		 System.out.println("get offices");
		
		 String query = "SELECT * FROM oficina where activa = 1;";
		 List<Map<String, String>>  oficinas = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
      
		 return (( oficinas == null) ? new ResponseEntity( oficinas , HttpStatus.NO_CONTENT) : new ResponseEntity(oficinas, HttpStatus.OK)) ;          
      	   
	  }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "Obtener sucursales por oficina")
	
	@GetMapping(value = "/getOffices/{branchOffice}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getBranchOffices(@PathVariable(value = "branchOffice") String branchOffice) throws SQLException
	  {
		 System.out.println("get offices");
		
		 String query = "SELECT * FROM oficina where sucursal = "+branchOffice;
		 List<Map<String, String>>  oficinas = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
    
		 return (( oficinas == null) ? new ResponseEntity( oficinas , HttpStatus.NO_CONTENT) : new ResponseEntity(oficinas, HttpStatus.OK)) ;          
    	   
	  }

}
