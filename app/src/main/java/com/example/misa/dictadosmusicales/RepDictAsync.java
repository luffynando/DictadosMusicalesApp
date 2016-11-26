package com.example.misa.dictadosmusicales;

import android.content.Context;
import android.content.DialogInterface;

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

    private TextView textView;
    private int i;
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
    {
        this.textView=textView;
        this.context=context;
        this.message=message;
    }

    //constructor vacio para evitar que la asymcTask sea null
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
        dictado= new ArrayList<String>();
        Log.d("info","contructor rep");
        this.d=PlayingActivity.d;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        Log.d("info", "background");
        if(message.compareTo(context.getString(R.string.facil))==0)
               generaDictadoFacil();
        else
        {if(message.compareTo(context.getString(R.string.dificil))==0)
            generaDictadoDificil();
         else
          repetir();
        }

     return true;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {

        int progreso=  values[0].intValue();
        textView.setText("reproduciendo nota"+(progreso+1));

    }

    @Override
    protected void onPreExecute(){
        index=0;
        mp= new MediaPlayer();
        Log.d("info", "creado");

        button_show= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(text.toString());
                PlayingActivity.bandRepetir=false;

            }
        };

        button_ok= new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(message.compareTo(context.getString(R.string.facil))==0) {
                    PlayingActivity.d.setDictadoString(df.getDictadoString());
                    Log.d("info", "asignando dictado al PLaying"+ PlayingActivity.d.getDictadoString());
                }
                else {
                    if(message.compareTo(context.getString(R.string.dificil) )==0 )
                    {PlayingActivity.d.setDictadoString(dd.getDictadoString());
                    Log.d("info", "asignando dictado al PLaying"+ PlayingActivity.d.getDictadoString());
                    }
                }
                textView.setText("toca la pantalla para repetir");
                PlayingActivity.bandRepetir=true;


            }
        };
    }

    @Override
    protected void onCancelled()
    {
        if(mp!=null)
        {   Log.d("info", "ejecutando on Cancelled");
            try {
                mp.stop();
            }catch(IllegalStateException e){mp.release();}
            mp.release();
        }
        cancel(true);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(result)
        {


            showDialog("Repetir","¿Quieres repetir el dictado?");

         //textView.setText(text.toString());

        }
    }

    public void showDialog( String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Repetir", button_ok);
        builder.setNegativeButton("Mostrar Respuesta", button_show);
        builder.show();
    }






    public String generaDictadoFacil()
    {

        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        df= new DictadoFacil();
        //creamos un arrayList de string que guardará la sucesion de notas del dictado

        //encontramos el TextView para poner las notas del dictado


        //buffer donde almacenamos las notas generadas del StringList
        text= new StringBuffer();
        //generamos el dictado
        dictado=df.generaDictado(5);
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
            publishProgress(index);
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

       mp= MediaPlayer.create(context,recurso);
        duration=mp.getDuration();
        int aux = 0;
        try {
            mp.start();


            if(isCancelled())
            {   Log.d("info", "cancelado despues de iniciar el start");
                if(mp!=null)
                { mp.stop();
                    mp.release();
                }
                return;
            }


            while (duration > aux) {
                aux = mp.getCurrentPosition();
                //Log.d("info","ciclado");
                if(isCancelled())
                {   Log.d("info", "cancelado en while audioPlayer");
                    if(mp!=null)
                    { mp.stop();
                        mp.release();
                    }
                    return;
                }
            }

        }catch(Exception e){

            mp.release();

        }
        if(isCancelled())
        {   Log.d("info", "cancelado en audioPlayer");
            if(mp!=null)
            { mp.stop();
                mp.release();
            }
            return;
        }
        if(mp!=null)
        {
            mp.release();
        }
    }

    public String repetir()
    {
        mp= new MediaPlayer();
        text= new StringBuffer();
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

}
