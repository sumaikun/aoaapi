package com.aoa.app.controllers;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.aoa.app.helpers.SQLMANAGER;
import com.aoa.app.models.Auditoria;
import java.util.Calendar;

import org.springframework.context.annotation.Bean;


public class auditController {
	

	
	public void saveAudit(Map<String,Object> data) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException
	{
		Auditoria auditoria = new Auditoria();
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int min = Calendar.getInstance().get(Calendar.MINUTE);
		int sec = Calendar.getInstance().get(Calendar.SECOND);
		
		auditoria.ano = year;
		auditoria.mes =  String.valueOf(month);
		auditoria.dia =  String.valueOf(day);
		auditoria.hora =  hour;
		auditoria.minuto =  min;
		auditoria.segundo =  sec;
		auditoria.nick = "servicio clientes aoa";
		auditoria.nombre = "servicio de clientes aoa";
		auditoria.tabla = (String) data.get("tabla");
		auditoria.accion = ((String) data.get("accion")).charAt(0);
		//auditoria.registro =  data.get("registro");
		auditoria.ip = (String) data.get("ip");
		auditoria.detalle = (String) data.get("detalle");
		
		System.out.println(auditoria.toString());
		
		//auditoria.save();
		
	}
}
