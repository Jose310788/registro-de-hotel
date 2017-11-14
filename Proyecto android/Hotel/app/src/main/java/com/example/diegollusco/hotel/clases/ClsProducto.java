package com.example.diegollusco.hotel.clases;

/**
 * Created by Diego Llusco on 30/05/2017.
 */
public class ClsProducto {

    private int ID_CARRITO;
    private int SECUENCIA;
    private int ID_PRODUCTO;
    private String NOMBRE;
    private String DESCRIPCION;
    private int PRECIO;
    private int ESTADO;
    private String IMAGEN;

    public ClsProducto(int ID_CARRITO, int SECUENCIA, int ID_PRODUCTO, String NOMBRE, String DESCRIPCION, int PRECIO, int ESTADO, String IMAGEN) {
        this.ID_CARRITO = ID_CARRITO;
        this.SECUENCIA = SECUENCIA;
        this.ID_PRODUCTO = ID_PRODUCTO;
        this.NOMBRE = NOMBRE;
        this.DESCRIPCION = DESCRIPCION;
        this.PRECIO = PRECIO;
        this.ESTADO = ESTADO;
        this.IMAGEN = IMAGEN;
    }

    public int getID_CARRITO() {
        return ID_CARRITO;
    }

    public int getSECUENCIA() {
        return SECUENCIA;
    }

    public int getID_PRODUCTO() {
        return ID_PRODUCTO;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public String getDESCRIPCION() {
        return DESCRIPCION;
    }

    public int getPRECIO() {
        return PRECIO;
    }

    public int getESTADO() {
        return ESTADO;
    }

    public String getIMAGEN() {
        return IMAGEN;
    }
}
