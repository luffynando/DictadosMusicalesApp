package com.example.misa.dictadosmusicales;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;


public class PlayingActivity extends AppCompatActivity {

    public final String DEBUG_TAG="info";
    MediaPlayer mp;
    String message;
    TextView textView;
    RepDictAsync reproduceDictad;
   static  int reproduciendo;
   static  Dictado d;
    static boolean bandRepetir;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        Log.i("inf","iniciando segunda Activity");
        textView= (TextView)findViewById(R.id.respuesta_dictado);
        //obtenemos el intent de MainActivity
        Intent intent= getIntent();
        message =intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
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

    /*@Override
    public void onPause()
    {super.onPause();
       // if(reproduceDictad.getStatus()!=AsyncTask.Status.FINISHED)
         //   reproduceDictad.onCancelled();
       // Intent principal= new Intent(this,MainActivity.class);
        //startActivity(principal);
    }*/





}
