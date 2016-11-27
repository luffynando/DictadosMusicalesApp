package com.example.misa.dictadosmusicales;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.StringBuffer;

import java.util.ArrayList;




public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.misa.dictadosmusicales.MESSAGE";


    //clase para generar dictado facil
    DictadoDificil dd;
    boolean bandPause;
    private Boolean exit = false;
    Toolbar toolbar;
    FrameLayout statusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //ponemos a Android listo para ajustar el tipo de volumen de musica no te timbres
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //Configuramos el statusBar y el Toolbar
        toolbarStatusBar();
        // Ajustamos algunas cosas con el modo de orientacion y el statusBar y navigation bar
        navigationBarStatusBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Agregamos el menu, esto agrega el menu si es que existe algun action bar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Las acciones que se deben tomar al presionar en alguna de las opciones del menu
        //Si se definen homeUp, Long, se debera crear en el manifest, el PARENT_ACTIVITY
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.ventana_acercade);
            final TextView author = (TextView) dialog.findViewById(R.id.versionAppLink);
            author.setMovementMethod(LinkMovementMethod.getInstance());
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void ajustarStatus(){
        if (Build.VERSION.SDK_INT >=19) {
            FrameLayout status = (FrameLayout) findViewById(R.id.statusBar);
            status.setVisibility(View.GONE);
        }
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
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, getString(R.string.salir),
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

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

    public void toolbarStatusBar() {

        // Definimos el toolbar y el Status bar de nuestro view
        statusBar = (FrameLayout) findViewById(R.id.statusBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
    }

    public void navigationBarStatusBar() {

        // Arreglamos algunos bugs en portrait modo
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Arreglamos algunos bugs en la configuracion del StatusBar color primario en kitkat
            if (Build.VERSION.SDK_INT >= 19) {
                TypedValue typedValue19 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }

            // Arreglamos algunos bugs en la configuracion del StatusBar color primario en lollipop y versiones posteriores
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue21 = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue21, true);
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
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue19, true);
                final int color = typedValue19.data;
                FrameLayout statusBar = (FrameLayout) findViewById(R.id.statusBar);
                statusBar.setBackgroundColor(color);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                TypedValue typedValue = new TypedValue();
                MainActivity.this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
                final int color = typedValue.data;
                getWindow().setStatusBarColor(color);
            }
        }
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


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {}
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


}
