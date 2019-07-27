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
//@RequestMapping("/aoaapi")
@Api(value="Servicios de usuarios", description="Operaciones de los usuarios que tienen perfil en la base de datos aoa")



public class UserController {
	
	private final SQLMANAGER manager;
	private final auditController auditController; 
	

	
	@Autowired	
	public UserController(auditController auditController) {
		this.manager = new SQLMANAGER();
		this.auditController = auditController;
	}
	
		 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
		@ApiOperation(value = "Login Usuarios App")	
		 
		@PostMapping(value = "/login/app", consumes = "multipart/form-data", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		  public ResponseEntity<?> AppLogin(@RequestParam(value = "username") String userName,@RequestParam(value = "password") String password) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException
		  {
			System.out.println("got in NOT json ver user warranty info");
			
			Map<String, Object> responseObj = this.AppLoginResponse(userName, password);
	        
	        return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK)) ;          
	        	   
		  }
		
		 @ApiIgnore
		 @PostMapping(value = "/login/app", consumes = "application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		  public ResponseEntity<?> findServiceJson(@RequestBody Map<String, ?> input) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException
		  {
			 System.out.println("got in json ver user warranty info");		 
			 
			 Map<String, Object> responseObj = this.AppLoginResponse((String)input.get("username"),(String)input.get("password"));
		        
	         return ((responseObj == null) ? new ResponseEntity(responseObj, HttpStatus.NO_CONTENT) : new ResponseEntity(responseObj, HttpStatus.OK));		 
			   
		  }
		 
		 private Map<String, Object> AppLoginResponse(String userName, String password) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException
		 {
			
			Map<String, Object> responseObject = new HashMap<String, Object>();
			
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(password.getBytes("utf8"));
	        password = String.format("%040x", new BigInteger(1, digest.digest()));
			
			String query = "Select * from usuario_appmovil where usuario = '"+userName+"' and clave = '"+password+"'";	        
			
	        Map<String, String> User = this.manager.ExecuteSql(query).fetch_query(null).first_row();        
	        
	        if(User != null)
	        {
	        	responseObject.put("Usuario", User);	       
	        	query = "Select * from usuario_desarrollo where usuario = '"+userName+"'";			
		        Map<String, String> admin = this.manager.ExecuteSql(query).fetch_query(null).first_row();
		        if(admin != null)
		        {
		        	responseObject.put("isAdmin", true);
		        }else {
		        	responseObject.put("isAdmin", false);
		        }
		        
		        query = "Select email , celular, oficina from operario where usuario = '"+userName+"' and inactivo = 0 ";			
		        Map<String, String> usuarioFloat = this.manager.ExecuteSql(query).fetch_query(null).first_row();
		        responseObject.put("datosFlota", usuarioFloat);
		        
	        }	        
	        
	        	        
	        return ((User == null) ? null : responseObject );
	        
		}
}
