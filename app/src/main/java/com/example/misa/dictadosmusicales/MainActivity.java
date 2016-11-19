package com.example.misa.dictadosmusicales;


import android.media.MediaPlayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.lang.StringBuffer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public int button_band;



    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp= new MediaPlayer();

        button_band=0;


    }

    //cuando usario presiona el boton empezar se llama este metedo
    public void generaDictado(View view)
    {       button_band++;
        //verificamos que si se presiona mientras reproduce dictado no se genere otro dictado
        if(button_band>1)
            return;
        //generamos objeto de la clase  DictadoFacil que generará los dictados para el nivel facil
        DictadoFacil df= new DictadoFacil();
        //creamos un arrayList de string que guardará la sucesion de notas del dictado
        //mandamos el número de notas que contendrá el dictado
        ArrayList<String> dictado;

        //encontramos el TextView para poner las notas del dictado
        TextView notasView=(TextView) findViewById(R.id.texto);

        //buffer donde almacenamos las notas generadas del StringList
        StringBuffer text= new StringBuffer();
        //generamos el dictado
        dictado=df.generaDictado(20);

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
        notasView.setText(text.toString());
        button_band=0;

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
        return(-1);
    }


    public void audioPlayer(int recurso){


        mp = MediaPlayer.create(MainActivity.this, recurso);
           try {

                    mp.start();
               while(mp.isPlaying())
               {

               }
                } catch (Exception e) {
                    e.printStackTrace();
                }



    }


}
