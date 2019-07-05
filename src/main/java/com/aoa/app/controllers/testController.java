package com.aoa.app.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aoa.app.models.People;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;


@RestController
//@RequestMapping("/aoaapi")
@Api(value="Servicios de testeo para swagger", description="Operaciones pertinentes al testeo de apps")

public class testController{ 

  @ApiOperation(value = "Ejemplo de servicio")
  @GetMapping("/people")
  public List<People> findAll() {
    
    List<People> lista= new ArrayList<People>();
    lista.add(new People("pepe","perez",25));
    lista.add(new People("juan","sanchez",35));
    lista.add(new People("ana","gomez",25));
    return lista;    
   
  }
  
}
