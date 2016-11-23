package com.example.misa.dictadosmusicales;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import android.widget.TextView;

import android.media.MediaPlayer;

import java.util.ArrayList;


/**
 * Created by misa on 21/11/16.
 */

public class ReproducingService extends Service {

    // Binder given to clients
    private IBinder mBinder;
    DictadoFacil df;
    StringBuffer text;
    ArrayList<String> dictado;
    MediaPlayer mp;
    boolean bandReproduce;
    DictadoDificil dd;
    static boolean dictadoTerminado;



    public ReproducingService()
    {
         mBinder = new LocalBinder();
    }
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ReproducingService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ReproducingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public String generaDictadoDificil(TextView view)
    {mp= new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        dd= new DictadoDificil();
        //creamos un arrayList de string que guardará la sucesion de notas del dictado




        //buffer donde almacenamos las notas generadas del StringList
        text= new StringBuffer();
        //generamos el dictado
        dictado=dd.generaDictado(20);

        int index=0;
        while(index<dictado.size())
        {
            //pasamos las Notas a String
            text.append(dictado.get(index));
            //llamo a función que encuentra los ID del raw segun su nombre
            //a su vez llamo al metodo que reproduce el sonido segun la nota
            this.audioPlayer( this.getnotaID(dictado.get(index)  ));
            text.append(",");
            index++;
        }
        //soltamos el recurso mp
        mp.release();
        //escribimos las notas en el TextView
        return(text.toString());

    }


    public String generaDictadoFacil(TextView textView)
    {




        Log.d("info"," en el servicio");
        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        df= new DictadoFacil();
        //creamos un arrayList de string que guardará la sucesion de notas del dictado

        //encontramos el TextView para poner las notas del dictado


        //buffer donde almacenamos las notas generadas del StringList
        text= new StringBuffer();
        //generamos el dictado
        dictado=df.generaDictado(20);
        bandReproduce=true;
        int index=0;
        while(index<dictado.size())
        {

            //pasamos las Notas a String
            text.append(dictado.get(index));
            //llamo a función que encuentra los ID del raw segun su nombre
            //a su vez llamo al metodo que reproduce el sonido segun la nota
            this.audioPlayer( this.getnotaID(dictado.get(index)  ));
            text.append(",");
            index++;
        }
        //soltamos el recurso mp
        mp.release();
        //escribimos las notas en el TextView

        return text.toString();
    }

    public int getnotaID(String nota)
    {
        if(nota.equals("b4"))
            return(R.raw.b4);
        if(nota.equals("c5"))
            return(R.raw.c5);
        if(nota.equals("d5"))
            return(R.raw.d5);
        if(nota.equals("e5"))
            return(R.raw.e5);
        if(nota.equals("f5"))
            return(R.raw.f5);
        if(nota.equals("g5"))
            return(R.raw.g5);
        if(nota.equals("a5"))
            return(R.raw.a5);
        if(nota.equals("b5"))
            return(R.raw.b5);
        if(nota.equals("c6"))
            return(R.raw.c6);
        if(nota.equals("d6"))
            return(R.raw.d6);
        if(nota.equals("e6"))
            return(R.raw.e6);
        if(nota.equals("f6"))
            return(R.raw.f6);
        if(nota.equals("g6"))
            return(R.raw.g6);
        if(nota.equals("a6"))
            return(R.raw.a6);
        if(nota.equals("cs5"))
            return(R.raw.cs5);
        if(nota.equals("ds5"))
            return(R.raw.ds5);
        if(nota.equals("fs5"))
            return(R.raw.fs5);
        if(nota.equals("gs5"))
            return(R.raw.gs5);
        if(nota.equals("as5"))
            return(R.raw.as5);
        if(nota.equals("cs6"))
            return(R.raw.cs6);
        if(nota.equals("ds6"))
            return(R.raw.ds6);
        if(nota.equals("fs6"))
            return(R.raw.fs6);
        if(nota.equals("gs6"))
            return(R.raw.gs6);
        return(-1);
    }

    public void audioPlayer(int recurso){


        mp = MediaPlayer.create(ReproducingService.this, recurso);
        int aux = 0;
        try {
            mp.start();

            while (mp.getDuration() > aux) {
                aux = mp.getCurrentPosition();
            }

        }catch(IllegalStateException e){
            mp.release();
            stopSelf();
            }

    }

    public void notaDeReferencia()
    {
        mp= MediaPlayer.create(ReproducingService.this,R.raw.a5);
        mp.start();
    }

    @Override
    public void onDestroy()
    { super.onDestroy();
        if(mp!=null) {
            mp.release();
            mp = null;
        }
        bandReproduce=false;
            stopSelf();
    }





}