package com.fer.ejercicio_01_28_Obligatorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import clasesUtilidades.InternetUtils;
import clasesUtilidades.JsonUtils;
import clasesUtilidades.SerializacionUtils;
import entidades.TiempoCiudad;
import entidadesCSV.TiempoCSV;

/**
 * 
 * Mi key de la API de Weather: d0e3de17102f61907397677149452be7
 * 
 * Como llamar a la API:
 * https://api.openweathermap.org/data/2.5/onecall?lat={lat}&lon={lon}&langes&appid=d0e3de17102f61907397677149452be7
 */
public class App {
	public static Scanner sc = new Scanner(System.in);
	
	// Objetos TiempoCiudad y TiempoCiudadXML para poder serializar el último que he hecho 
	public static TiempoCiudad ultimoTiempoCiudad;

	public static ArrayList<TiempoCSV> tiemposCSV = TiempoCSV
			.leerCSV("src/main/resources/datos.csv");

	public static void menuOpciones() {
		System.out.flush();
		System.out.println("Elige una de las opciones:");
		System.out.println("1. Tiempo de ciudad con latitud y longitud");
		System.out.println("2. Tiempo de ciudad con nombre de esta ");
		System.out.println("3. Temperatura en intervalo de fechas");
		System.out.println("4. Serializar datos obtenidos en apartados 1 o 2");
		System.out.println("5. Deserializar datos de las busquedas e imprimir por pantalla");
		System.out.println("6. Buscar con Otra API");
		System.out.println("7. Salir");
	}

	// Este es el apartado 1, En principio lo hace todo bien, Hacer testing de
	// errores más adelante
	// 10/01/2024, Funciona y guarda el objeto
	public static void ImprimirTiempoCiudadJSON(double latitud, double longitud) {
		System.out.println();
		// Guardo el objeto
		ultimoTiempoCiudad = JsonUtils.leerGenerico("https://api.openweathermap.org/data/2.5/weather?lat=" + latitud
				+ "&lon=" + longitud + "&lang=es&appid=d0e3de17102f61907397677149452be7", TiempoCiudad.class);
		System.out.println(ultimoTiempoCiudad.toString());
		System.out.println();
		System.out.println("Pulsa tecla enter para continuar");
	}

	// TODO
	// Creada la clase, ahora adapatar metodos o algo no se
	public static void tiempoCiudadNombre() {
		boolean infoCorrecta = true;
		do {
			System.out.println("Escribe el nombre de la ciudad");
			String nombreCiudad = sc.nextLine();
			// para que no pete al poner ciudades con espacios y tal
			nombreCiudad = nombreCiudad.replace(" ", "+");
			ImprimirTiempoCiudadNombreXML(nombreCiudad);
		} while (!infoCorrecta);
	}

	// Para llamar a la Api y que devuelva la info en XMl
	// https://api.openweathermap.org/data/2.5/weather?q=Alicante&mode=xml&appid=d0e3de17102f61907397677149452be7
	// En principio Apartado 2 Funciona Ya, ¿Como funciona? con libreria DOM
	public static void ImprimirTiempoCiudadNombreXML(String nombreCiudad) {
		try {
			try {
				ultimoTiempoCiudad = TiempoCiudad.leerDesdeXML(InternetUtils.readUrl("https://api.openweathermap.org/data/2.5/weather?q=" + nombreCiudad
						+ "&lang=es&mode=xml&appid=d0e3de17102f61907397677149452be7"));
			} catch (ParserConfigurationException | SAXException | IOException e) {
				System.out.println("Error :(");;
			}

			System.out.println(ultimoTiempoCiudad);
		} catch (Error e) {
			System.out.println("Error de ciudad");
			ultimoTiempoCiudad = null;
		}

	}

	public static void tiempoCiudadLatLong() {// Latitud entre -90 y 90, Longitud entre -180 y 180
		boolean infoCorrecta = true;
		try {
			do {
				System.out.flush();
				System.out.println("Introducir latitud:");
				double latitud = Double.parseDouble(sc.nextLine());
				if (latitud > -90 || latitud < 90) {
					System.out.println("Introducir longitud:");
					double longitud = Double.parseDouble(sc.nextLine());
					if (longitud > -180 || longitud < 180) {
						System.out.flush();
						ImprimirTiempoCiudadJSON(latitud, longitud);
						sc.nextLine();
						infoCorrecta = true;
					} else {
						infoCorrecta = false;
						System.out.println("Error al introducir longitud");
						Thread.sleep(500);
					}
				} else {
					infoCorrecta = false;
					System.out.println("Error al introducir latitud");
					Thread.sleep(500);
				}

			} while (!infoCorrecta);
		} catch (NumberFormatException e) {
			System.out.println("Error de formateo de Numeros, vuelve a intentarlo");
			infoCorrecta = false;
		} catch (InterruptedException e) {
			infoCorrecta = false;
		}

	}

	//TODO meter comprobación para fechas
	public static void evoluciónRangoFechas() {
		System.out.println("Introduce fecha de inicio (yyyy-MM-dd): ");
		String fechaInicio = sc.nextLine();
		System.out.println("Introduce fecha de final (yyyy-MM-dd): ");
		String fechaFinal = sc.nextLine();

		TiempoCSV.imprimirEvolucionTemperatura(tiemposCSV, fechaInicio, fechaFinal);
	}

	public static void elegirOpciones() throws InterruptedException, IOException {
		boolean acabado = false;
		String opcion;
		do {
			System.out.flush();
			menuOpciones();
			switch (opcion = sc.nextLine()) {
			case "1":
				tiempoCiudadLatLong();
				break;
			case "2":
				tiempoCiudadNombre();
				break;
			case "3":
				evoluciónRangoFechas();
				break;
			case "4":
				if(ultimoTiempoCiudad != null) {
					if(SerializacionUtils.serializarObjetoAJson(ultimoTiempoCiudad,"src/main/resources/tiempos.dat"))
						System.out.println("Tiempo Serializado!");
					else
						System.out.println("Error al serializar!");
				}else
					System.out.println("No has hecho ninguna busqueda o hay algún problema con ella :(");
				break;
			case "5":
					 System.out.println(SerializacionUtils.deserializarObjetoDeJson("src/main/resources/tiempos.dat").toString());
				break;
			case "6":
				break;
			case "7":
				System.out.println("¡Programa terminado!");
				acabado = true;
				break;
			default:
				System.out.println("Error al elegir opción, intentalo de nuevo :(");
				// TODO esto para que tarde un poquito en borrar la pantalla
				Thread.sleep(500);
			}

		} while (!acabado);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		elegirOpciones();
	}
}
