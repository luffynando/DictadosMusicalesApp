package com.example.misa.dictadosmusicales;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.media.SoundPool;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.lang.StringBuffer;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.misa.dictadosmusicales.MESSAGE";


    //clase para generar dictado facil
    DictadoDificil dd;
    boolean bandPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //ponemos a Android listo para ajustar el tipo de volumen de musica no te timbres
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    //cuando usario presiona el boton empezar se llama este metedo
    public void actionButtonFacil(View view) {
        Intent intent = new Intent(this, PlayingActivity.class);
        String message = getResources().getString(R.string.facil);

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void actionButtonDificil(View view) {
        Intent intent = new Intent(this, PlayingActivity.class);
        String message = getResources().getString(R.string.dificil);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    //obtiene el Id segun la nota y regresa el id del recurso
    public int getnotaID(String nota) {
        if (nota.equals("b4"))
            return (R.raw.b4);
        if (nota.equals("c5"))
            return (R.raw.c5);
        if (nota.equals("d5"))
            return (R.raw.d5);
        if (nota.equals("e5"))
            return (R.raw.e5);
        if (nota.equals("f5"))
            return (R.raw.f5);
        if (nota.equals("g5"))
            return (R.raw.g5);
        if (nota.equals("a5"))
            return (R.raw.a5);
        if (nota.equals("b5"))
            return (R.raw.b5);
        if (nota.equals("c6"))
            return (R.raw.c6);
        if (nota.equals("d6"))
            return (R.raw.d6);
        if (nota.equals("e6"))
            return (R.raw.e6);
        if (nota.equals("f6"))
            return (R.raw.f6);
        if (nota.equals("g6"))
            return (R.raw.g6);
        if (nota.equals("a6"))
            return (R.raw.a6);
        if (nota.equals("cs5"))
            return (R.raw.cs5);
        if (nota.equals("ds5"))
            return (R.raw.ds5);
        if (nota.equals("fs5"))
            return (R.raw.fs5);
        if (nota.equals("gs5"))
            return (R.raw.gs5);
        if (nota.equals("as5"))
            return (R.raw.as5);
        if (nota.equals("cs6"))
            return (R.raw.cs6);
        if (nota.equals("ds6"))
            return (R.raw.ds6);
        if (nota.equals("fs6"))
            return (R.raw.fs6);
        if (nota.equals("gs6"))
            return (R.raw.gs6);
        return (-1);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //  notasView=(TextView) findViewById(R.id.respuesta_dictado);

        if(keyCode==KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN): {
                    MediaPlayer mp= new MediaPlayer();
                    mp.reset();
                    mp= MediaPlayer.create(MainActivity.this,R.raw.a5);
                    mp.start();
            }return(true);
        }
        return super.onTouchEvent(event);


    }



    /*@Override
    public void onResume()
    {super.onResume();
        mp= new MediaPlayer();
        bandPause=false;
    }

     @Override
    public void onPause()
    {   super.onPause();
        Log.d("info"," pausado desde Main");
    }

    */


}
