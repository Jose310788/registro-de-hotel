package com.example.diegollusco.hotel.clases;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Diego Llusco on 30/05/2017.
 */
public class ClsPrefer {

    private final String Name_Prefe="Preferencias";
    private String NRO_HABITACION;
    private String USUARIO;

    public String getNRO_HABITACION(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name_Prefe, Context.MODE_PRIVATE);
        this.NRO_HABITACION = sharedPreferences.getString("NRO_HABITACION","null");
        return NRO_HABITACION;
    }

    public String getUSUARIO(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name_Prefe, Context.MODE_PRIVATE);
        this.USUARIO = sharedPreferences.getString(USUARIO,"null");
        return USUARIO;
    }

    public void setNRO_HABITACION(Context context, String x){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name_Prefe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NRO_HABITACION",x);
        editor.commit();
    }

    public void setUSUARIO(Context context, String x){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name_Prefe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USUARIO",x);
        editor.commit();
    }
}
