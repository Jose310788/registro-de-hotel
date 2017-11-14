package com.example.diegollusco.hotel.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.diegollusco.hotel.R;
import com.example.diegollusco.hotel.clases.ClsPrefer;
import com.example.diegollusco.hotel.clases.ClsRutas;
import com.example.diegollusco.hotel.clases.ConectToWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class LogeoActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ObjectAnimator progressAnimator;
    private LinearLayout form;

    private EditText nro_h, nro_doc, user, password;
    private TextInputLayout h_, d_, u_, p_;
    private TextView error1, error2, error3, error4;
    private Button login;
    private RequestQueue requestQueue;
    private JsonObjectRequest request;
    private int CountBack=0;
    private Timer timer;

    private ClsPrefer PC = new ClsPrefer();
    private static String URL = "";
    private ClsRutas rC = new ClsRutas();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeo);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        form = (LinearLayout)findViewById(R.id.formLogin);

        nro_h = (EditText)findViewById(R.id.Login_EtNro_H);
        nro_doc = (EditText)findViewById(R.id.Login_EtDoc);
        user = (EditText)findViewById(R.id.Login_EtUser);
        password = (EditText)findViewById(R.id.Login_EtPass);

        error1 = (TextView)findViewById(R.id.Login_TvError1);
        error2 = (TextView)findViewById(R.id.Login_TvError2);
        error3 = (TextView)findViewById(R.id.Login_TvError3);
        error4 = (TextView)findViewById(R.id.Login_TvError4);

        h_ = (TextInputLayout)findViewById(R.id.supportNro_H);
        d_ = (TextInputLayout)findViewById(R.id.supportDoc);
        u_ = (TextInputLayout)findViewById(R.id.supportUser);
        p_ = (TextInputLayout)findViewById(R.id.supportPass);

        login = (Button)findViewById(R.id.Login_Btn);
    }

    private boolean validar(){
        Boolean flag = true;

        //reset errors
        error1.setVisibility(View.GONE);
        error2.setVisibility(View.GONE);
        error3.setVisibility(View.GONE);
        error4.setVisibility(View.GONE);

        if(TextUtils.isEmpty(nro_h.getText())){
            flag = false;
            error1.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(nro_doc.getText())){
            flag = false;
            error2.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(user.getText())){
            flag = false;
            error3.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(password.getText())){
            flag = false;
            error4.setVisibility(View.VISIBLE);
        }

        return flag;
    }

    private void setEnable(int x){
        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
        progressAnimator.setDuration(10000);
        progressAnimator.setInterpolator(new LinearInterpolator());

        //0 es para volver visible el progresBar y volver invisible linearLayout
        //1 es para volver invisible el progresBar y volver visible linearLayout
        switch (x){
            case 0:
                progressBar.setVisibility(View.VISIBLE);
                progressAnimator.start();
                form.setVisibility(View.GONE);
                break;

            case 1:
                progressBar.setVisibility(View.GONE);
                progressAnimator.cancel();
                form.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void OnClickLogin(View view) {
        ConectToWeb CtW = new ConectToWeb();
        if(CtW.isOnline(LogeoActivity.this)) {
            if(validar()){
                HashMap<String, String> params;
                URL = rC.LoginURL();
                params = new HashMap<>();
                params.put("NRO_HABITACION", nro_h.getText().toString());
                params.put("NRO_DOCUMENTO", nro_doc.getText().toString());
                params.put("USUARIO", user.getText().toString());
                params.put("PASSWORD", password.getText().toString());

                setEnable(0);

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(password.getWindowToken(), 0);

                LoginWork(params);
            }
        }
    }

    private void LoginWork (final HashMap<String, String> params){
        requestQueue = Volley.newRequestQueue(LogeoActivity.this);

        request = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getJSONObject("status").getInt("code")==200){

                                PC = new ClsPrefer();
                                PC.setNRO_HABITACION(LogeoActivity.this, nro_h.getText().toString());
                                PC.setUSUARIO(LogeoActivity.this, user.getText().toString());

                                Intent i = new Intent(LogeoActivity.this,MainActivity.class);
                                LogeoActivity.this.startActivity(i);
                                Toast.makeText(LogeoActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                LogeoActivity.this.finish();

                            }else{
                                String respuesta = response.getJSONObject("status").get("desc").toString();
                                setEnable(1);
                                Toast.makeText(LogeoActivity.this, respuesta, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            setEnable(1);
                            Toast.makeText(LogeoActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setEnable(1);
                Toast.makeText(LogeoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        if(CountBack==0){
            CountBack=1;
            Toast.makeText(LogeoActivity.this, "Vuelva a pulsar la tecla Atrás para cerrar la aplicación", Toast.LENGTH_SHORT).show();
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
