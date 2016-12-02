package com.example.misa.dictadosmusicales;

import java.util.ArrayList;

/**
 * Created by misa on 24/11/16.
 */
//clase abstract que genera los dictados
public abstract class Dictado{

    //array donde guardamos la lista del dictado
    protected  ArrayList<String> dictadoString;

        //metodo que egenera el dictado
        abstract ArrayList<String> generaDictado(int numNotas);
        //metodo que genera la nota inicial
        abstract Nota generaNotaInicial();
        //metodo que obtiene el dictado
        abstract ArrayList<String> getDictadoString();
        //metodo que cambia notas del dictado
        abstract void setDictadoString(ArrayList<String> dictadoString);
}
