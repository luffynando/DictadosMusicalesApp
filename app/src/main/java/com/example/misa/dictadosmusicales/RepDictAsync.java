package com.example.misa.dictadosmusicales;

import android.content.Context;
import android.content.DialogInterface;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by misa on 23/11/16.
 */

 class RepDictAsync extends AsyncTask<Void, Integer, Boolean> {
    // etiqueta para info en debug
    public final String DEBUG_TAG="info";

    private TextView textView;
    private DictadoFacil df;
    private StringBuffer text;
    private ArrayList<String> dictado;
    private MediaPlayer mp;
    private Context context;
    private int index;
    private DictadoDificil dd;
    private String message;
    private DialogInterface.OnClickListener button_ok, button_show;
    private Dictado d;
    public int duration;

    private boolean bandRepetir;

    //constructo para fácil y dificil
    public RepDictAsync(final TextView textView, Context context, String message)
    {   // le mandamos el contextl e, textView y el texto con la dificultad
        this.textView=textView;
        this.context=context;
        this.message=message;
    }

    //constructor vacio para evitar que la asyncTask sea null
    public RepDictAsync(TextView textView, Context context)
    {
        this.textView=textView;
        this.context=context;
    }

    //constructor para la repeticion
    public RepDictAsync(final TextView textView, Context context, String message, Dictado d)
    {
        this.textView=textView;
        this.context=context;
        this.message=message;
        // inicializamos el dictado
        dictado= new ArrayList<String>();
        Log.d(DEBUG_TAG,"contructor rep");
        // inicializamos el dictado general con el ultimo dictado reproducido
        this.d=PlayingActivity.d;
    }

    // hilo
    @Override
    protected Boolean doInBackground(Void... params) {
        // si es fácil
        Log.d(DEBUG_TAG, "background");
        if(message.compareTo(context.getString(R.string.facil))==0)
               generaDictadoFacil();
        else
        {// si es dificil
            if(message.compareTo(context.getString(R.string.dificil))==0)
            generaDictadoDificil();
           // si hay que repetir
            else
          repetir();
        }

     return true;
    }


    // vamos actualizando a la nota que estamos reproduciendo
    @Override
    protected void onProgressUpdate(Integer... values) {

        int progreso=  values[0].intValue();
        textView.setText("reproduciendo nota"+(progreso+1));

    }

    // andes de ejecutar el Thread
    @Override
    protected void onPreExecute(){
        index=0;
        // creamos el mp
        mp= new MediaPlayer();
        Log.d("info", "creado");
        //inicializamos las opciones para despues de reproducir
        // boton para mostrar resultado
        button_show= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(text.toString());
                PlayingActivity.bandRepetir=false;

            }
        };
        // boton para repetir dictad
        button_ok= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(message.compareTo(context.getString(R.string.facil))==0) {
                    // si el dictado a repetir es
                    PlayingActivity.d.setDictadoString(df.getDictadoString());
                    Log.d("info", "asignando dictado al PLaying"+ PlayingActivity.d.getDictadoString());
                }
                else {// si la dificutlad es dificil
                    if(message.compareTo(context.getString(R.string.dificil) )==0 )
                    {PlayingActivity.d.setDictadoString(dd.getDictadoString());
                    Log.d("info", "asignando dictado al PLaying"+ PlayingActivity.d.getDictadoString());
                    }
                }
                // ponemos el estado a no reproduciendose
                PlayingActivity.reproduciendo=0;
                textView.setText("toca la pantalla para repetir");
                // ponemos la bandera de repeticion
                PlayingActivity.bandRepetir=true;


            }
        };
    }

    // cuando cancela el dictado
    @Override
    protected void onCancelled()
    {
       cancelado();
        cancel(true);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result)
        {

            // despues de ejecutar mostramos el dialogo con los botones
            showDialog("Repetir","¿Quieres repetir el dictado?");


         //textView.setText(text.toString());

        }
    }

    // inicializamos el dialog con los botones
    public void showDialog( String title, CharSequence message) {
        // creamos el dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // le ponemos titule
        if (title != null)
        builder.setTitle(title);
        // le ponemos el mensaje
        builder.setMessage(message);
        // forzamos al usuario a elegir una opcion
        builder.setCancelable(false);
        // asignamos texto a los botones
        builder.setPositiveButton("Repetir", button_ok);
        builder.setNegativeButton("Mostrar Respuesta", button_show);
        // mostramos el alertdialog
        builder.show();
    }






    public String generaDictadoFacil()
    {

        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        df= new DictadoFacil();
        //buffer donde almacenamos las notas generadas del StringList
        text= new StringBuffer();
        //generamos el dictado
        dictado=df.generaDictado(5);
        // index para saber las notas que se llevan
        index=0;
        while(index<dictado.size())
        {

            //pasamos las Notas a String
            text.append(dictado.get(index));
            //llamo a función que encuentra los ID del raw segun su nombre
            //a su vez llamo al metodo que reproduce el sonido segun la nota
            this.audioPlayer( this.getnotaID(dictado.get(index)));
            text.append(",");
            index++;
            // mostramos el progreso con el index
            publishProgress(index);
            // si es cancelado liberamos recursos
            if(isCancelled()) {
                Log.d("info", "cancelado en dictado dificil");
                if(mp!=null) {
                    mp.stop();
                    mp.release();
                }
                return"";
            }
        }
        //soltamos el recurso mp
        mp.release();
        //escribimos las notas en el TextView

        return text.toString();
    }



    public String generaDictadoDificil()
    {   mp= new MediaPlayer();

        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        dd= new DictadoDificil();
        //creamos un arrayList de string que guardará la sucesion de notas del dictado
        //buffer donde almacenamos las notas generadas del StringList
        text= new StringBuffer();
        //generamos el dictado
        dictado=dd.generaDictado(5);

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
            publishProgress(index);
            if(isCancelled()) {
                Log.d("info", "cancelado en dictado facil");
                if(mp!=null) {
                    mp.stop();
                    mp.release();
                }
                return"";
            }
        }
        //soltamos el recurso mp
        mp.release();

        return(text.toString());

    }

    public void audioPlayer(int recurso){
        //inicializamos el mp
        mp= MediaPlayer.create(context,recurso);
        try {
            //inicializamos el mp
            mp.start();

            // si cancela en medio de la reproduccion
            if (isCancelled()) {
                Log.d("info", "cancelado despues de iniciar el start");
                if (mp != null) {
                    mp.stop();
                    mp.reset();
                    mp.release();
                    mp = null;
                }
                return;
            }
            //mientras esté reproduciendo que no pase al siguiente sonido
            while (mp.isPlaying()) {
                // si es cancelado en medio de la reproduccion
                if (isCancelled()) {
                    Log.d("info", "cancelado en while audioPlayer");
                    if (mp != null) {
                        mp.stop();
                        mp.reset();
                        mp.release();
                        mp = null;
                    }
                    return;
                }
            }

            //mp.stop();
            //mp.reset();

        } catch (Exception e) {

            mp.release();

        }
        if (isCancelled()) {
            Log.d("info", "cancelado en audioPlayer");
            if (mp != null) {
                mp.stop();
                mp.reset();
                mp.release();
                mp = null;
            }
            return;
        }
        try {
            if (mp.isPlaying()) {
                //Fix mediaplayer kk
                mp.stop();
                mp.reset();
                mp.release();
            }
        } catch (Exception e) {
            mp.release();
        }
       mp.release();
    }

    public String repetir()
    {   // ponemos la bandera de repetir a falso
        PlayingActivity.bandRepetir=false;
        mp= new MediaPlayer();
        text= new StringBuffer();
        // al dictado le asignamos el dictado guardado
        dictado=d.dictadoString;
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
            publishProgress(index);
            if(isCancelled()) {
                Log.d("info", "cancelado en dictado facil");
                if(mp!=null) {
                    mp.stop();
                    mp.release();
                }
                return"";
            }
        }
        //soltamos el recurso mp
        mp.release();

        return(text.toString());

    }

    //obtenemos el ID del recurso dado su nombre
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

    // si cancela que haga estas libreaciones del recurso
    public void cancelado()
    {
        Log.d("info", "cancelado en audioPlayer");
        try {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
        }catch (IllegalStateException e){}
    }

}
