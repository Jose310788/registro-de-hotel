package com.example.diegollusco.hotel.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.diegollusco.hotel.clases.ClsPrefer;
import com.example.diegollusco.hotel.clases.ClsProducto;
import com.example.diegollusco.hotel.clases.ClsRutas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int CountBack=0;
    private Timer timer;
    private ArrayList<ClsProducto> Producto0;
    private AdapterProducto adapter0;
    private ListView lv_lista0;
    private SwipeRefreshLayout swipeRefreshLayout0;
    private TextView vacio, serv;
    private HashMap<String, String> params;

    private String id_serv;

    private ClsRutas rC = new ClsRutas();
    private static String URL_ = "";

    private static RequestQueue requestQueue;
    private JsonObjectRequest request;

    private ClsPrefer PC = new ClsPrefer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //INSTANCIAR
        swipeRefreshLayout0 = (SwipeRefreshLayout)findViewById(R.id.Refresh_0);
        swipeRefreshLayout0.setColorSchemeResources(R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        lv_lista0 = (ListView)findViewById(R.id.lv_producto);
        vacio = (TextView)findViewById(R.id.vacio_main);
        serv = (TextView)findViewById(R.id.txt_serv);

        //INSTANCIAR

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                params = new HashMap<String, String>();
                params.put("NRO_HABITACION", PC.getNRO_HABITACION(MainActivity.this));
                params.put("USUARIO", PC.getUSUARIO(MainActivity.this));

                GetCarritoAbierto(params);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*----------------------------------------------------------------------*/

        swipeRefreshLayout0.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vacio.setVisibility(View.INVISIBLE);
                params = new HashMap<>();
                params.put("ID_SERVICIO", id_serv);
                listarProductos(params);
            }
        });

        /*----------------------------------------------------------------------*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.desayuno) {
            id_serv="1";
            serv.setText("DESAYUNO");
            swipeRefreshLayout0.setVisibility(View.VISIBLE);
            swipeRefreshLayout0.setRefreshing(true);
            vacio.setVisibility(View.INVISIBLE);
            params = new HashMap<>();
            params.put("ID_SERVICIO", id_serv);
            listarProductos(params);
        } else if (id == R.id.almuerzo) {
            id_serv="2";
            serv.setText("ALMUERZO");
            swipeRefreshLayout0.setVisibility(View.VISIBLE);
            swipeRefreshLayout0.setRefreshing(true);
            vacio.setVisibility(View.INVISIBLE);
            params = new HashMap<>();
            params.put("ID_SERVICIO", id_serv);
            listarProductos(params);
        } else if (id == R.id.snack) {
            id_serv="3";
            serv.setText("SNACK");
            swipeRefreshLayout0.setVisibility(View.VISIBLE);
            swipeRefreshLayout0.setRefreshing(true);
            vacio.setVisibility(View.INVISIBLE);
            params = new HashMap<>();
            params.put("ID_SERVICIO", id_serv);
            listarProductos(params);
        } else if (id == R.id.salir) {
            PC.setNRO_HABITACION(MainActivity.this, "null");
            PC.setUSUARIO(MainActivity.this, "null");
            Intent intent = new Intent(MainActivity.this, LogeoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void GetCarritoAbierto (final HashMap<String, String> params){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        URL_=rC.AbiertoURL();
        request = new JsonObjectRequest(URL_, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String id;
                        String estado;
                        try {
                            if(response.getJSONObject("status").getInt("code")==200){
                                id = response.getJSONObject("status").get("id_carrito").toString();
                                estado = response.getJSONObject("status").get("estado").toString();

                                Intent i = new Intent(MainActivity.this,DetalleCarritoActivity.class);
                                i.putExtra("ID_CARRITO", id);
                                i.putExtra("ESTADO", estado);
                                MainActivity.this.startActivity(i);
                            }else{
                                String mensaje = response.getJSONObject("status").get("desc").toString();
                                Toast.makeText(getApplication(), mensaje, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplication(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    private void listarProductos (final HashMap<String, String> params){
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        URL_=rC.ServiciosURL();
        request = new JsonObjectRequest(URL_, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject Noti;
                        int NotiLeng;

                        int ID_PRODUCTO;
                        String NOMBRE;
                        String DESCRIPCION;
                        int PRECIO;
                        int ESTADO;
                        String IMAGEN;
                        Producto0 = new ArrayList<>();
                        try {
                            NotiLeng = response.getJSONArray("content").length();
                            if(NotiLeng==0){
                                vacio.setVisibility(View.VISIBLE);
                                vacio.setText("No Existe Elementos");
                                swipeRefreshLayout0.setVisibility(View.GONE);
                            }else{
                                vacio.setVisibility(View.GONE);
                            }
                            for(int i=0;i<NotiLeng;i++){
                                Noti = (JSONObject) response.getJSONArray("content").get(i);
                                ID_PRODUCTO = Noti.getInt("ID_PRODUCTO");
                                NOMBRE = Noti.getString("NOMBRE");
                                DESCRIPCION = Noti.getString("DESCRIPCION");
                                PRECIO = Noti.getInt("PRECIO");
                                ESTADO = Noti.getInt("ESTADO");
                                IMAGEN = Noti.getString("IMAGEN");
                                Producto0.add(new ClsProducto(0,0, ID_PRODUCTO,NOMBRE,DESCRIPCION,PRECIO,ESTADO,IMAGEN));
                            }
                            swipeRefreshLayout0.setRefreshing(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout0.setRefreshing(false);
                            Toast.makeText(getApplication(), "Algo salío mal\nRecargue la pagina", Toast.LENGTH_SHORT).show();
                        }
                        adapter0 = new AdapterProducto(MainActivity.this, Producto0);
                        lv_lista0.setAdapter(adapter0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout0.setRefreshing(false);
                Toast.makeText(getApplication(), "Algo salío mal\nRecargue la pagina", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(CountBack==0){
                CountBack=1;
                Toast.makeText(this, "Vuelva a pulsar la tecla Atrás para cerrar la aplicación", Toast.LENGTH_SHORT).show();
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        CountBack=0;
                    }
                };
                timer = new Timer();
                timer.schedule(task, 2000);
            }
            else{
                finish();
                timer.cancel();
            }
        }
    }
}
