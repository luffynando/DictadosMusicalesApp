package com.example.misa.dictadosmusicales;

import java.util.ArrayList;

/**
 * Created by misa on 24/11/16.
 */

public abstract class Dictado{

    protected  ArrayList<String> dictadoString;


        abstract ArrayList<String> generaDictado(int numNotas);
        abstract Nota generaNotaInicial();
        abstract ArrayList<String> getDictadoString();
        abstract void setDictadoString(ArrayList<String> dictadoString);
}
