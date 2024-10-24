package clasesUtilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import entidades.TiempoCiudad;

public class SerializacionUtils {

	public static <T> boolean serializarObjeto(String rutaCompleta, T objeto) {
		try {
			File fichero = new File(rutaCompleta);
			FileOutputStream ficheroSalida = new FileOutputStream(fichero);
			ObjectOutputStream ficheroObjetos = new ObjectOutputStream(ficheroSalida);
			ficheroObjetos.writeObject(objeto); // Serializa
			ficheroObjetos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static <T> T deserializarObjeto(String rutaCompleta) {
		try {
			File fichero = new File(rutaCompleta);
			FileInputStream ficheroSalida = new FileInputStream(fichero);
			ObjectInputStream ficheroObjetos = new ObjectInputStream(ficheroSalida);
			@SuppressWarnings("unchecked")
			T objeto = (T) ficheroObjetos.readObject(); // Serializa
			ficheroObjetos.close();
			return objeto;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> boolean serializarListaObjetos(String rutaCompleta, List<T> objetos) {

		try {
			ObjectOutputStream ficheroObjetos = new ObjectOutputStream(new FileOutputStream(new File(rutaCompleta)));
			ficheroObjetos.writeObject(objetos); // Serializa
			ficheroObjetos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static <T> List<T> deserializarListaObjetos(String rutaCompleta) {
		try {
			ObjectInputStream ficheroObjetos = new ObjectInputStream(new FileInputStream(new File(rutaCompleta)));
			List<T> lista = (List<T>) ficheroObjetos.readObject(); // Serializa
			ficheroObjetos.close();
			return lista;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Dado un objeto tiempo y una ruta, serializa el objeto en el archivo a JSON
	 * 
	 * @param tiempo
	 * @param archivo
	 * @throws IOException
	 * @returns true si se ha podido, false si ha petado
	 */
	public static boolean serializarObjetoAJson(TiempoCiudad tiempo, String archivo) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(archivo)) {
			gson.toJson(tiempo, writer);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Dado un archivo con Tiempos serializados, devuelve una lista de Tiempos
	 * 
	 * @param archivo
	 * @return Lista de tiempos desserializados
	 * @throws IOException
	 */
	public static List<TiempoCiudad> deserializarListaDeJson(String archivo) throws IOException {
		Gson gson = new Gson();
		Type listType = new TypeToken<List<TiempoCiudad>>() {
		}.getType();
		try (Reader reader = new FileReader(archivo)) {
			return gson.fromJson(reader, listType);
		}
	}

	public static TiempoCiudad deserializarObjetoDeJson(String archivo) throws IOException {
		Gson gson = new Gson();
		try (Reader reader = new FileReader(archivo)) {
			return gson.fromJson(reader, TiempoCiudad.class);
		}
	}

	/**
	 * Dado una lista de tiempos y una archivo, serializa los objetos
	 * 
	 * @param listaTiempo
	 * @param archivo
	 * @return true si se ha podido, false si algo ha fallado
	 * @throws IOException
	 */
	public static boolean serializarListaAJson(List<TiempoCiudad> listaTiempo, String archivo) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new FileWriter(archivo)) {
			gson.toJson(listaTiempo, writer); // Serializa la lista en lugar de un objeto
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Dado una lista de tiempos y un tiempo, si este tiene el mismo dia que un
	 * tiempo de la lista, lo sustituye, si no, lo agrega
	 * 
	 * @param lista
	 * @param nuevoTiempo
	 */
	public static void reemplazarTiempo(List<TiempoCiudad> lista, TiempoCiudad nuevoTiempo) {
		TiempoCiudad aux = lista.stream().filter(t -> t.getDt().equalsIgnoreCase(nuevoTiempo.getDt())).findFirst()
				.orElse(null);// Devuelve null si no se encuentra

		if (aux != null) {
			System.out.println("Tiempo con mismo día encontrado y sustituido");
			lista.set(lista.indexOf(aux), nuevoTiempo); // Reemplazar el objeto existente
		} else {
			System.out.println("No hay tiempos con el mismo día, agregado");
			lista.add(nuevoTiempo);
		}
	}

}