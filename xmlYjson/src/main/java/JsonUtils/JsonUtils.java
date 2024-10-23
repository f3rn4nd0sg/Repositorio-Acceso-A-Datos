package JsonUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

import XmlUtils.InternetUtils;
import entidades.People;
import entidades.Tarea;

public class JsonUtils {

	/**
	 * Ejemplo de librería json-simple con un fichero profesor.json que se encuentra
	 * en los apuntes del tema
	 * 
	 * @param cadenaCompleta
	 */
	public static void leerJsonDesdeFichero(String cadenaCompleta) {
		Object obj;
		try {
			// parseado el fichero "profesor.json"
			obj = new JSONParser().parse(new FileReader(cadenaCompleta));
			// casteando obj a JSONObject
			JSONObject jo = (JSONObject) obj;
			// cogiendo el nombre y el apellido
			String nombre = (String) jo.get("nombre");
			String apellido = (String) jo.get("apellido");
			System.out.println(nombre);
			System.out.println(apellido);
			// cogiendo la edad como long
			long edad = (long) jo.get("edad");
			System.out.println(edad);
			// cogiendo direccion
			Map domicilio = ((Map) jo.get("domicilio"));
			// iterando direccion Map
			Iterator<Map.Entry> itr1 = domicilio.entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pareja = itr1.next();
				System.out.println(pareja.getKey() + " : " + pareja.getValue());
			}
			// cogiendo números de teléfonos
			JSONArray ja = (JSONArray) jo.get("numerosTelefonos");
			// iterando números de teléfonos
			Iterator itr2 = ja.iterator();
			while (itr2.hasNext()) {
				itr1 = ((Map) itr2.next()).entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry pareja = itr1.next();
					System.out.println(pareja.getKey() + " : " + pareja.getValue());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void leerLuke(String cadenaCompleta) {
		Object obj;
		try {
			// parseado el fichero "profesor.json"
			obj = new JSONParser().parse(new FileReader(cadenaCompleta));
			// casteando obj a JSONObject
			JSONObject jo = (JSONObject) obj;
			// cogiendo el nombre y el apellido
			String nombre = (String) jo.get("name");
			String altura = (String) jo.get("height");
			System.out.println("Nombre: " + nombre);
			System.out.println("Altura: " + altura);
			System.out.println("Peliculas en las que ha aparecido: ");
			JSONArray peliculas = (JSONArray) jo.get("films");
			peliculas.forEach(e -> System.out.println(e + "/n"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static <T> long devolverCuentaRegistros(String url,String campo) {
		Object obj;
		try {
			JSONObject ja = (JSONObject) new JSONParser().parse(InternetUtils.readUrl(url));
			return (long) ja.get(campo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	//Lee un json array y lo transforma a una lista de esos objetos
	public static List<Tarea> devolverTareasInternet(String url) {
		List<Tarea> resultado = new ArrayList<Tarea>();

		try {
			JSONArray ja = (JSONArray) new JSONParser().parse(InternetUtils.readUrl(url));
			
			Iterator<?> elementos = ja.iterator();
			elementos.forEachRemaining(e -> {
				JSONObject elementoObjeto = (JSONObject) e;
				resultado.add(new Tarea(
						(long) elementoObjeto.get("userId"),
						(long) elementoObjeto.get("id"),
						(String) elementoObjeto.get("title"),
						(boolean) elementoObjeto.get("completed")
						));
			});
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado;
	}
	
	//TODO aun falta por terminar esto
	private static <T> void insertarElemento(List<T> resultado, Object e) {
		//resultado.add((T)new Tarea(
			//	(long) e.get("userId"),
			//	(long) e.get("id"),
			//	(String) e.get("title"),
				//(boolean) e.get("completed")
				//));
	}
	
	public static <T> List<T> devolverListaInternet(String url) {
		List<T> resultado = new ArrayList<T>();

		try {
			JSONArray ja = (JSONArray) new JSONParser().parse(InternetUtils.readUrl(url));
			
			Iterator<?> elementos = ja.iterator();
			elementos.forEachRemaining(e -> {
				//JSONObject elementoObjeto = (JSONObject) e;
				insertarElemento(resultado,e);
			});
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultado; 
	}
	
	public static People leerStarWars(String url) {
		return new Gson().fromJson(InternetUtils.readUrl(url), People.class);

	}
	
	public static <T> T leerGenerico(String url,Class<T> clase) {
		return new Gson().fromJson(InternetUtils.readUrl(url), clase);
	}
	

}