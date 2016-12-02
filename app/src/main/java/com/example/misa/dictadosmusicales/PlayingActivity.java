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
    // etiqueta para info en debug
    public final String DEBUG_TAG="info";
    // objeto mediaPLayer para reproducir los ogg
    MediaPlayer mp;
    // mensaje recibido por el intent anterior
    String message;
    // textview para los resultados
    TextView textView;
    // objeto que reproduce los dictados
    RepDictAsync reproduceDictad;
    // variables para validar estados de reproducción
   static  int reproduciendo;
   static  Dictado d;
    static boolean bandRepetir;
    Toolbar toolbar;
    // frame para el toolbar
    FrameLayout statusBar;



    // inicializamos variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Log.i(DEBUG_TAG,"iniciando segunda Activity");
        //inicializamos el textView
        textView= (TextView)findViewById(R.id.respuesta_dictado);
        //obtenemos el intent de MainActivity
        Intent intent= getIntent();
        message =intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //Configuramos el statusBar y el Toolbar
        toolbarStatusBar(message);

        // Ajustamos algunas cosas con el modo de orientacion y el statusBar y navigation bar
        navigationBarStatusBar();
        //inicializmoas media player
        mp= new MediaPlayer();
        // ponemos el textView con instrucciones
        textView.setText(getResources().getString(R.string.toque_para_rep));
        // controlador del audio
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // no reproduciendo
        reproduciendo=0;
        // creamos la asyncTask
        reproduceDictad= new RepDictAsync(textView, this);
        bandRepetir=false;
        // inicializamos d, para que no esté vacío
        d= new DictadoDificil();
    }




   public void actionButtonStop(View view)
   {       Log.d("info", "buttonStop");
       regresaPrincipal();
   }

    // detiene servicios limpia cache y regresa el menú principal
    public void regresaPrincipal()
    {      // si el asynctask no ha terminado lo detenemos
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
                    Log.d(DEBUG_TAG,"valor de reproduciendo "+ reproduciendo );
                // decimos en el textView que estamos reproduciendo
                String dificultad= getResources().getString(R.string.texto_dificultad);
                textView.setText(dificultad+" "+message);
                //checamos que no se esté reproduciendo y si se va a repetir el dictado
                if(bandRepetir && reproduciendo==0) {
                    // ponemos el estado a reproduciendo
                    reproduciendo=1;
                    // creamos el asyncTask
                    reproduceDictad= new RepDictAsync(textView,this,getString(R.string.repetir),d);
                    // ejecutamos
                    reproduceDictad.execute();
                }
                // if si es fácil y no se está reproduccioend
                if( message.compareTo(getString(R.string.facil))==0 && reproduciendo==0 ) {
                    reproduciendo=1;
                     reproduceDictad = new RepDictAsync(textView, this, message);
                    reproduceDictad.execute();
                }
                else
                {   // para dificil
                    if(reproduciendo==0) {
                     reproduciendo=1;
                     reproduceDictad = new RepDictAsync(textView, this, message);
                     reproduceDictad.execute();
                    }
                }
                    // cuando termina de reproducir se pone reproduciendo a cero
                 if(AsyncTask.Status.FINISHED== reproduceDictad.getStatus()   )
                  { reproduciendo=0;

                  }
            }   return true;
            // demás opciones
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

    // si presiona back
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // regresamos al intent anterior
            regresaPrincipal();
        }
        return super.onKeyDown(keyCode,event);
    }

    //sobreescribimos el metodo back
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
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // bugs de vista en distintas versiones de Android
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


}
