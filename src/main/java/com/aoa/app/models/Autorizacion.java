package com.aoa.app.models;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

import com.aoa.app.helpers.EntityModel;

/**
 *
 * @author JVega
 */
public class Autorizacion extends EntityModel implements Serializable{
    
    public Autorizacion()
    {
        setTable("sin_autor");
    }

    

    public String getSiniestro() {
        return siniestro;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNumero() {
        return numero;
    }

    public String getBanco() {
        return banco;
    }

    public String getVencimiento_mes() {
        return vencimiento_mes;
    }

    public String getVencimiento_ano() {
        return vencimiento_ano;
    }

    public String getCodigo_seguridad() {
        return codigo_seguridad;
    }

    public String getFecha_consignacion() {
        return fecha_consignacion;
    }

    public String getNumero_consignacion() {
        return numero_consignacion;
    }

    public String getFranquicia() {
        return franquicia;
    }

    public String getFecha_solicitud() {
        return fecha_solicitud;
    }

    public String getSolicitado_por() {
        return solicitado_por;
    }

    public String getEstado() {
        return estado;
    }

    public String getValor() {
        return valor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getEmail() {
        return email;
    }

    public String getDevol_cuenta_banco() {
        return devol_cuenta_banco;
    }

    public String getDevol_tipo_cuenta() {
        return devol_tipo_cuenta;
    }

    public String getDevol_banco() {
        return devol_banco;
    }

    public String getDevol_ncuenta() {
        return devol_ncuenta;
    }

    public String getIdentificacion_devol() {
        return identificacion_devol;
    }

    public String getFormulario_web() {
        return formulario_web;
    }

    public String getConsignacion_f() {
        return consignacion_f;
    }

   /* public String getFecha_proceso() {
        return fecha_proceso;
    }*/
       
        //public String id;
    public String siniestro;
	public String nombre;
	public String identificacion;
	public String numero;
	public String banco;
	public String vencimiento_mes;
	public String vencimiento_ano;
	public String codigo_seguridad;
	public String fecha_consignacion;
	public String numero_consignacion;
	public String franquicia;
	public String fecha_solicitud;
	public String solicitado_por;
	public String estado;
	public String valor;
	public String observaciones;
	public String email;
	public String devol_cuenta_banco;
	public String devol_tipo_cuenta;
	public String devol_banco;
	public String devol_ncuenta;
	public String identificacion_devol;
	public String formulario_web;
	public String consignacion_f;
	//public String fecha_proceso;
}
