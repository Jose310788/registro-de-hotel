package com.example.diegollusco.hotel.clases;

/**
 * Created by Diego Llusco on 30/05/2017.
 */
public class ClsRutas {
    private String ruta = "http://hotel-vladdy.000webhostapp.com";
    //private String ruta = "http://192.168.0.97";

    public String RutaURL(){ return  ruta; }

    public String LoginURL(){ return ruta+"/Rest_Prueba/page/logeo"; }

    public String ServiciosURL(){ return ruta+"/Rest_Prueba/page/servicio"; }

    public String CarritoURL(){ return ruta+"/Rest_Prueba/page/carrito"; }

    public String DetalleURL(){ return ruta+"/Rest_Prueba/page/detallecarrito"; }//DETALLE DE UN CARRITO

    public String DeleteURL(){ return ruta+"/Rest_Prueba/page/deletedetalle"; }//DELETE UN PRODUCTO DEL CARRITO

    public String AbiertoURL(){ return ruta+"/Rest_Prueba/page/carritoabierto"; }//CARRITO ABIERTO

}
