package com.example.diegollusco.hotel.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.diegollusco.hotel.R;
import com.example.diegollusco.hotel.clases.ClsPrefer;
import com.example.diegollusco.hotel.clases.ClsProducto;
import com.example.diegollusco.hotel.clases.ClsRutas;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Diego Llusco on 13/09/2016.
 */
public class AdapterProducto extends ArrayAdapter<ClsProducto>{

    private Context context;
    private ArrayList <ClsProducto>datos;

    public AdapterProducto(Context context, ArrayList datos){
        super(context, R.layout.itemproducto, datos);

        this.context = context;
        this.datos = datos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.itemproducto, null);
        //TITULO/NOMBRE
        TextView titulo = (TextView)item.findViewById(R.id.txt_producto);
        titulo.setText(datos.get(position).getNOMBRE());
        //DESCRIPCION
        TextView descripcion = (TextView)item.findViewById(R.id.txt_descripcion);
        descripcion.setText(datos.get(position).getDESCRIPCION());
        //PRECIO
        TextView precio = (TextView)item.findViewById(R.id.txt_precio);
        precio.setText("Precio: Bs. " + datos.get(position).getPRECIO());
        //IMAGEN
        ImageView img = (ImageView)item.findViewById(R.id.img_producto);
        ClsRutas cR = new ClsRutas();
        Picasso.with(context).load(cR.RutaURL()+datos.get(position).getIMAGEN()).into(img);
        //CARRITO
        ImageButton imgbtn = (ImageButton)item.findViewById(R.id.img_btn);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClsPrefer cP = new ClsPrefer();
                HashMap<String, String> params = new HashMap<>();
                params.put("NRO_HABITACION", cP.getNRO_HABITACION(context));
                params.put("USUARIO", cP.getUSUARIO(context));
                params.put("ID_PRODUCTO", datos.get(position).getID_PRODUCTO()+"");

                AddWork(params);
            }
        });
        return item;
    }

    private void AddWork (final HashMap<String, String> params){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ClsRutas rC = new ClsRutas();
        String URL = rC.CarritoURL();
        JsonObjectRequest request = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getJSONObject("status").getInt("code")==200){
                                String respuesta = response.getJSONObject("status").get("desc").toString();
                                Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                            } else{
                                String respuesta = response.getJSONObject("status").get("desc").toString();
                                Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}
