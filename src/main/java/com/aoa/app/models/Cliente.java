package com.aoa.app.models;

import java.io.Serializable;

import com.aoa.app.helpers.EntityModel;

public class Cliente extends EntityModel implements Serializable{
	
	public Cliente()
    {
        setTable("cliente");
    }
	
	public String nombre;
	public String apellido;
    public String tipo_id;
    public String lugar_expdoc;
    public String pais;
    public String ciudad;
    public String direccion;
    public String barrio;
    public String celular;
    public String email_e;
    public String sexo;
    public String identificacion;
    public String telefono_oficina;
    public String direccion_oficina;

	
	public String getNombre() {
		return nombre;
	}
	
	public String getApellido() {
		return apellido;
	}
	
	public String getTipo_id() {
		return tipo_id;
	}
	
	public String getLugar_expdoc() {
		return lugar_expdoc;
	}
	
	public String getPais() {
		return pais;
	}
	
	public String getCiudad() {
		return ciudad;
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public String getBarrio() {
		return barrio;
	}	
	
	
	public String getCelular() {
		return celular;
	}
	
	public String getEmail_e() {
		return email_e;
	}
	
	
	public String getSexo() {
		return sexo;
	}
	
	public String getIdentificacion() {
		return identificacion;
	}
	
	public String getTelefono_oficina() {
		return telefono_oficina;
	}
	
	public String getDireccion_oficina() {
		return direccion_oficina;
	}
	
}
