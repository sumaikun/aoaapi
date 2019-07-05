package com.aoa.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import com.aoa.app.config.Swagger2Config;
import com.aoa.app.controllers.auditController;



@SpringBootApplication
@Import(Swagger2Config.class)
public class Application {
	
	

	
	@Bean
    public auditController auditController() {
        return new auditController();
    }
	
	
	public static void main(String[] args) {
		System.out.println("Got in app main");
		SpringApplication.run(Application.class, args);
	}
	
	


}
