package com.example.misa.dictadosmusicales;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayingActivity extends AppCompatActivity {

    public final String DEBUG_TAG="info";
    MediaPlayer mp;
    String message;
    TextView textView;
    int reproduciendo;

   ReproducingService mService;
    boolean mBound = false;


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
        mService=new ReproducingService();
        reproduciendo=0;

        registerReceiver(uiUpdated, new IntentFilter("PlayingActivity"));


    }

    private BroadcastReceiver uiUpdated= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("info", "recibido");
            textView.setText(intent.getStringExtra(ReproducingService.RESPUESTA));

        }
    };


    @Override
    public void onStart()
    {super.onStart();
      Intent  intent = new Intent(this, ReproducingService.class);
      mBound= getApplicationContext().bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        if(mBound)
            Log.d("info","bounded");
    }








    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN): {

                String dificultad= getResources().getString(R.string.texto_dificultad);
                textView.setText(dificultad+" "+message);
                if (mBound && reproduciendo==0) {
                                reproduciendo=1;
                                Log.d("info", message);
                                if( message.compareTo("Fácil")==0 ) {
                                    new Thread(new Runnable() {
                                        public void run() {
                                            Log.d("info","ejecutando hilo facil");
                                          mService.generaDictadoFacil(textView, getApplicationContext());
                                            reproduciendo=0;
                                        }
                                    }).start();
                                    Log.d("info", "despues del generaDictadoFacil");

                                }
                                else{
                                    new Thread(new Runnable() {
                                        public void run() {
                                            Log.d("info","ejecutando hilo dificil");
                                              mService.generaDictadoDificil(textView);
                                            reproduciendo=0;
                                        }
                                    }).start();
                                }


                }
            }   return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "La acción ha sido MOVER");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "La acción ha sido ARRIBA");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "La accion ha sido CANCEL");
                  stopServices();
                return true;
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
            this.stopServices();
            Intent principal= new Intent(this,MainActivity.class);
            startActivity(principal);
        }
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            this.stopServices();
            Intent principal= new Intent(this,MainActivity.class);
            startActivity(principal);

        }
        return super.onKeyDown(keyCode,event);
    }



    public void stopServices() {

        // Unbind from the service
        if (mBound) {
            getApplicationContext().unbindService(mConnection);
            Intent i = new Intent(this, ReproducingService.class);
            mService.stopSelf();
            mBound = false;
        }


    }


    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established


        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can

            // cast its IBinder to a concrete class and directly access it.
          ReproducingService.LocalBinder binder= (ReproducingService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        // Called when the connection with the service disconnects unexpectedly
        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d("info", "onServiceDisconnected");
            mBound = false;
        }
    };

        @Override
    public void onPause()
    {super.onPause();
        Log.d("info", "pause");
       stopServices();
        mService.onDestroy();
    }

    @Override
    public void onStop()
    {   super.onStop();

        Log.d("info", "servicio parado");
        stopServices();
        mService.onDestroy();
    }

    public void actionButtonStop (View view)
    { Log.d("info","parando dictado");
        mService.stopSelf();
        mService.onDestroy();
        Intent  intent =new Intent(this, MainActivity.class);
        startActivity(intent);
        reproduciendo=0;
    }

    @Override
    public void onDestroy()
    {   super.onDestroy();
        unregisterReceiver(uiUpdated);
    }
}
