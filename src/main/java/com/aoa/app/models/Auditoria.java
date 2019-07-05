package com.aoa.app.models;

import java.io.Serializable;

import com.aoa.app.helpers.EntityModel;

public class Auditoria extends EntityModel implements Serializable {
	
	public Auditoria() {
		
		setTable("app_bitacora");
		
	}
	
	
	public int ano;
	public String mes;
	public String dia;
	public int hora;
	public int minuto;
	public int segundo;
	public String nick;
	public String nombre;
	public String tabla;
	public char accion;
	public int registro;
	public String ip;
	public String detalle;
	
	
	public int getAno() {
		return ano;
	}
	
	public String getMes() {
		return mes;
	}
	
	
	public String getDia() {
		return dia;
	}
	
	public int getHora() {
		return hora;
	}
	
	public int getMinuto() {
		return minuto;
	}
	
	public int getSegundo() {
		return segundo;
	}
	
	public String getNick() {
		return nick;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getTabla() {
		return tabla;
	}
	
	public char getAccion() {
		return accion;
	}
	
	public int getRegistro() {
		return registro;
	}
	
	public String getIp() {
		return ip;
	}
	
	public String getDetalle() {
		return detalle;
	}
	
	
	
}
