package com.example.misa.dictadosmusicales;

/**
 * Created by misa on 20/11/16.
 */

import java.util.ArrayList;
import java.util.Random;

public class DictadoDificil {


    Nota b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6;
    Nota cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6;
    Nota[] notasPosibles;

    int numNotasPosibles=24;
    ArrayList<String> dictadoString;

    public DictadoDificil()
    {
        //inicialicamos el array ahora inicialicamos el array con una variable final
        //para así sea fácil modificar el número de elementos posibles en el dictado
        notasPosibles= new Nota[ numNotasPosibles];
        //al array de notas le agregamos la nota inicializada con el nombre del nota
        notasPosibles[0]=b4=new Nota("b4");
        notasPosibles[1]=c5=new Nota("c5");
        notasPosibles[2]=d5= new Nota("d5");
        notasPosibles[3]=e5= new Nota("e5");
        notasPosibles[4]=f5= new Nota("f5");
        notasPosibles[5]=g5= new Nota("g5");
        notasPosibles[6]=a5= new Nota("a5");
        notasPosibles[7]=b5= new Nota("b5");
        notasPosibles[8]=c6= new Nota("c6");
        notasPosibles[9]=d6= new Nota("d6");
        notasPosibles[10]=e6= new Nota("e6");
        notasPosibles[11]=f6= new Nota("f6");
        notasPosibles[12]=g6= new Nota("g6");
        notasPosibles[13]=a6= new Nota("a6");
        notasPosibles[14]=cs5= new Nota("cs5");
        notasPosibles[15]=ds5= new Nota("ds5");
        notasPosibles[16]=fs5= new Nota("fs5");
        notasPosibles[17]=gs5= new Nota("gs5");
        notasPosibles[18]=as5= new Nota("as5");
        notasPosibles[19]=cs6= new Nota("cs6");
        notasPosibles[20]=ds6= new Nota("ds6");
        notasPosibles[21]=fs6= new Nota("fs6");
        notasPosibles[22]=gs6= new Nota("gs6");
        notasPosibles[23]=as6= new Nota("gs6");
        //inicializamos el arraylist donde guardaremos el nombre de las notas para relacionarlos
        //con el archivo raw/"nombre de la nota" que contiene el sonido
        dictadoString= new ArrayList<String>();

    }



    public ArrayList<String> generaDictado(int numNotas)
    {
        ArrayList<Nota> dictado= new ArrayList<Nota>();

        //llamamos el metodo notasPosibles de la nota y le agregamos las notas posibles
        b4.notasPosibles(c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        c5.notasPosibles(b4,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        d5.notasPosibles(b4,c5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        e5.notasPosibles(b4,c5,d5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        f5.notasPosibles(b4,c5,d5,e5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        g5.notasPosibles(b4,c5,d5,e5,f5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        a5.notasPosibles(b4,c5,d5,e5,f5,g5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        b5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        c6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        d6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        e6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        f6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        g6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        a6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);

        cs5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        ds5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,fs5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        fs5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,gs5,as5,cs6,ds6,fs6,gs6,as6);
        gs5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,as5,cs6,ds6,fs6,gs6,as6);
        as5.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,cs6,ds6,fs6,gs6,as6);
        cs6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,ds6,fs6,gs6,as6);
        ds6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,fs6,gs6,as6);
        fs6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,gs6,as6);
        gs6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,as6);
        as6.notasPosibles(b4,c5,d5,e5,f5,g5,a5,b5,c6,d6,e6,f6,g6,a6,cs5,ds5,fs5,gs5,as5,cs6,ds6,fs6,gs6);

        //generamos la nota inicial del dictado aleatoramiente
        dictado.add(this.generaNotaInicial());
        System.out.println("nota inicial "+ dictado.get(0).getName());
        dictadoString.add(dictado.get(0).getName());
        int aux=1;

        //generamos dictado del numero de notas recibido
        while(dictado.size()<numNotas)
        {
            dictado.add(dictado.get(aux-1).eligeNotaSiguienteDifcil(numNotasPosibles));
            dictadoString.add(dictado.get(aux).getName());
            //System.out.println("agregada nota: "+ dictado.get(aux).getName());
            aux++;

        }

        //por ahora no regresemas ningun dictado
        return(dictadoString);
    }


    public Nota generaNotaInicial()
    {
        //definimos un random para el numero aleatorio
        Random rnd= new Random();
        //casteamos el aleatorio y lo ponemos en un rango de 0 al numero de notas Posibles
        int aux=(int) (rnd.nextDouble()*this.numNotasPosibles);

        //retornamos la primera nota
        return(notasPosibles[aux]);
    }


}
