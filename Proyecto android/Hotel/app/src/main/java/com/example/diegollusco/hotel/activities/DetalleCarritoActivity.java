package com.example.diegollusco.hotel.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.diegollusco.hotel.R;
import com.example.diegollusco.hotel.adapters.AdapterProducto;
import com.example.diegollusco.hotel.adapters.AdapterProductoCarrito;
import com.example.diegollusco.hotel.clases.ClsPrefer;
import com.example.diegollusco.hotel.clases.ClsProducto;
import com.example.diegollusco.hotel.clases.ClsRutas;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DetalleCarritoActivity extends AppCompatActivity {

    private String ID_CARRITO;
    private String ESTADO;

    private ArrayList<ClsProducto> Producto0;
    private AdapterProductoCarrito adapter0;
    private ListView lv_lista0;
    private SwipeRefreshLayout swipeRefreshLayout0;
    private Button btn;
    private TextView estado;
    private HashMap<String, String> params;
    private static String URL = "";

    private ClsRutas rC = new ClsRutas();
    private ClsPrefer PC = new ClsPrefer();

    private static RequestQueue requestQueue;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_carrito);

        swipeRefreshLayout0 = (SwipeRefreshLayout) findViewById(R.id.Refresh_DC);
        swipeRefreshLayout0.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        lv_lista0 = (ListView) findViewById(R.id.lv_DetalleProducto);
        btn = (Button) findViewById(R.id.btn_pedido);
        estado = (TextView) findViewById(R.id.txt_estado_carrito);

        ID_CARRITO = getIntent().getStringExtra("ID_CARRITO");
        ESTADO = getIntent().getStringExtra("ESTADO");



        if (ESTADO.equals("0")) {
            btn.setEnabled(true);
            estado.setVisibility(View.GONE);
        } else if (ESTADO.equals("1")) {
            btn.setEnabled(false);
            estado.setVisibility(View.VISIBLE);
            estado.setText("Sin Pagar");
        } else if (ESTADO.equals("2")) {
            btn.setEnabled(false);
            estado.setVisibility(View.VISIBLE);
            estado.setText("Pagado");
        }

        params = new HashMap<>();
        params.put("ID_CARRITO", ID_CARRITO);
        swipeRefreshLayout0.setRefreshing(true);
        listar(params);

        swipeRefreshLayout0.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                params = new HashMap<>();
                params.put("ID_CARRITO", ID_CARRITO);
                swipeRefreshLayout0.setRefreshing(true);
                listar(params);
            }
        });

        lv_lista0.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapter0.getCount() == 0){
                    DetalleCarritoActivity.this.finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void listar (final HashMap<String, String> params){
        requestQueue = Volley.newRequestQueue(DetalleCarritoActivity.this);
        URL = rC.DetalleURL();
        request = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject Noti;
                        int NotiLeng;

                        int SECUENCIA;
                        int ID_PRODUCTO;
                        String NOMBRE;
                        String DESCRIPCION;
                        int PRECIO;
                        int ESTADO;
                        String IMAGEN;
                        Producto0 = new ArrayList<>();
                        try {
                            NotiLeng = response.getJSONArray("content").length();
                            if(NotiLeng==0){//vacio
                                swipeRefreshLayout0.setVisibility(View.GONE);
                                Toast.makeText(getApplication(), "El carrito se encuentra vacio", Toast.LENGTH_SHORT).show();
                                swipeRefreshLayout0.setEnabled(false);
                                btn.setVisibility(View.GONE);
                            }else{
                                for(int i=0;i<NotiLeng;i++){
                                    Noti = (JSONObject) response.getJSONArray("content").get(i);
                                    SECUENCIA = Noti.getInt("SECUENCIA");
                                    ID_PRODUCTO = Noti.getInt("ID_PRODUCTO");
                                    NOMBRE = Noti.getString("NOMBRE");
                                    DESCRIPCION = Noti.getString("DESCRIPCION");
                                    PRECIO = Noti.getInt("PRECIO");
                                    ESTADO = 0;
                                    IMAGEN = Noti.getString("IMAGEN");
                                    Producto0.add(new ClsProducto(Integer.parseInt(ID_CARRITO), SECUENCIA, ID_PRODUCTO,NOMBRE,DESCRIPCION,PRECIO,ESTADO,IMAGEN));
                                }
                                swipeRefreshLayout0.setRefreshing(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout0.setRefreshing(false);
                            Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        adapter0 = new AdapterProductoCarrito(DetalleCarritoActivity.this, Producto0);
                        lv_lista0.setAdapter(adapter0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout0.setRefreshing(false);
                Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private static Thread myThread; //this is the code I got from the tutorial
    private static boolean ThreadsRunning;
    public void Test1(MainActivity activity)
    {
        final MainActivity fActivity = activity;
        ThreadsRunning = true;
        myThread = new Thread(){

            public void run()
            {
                if(adapter0.getCount() == 0){
                    DetalleCarritoActivity.this.finish();
                }
            }
        };
        myThread.start();
    }

}
