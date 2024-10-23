package com.fer.prueba2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fer.ficheros.utilidades.FicherosUtils;
import com.fer.ficheros.utilidades.SerializacionUtils;
import com.fer.prueba2.entidades.Alumno;

public class App 
{
    public static void main( String[] args )
    {
    	PruebasSerializacion();
    }
    public void PruebFicheros() {
    	FicherosUtils.devolverLineasFichero("C:\\Ficheros\\ejemplo1.txt").stream().filter(e->e.length()>4).forEach(e->System.out.println(e));
        FicherosUtils.leerFichero("C:\\Ficheros\\ejemplo1.txt");
        //Obten lineas que tengan una a y que sean con más de 4 letras
        FicherosUtils.devolverLineasFichero("C:\\Ficheros\\ejemplo1.txt").stream().filter(e->e.contains("a") && e.length()>4).forEach(e->System.out.println(e));
        List<String> alumnosDam = FicherosUtils.devolverLineasFichero("C:\\Ficheros\\ejemplo1.txt").stream().filter(e->e.contains("2ºDAM")).collect(Collectors.toList());
        
        List<String>alumnosDaw = new ArrayList<String>();
        
        List<Alumno> alumnos = new ArrayList<Alumno>(); 
        //TODO por algun motivo, el stream ha muerto
        /*
        alumnosDam.stream().
        forEach(e->alumnos.add (new Alumno
        		(e.split(";")[0]
        		,e.split(";")[1]
        		,Double.ParseDouble(e.split(";")[2]))));
        */
        List<String> Lineas = new ArrayList<String>();
        
        //TODO por otro motivo, el arrayList ha muerto
        ///Lineas.add("Primera Linea");
        ///Lineas.add("Segunda Linea");
        ///Lineas.add("Tercera Linea");
    }
    public static void PruebasSerializacion() {
    	
    	//if (SerializacionUtils.serializarObjeto(new Alumno("2ºDAM","Fer",10.0), "C:/ficheros/alumnos24.dat"))
    		System.out.println("Serializaion realizada correctamente");
    	
    	
    //	Alumno alumno = SerializacionUtils.deSerializarObjeto("C:/ficheros/alumnos24.dat");
    	//	System.out.println(alumno);
    		
    		List<Alumno> alumnos = new ArrayList<Alumno>();
    		alumnos.add(new Alumno("2ºDAM","Fran",10.0));
    		alumnos.add(new Alumno("2ºDAM","Paco",8.0));
    		alumnos.add(new Alumno("2ºDAM","Pepe",7.0));
    		alumnos.add(new Alumno("2ºDAM","David",2.0));
    		
    		if(SerializacionUtils.serializarListaObjetos("C:/ficheros/lista24.dat", alumnos))
    			System.out.println("Serializacion de lista de alumnos correcta");
    		else
    			System.out.println("Serialización de lista de alumnos incorrecta");
    		
    	//	alumnos = SerializacionUtils.desSerializarListaObjetos("C:/ficheros/lista24.dat");
    		alumnos.forEach(e->System.out.println(e));
    }
}
