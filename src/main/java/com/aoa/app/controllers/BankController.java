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
@Api(value="Servicio nombre de bancos plataforma aoa", description="Operaciones con los bancos")

public class BankController {
	
	private final SQLMANAGER manager;
	
	public BankController() {
		this.manager = new SQLMANAGER();
	}
	

	
	@ApiOperation(value = "Obten todas los nombres de los bancos")
	@GetMapping(value = "/getBanks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	  public ResponseEntity<?> getBanks() throws SQLException
	  {
		System.out.println("get Banks");
		
		String query = "Select * from codigo_ach order by nombre";      
        List<Map<String, String>> bancos = this.manager.ExecuteSql(query).fetch_query(null).get_rows();
      
        return ((bancos == null) ? new ResponseEntity(bancos, HttpStatus.NO_CONTENT) : new ResponseEntity(bancos, HttpStatus.OK)) ;          
      	   
	  }
}