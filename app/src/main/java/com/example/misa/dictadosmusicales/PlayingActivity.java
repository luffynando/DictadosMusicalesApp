package com.example.misa.dictadosmusicales;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PlayingActivity extends AppCompatActivity {

    public final String DEBUG_TAG="info";
    MediaPlayer mp;
    String message;
    TextView textView;
    RepDictAsync reproduceDictad;
   static  int reproduciendo;
   static  Dictado d;
    static boolean bandRepetir;
    Toolbar toolbar;
    FrameLayout statusBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Log.i("inf","iniciando segunda Activity");
        textView= (TextView)findViewById(R.id.respuesta_dictado);
        //obtenemos el intent de MainActivity
        Intent intent= getIntent();
        message =intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Configuramos el statusBar y el Toolbar
        toolbarStatusBar(message);

        // Ajustamos algunas cosas con el modo de orientacion y el statusBar y navigation bar
        navigationBarStatusBar();

        mp= new MediaPlayer();
        textView.setText(getResources().getString(R.string.toque_para_rep));
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        reproduciendo=0;
        reproduceDictad= new RepDictAsync(textView, this);
        bandRepetir=false;
        d= new DictadoDificil();
    }




   public void actionButtonStop(View view)
   {       Log.d("info", "buttonStop");
       regresaPrincipal();
   }

    // detiene servicios limpia cache y regresa el menú principal
    public void regresaPrincipal()
    {
        if(reproduceDictad.getStatus()!=AsyncTask.Status.FINISHED)
            reproduceDictad.onCancelled();
        MainActivity.deleteCache(this);
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);

    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN): {
                    Log.d("info","valor de reproduciendo "+ reproduciendo );
                String dificultad= getResources().getString(R.string.texto_dificultad);
                textView.setText(dificultad+" "+message);
                if(bandRepetir && reproduciendo==0) {
                    reproduciendo=1;
                    reproduceDictad= new RepDictAsync(textView,this,getString(R.string.repetir),d);
                    reproduceDictad.execute();
                }
                if( message.compareTo(getString(R.string.facil))==0 && reproduciendo==0 ) {
                    reproduciendo=1;
                     reproduceDictad = new RepDictAsync(textView, this, message);
                    reproduceDictad.execute();
                }
                else
                {   if(reproduciendo==0) {
                     reproduciendo=1;
                     reproduceDictad = new RepDictAsync(textView, this, message);
                     reproduceDictad.execute();
                    }
                }

                 if(AsyncTask.Status.FINISHED== reproduceDictad.getStatus()   )
                  { reproduciendo=0;

                  }
            }   return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "La acción ha sido MOVER");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "La acción ha sido ARRIBA");
                return true;
            case (MotionEvent.ACTION_CANCEL): {
                Log.d(DEBUG_TAG, "La accion ha sido CANCEL");
                reproduceDictad.onCancelled();
                Intent principal = new Intent(this, MainActivity.class);
                startActivity(principal);

            } return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG,
                        "La accion ha sido fuera del elemento de la pantalla");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            regresaPrincipal();
        }
        return super.onKeyDown(keyCode,event);
    }


    @Override
    public void onBackPressed()
    {super.onBackPressed();
        regresaPrincipal();
    }

    public void toolbarStatusBar(String aux) {
        // Cast toolbar and status bar
        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Modo: "+aux);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void navigationBarStatusBar() {

        // Arreglamos algunos bugs en portrait modo
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Arreglamos algunos bugs en la configuracion del StatusBar color primario en kitkat
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                PlayingActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Arreglamos algunos bugs en la configuracion del StatusBar color primario en lollipop y versiones posteriores
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                PlayingActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
                final int color = typedValue21.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
                getWindow().setStatusBarColor(color);
            }
        }

        // Arreglamos algunos bugs en landscape modo (Lollipop)
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                PlayingActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                PlayingActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
    }


    /*@Override
    public void onPause()
    {super.onPause();
       // if(reproduceDictad.getStatus()!=AsyncTask.Status.FINISHED)
         //   reproduceDictad.onCancelled();
       // Intent principal= new Intent(this,MainActivity.class);
        //startActivity(principal);
    }*/





}
