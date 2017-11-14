package com.example.diegollusco.hotel.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.example.diegollusco.hotel.R;
import com.example.diegollusco.hotel.clases.ClsPrefer;

import java.util.Timer;
import java.util.TimerTask;

public class InicioActivity extends AppCompatActivity {

    private ImageView im;
    private Timer timer;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        v = (View) findViewById(R.id.container_inicio);
        v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        im = (ImageView)findViewById(R.id.Inicio_Img);

        Animacion();
    }
    //Animacion
    private void Animacion(){
        AnimationSet animacion = new AnimationSet(true);

        AlphaAnimation aparicion_in = new AlphaAnimation(0, 1);
        aparicion_in.setDuration(2500);
        aparicion_in.setStartOffset(500);

        AlphaAnimation aparicion_out = new AlphaAnimation(1, 0);
        aparicion_out.setDuration(2500);
        aparicion_out.setStartOffset(3000);

        animacion.addAnimation(aparicion_in);
        animacion.addAnimation(aparicion_out);

        animacion.setFillAfter(true);
        animacion.setRepeatMode(Animation.INFINITE);
        im.startAnimation(animacion);

        //timer
        Timer();
        //fin timer
    }
    //Timer
    private void Timer(){
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                goNext();
            }
        };
        timer = new Timer();
        timer.schedule(task, 6000);
    }
    private void goNext(){
        ClsPrefer PC = new ClsPrefer();
        if(!PC.getNRO_HABITACION(this).equals("null")){
            goMain();
        } else{
            goLogin();
        }
    }
    private void goMain() {
        Intent intent = new Intent(InicioActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void goLogin() {
        Intent intent = new Intent(InicioActivity.this, LogeoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |  Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
