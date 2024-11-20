package com.fer.Ejercicio_02_15_Obligatorio;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fer.Ejercicio_02_15_Obligatorio.entidades.Joke;
import com.fer.jdbc.utilidades.JdbcUtils;
import com.fer.jdbc.utilidades.JokeInserter;
import com.fer.jdbc.utilidades.UtilsAuxiliar;

/**
 * Hello world!
 *
 */
public class App {
	static String url = "jdbc:postgresql://localhost:5432/jokes";
	static String usuario = "postgres";
	static String password = "postgres";
	public static Scanner sc = new Scanner(System.in);
	static String query;
	static List<Joke> jokes = new ArrayList<Joke>();
	static List<Joke> jokesAux = new ArrayList<Joke>();

	public static void main(String[] args) {
		JdbcUtils.conexionBdd(url, usuario, password);
		elegirOpciones();
	}

	public static void menuOpciones() {
		System.out.flush();
		System.out.println("Elige una de las opciones:");
		System.out.println("1. Resetear BDD :(");
		System.out.println("2. Añadir chiste Statement");
		System.out.println("3. Añadir chiste PreparedStatement");
		System.out.println("4. Búsqueda por texto");
		System.out.println("5. Chistes sin flags");
		System.out.println("6. Salir");
	}

	@SuppressWarnings("unused")
	public static void elegirOpciones() {
		boolean acabado = false;
		String opcion;
		do {
			System.out.flush();
			menuOpciones();
			switch (opcion = sc.nextLine()) {
			case "1":
				ReiniciarBDD();
				break;
			case "2":
				AnadirChisteStatement((JdbcUtils.getConnection()));
				break; 
			case "3":
				AnadirChistePreparedStatement((JdbcUtils.getConnection()));
				break;
			case "4":
				System.out.println("Que quieres buscar?");
				buscarChistePorTexto(JdbcUtils.getConnection(),sc.nextLine());
				break;
			case "5":
				buscarChistesSinFlags(JdbcUtils.getConnection());
				break;
			case "6":
				System.out.println("¡Programa terminado!");
				acabado = true;
				break;
			default:
				System.out.println("Error al elegir opción, intentalo de nuevo :(");
			}

		} while (!acabado);
	}
	
	/**
	 * Llama al método que busca los chistes sin flags
	 * @param conn
	 */
	private static void buscarChistesSinFlags(Connection conn) {
        JokeInserter.buscarChistesSinFlags(conn);
	}
	/**
	 * Llama al método que busca los chistes por texto
	 * @param conn
	 * @param text
	 */
	private static void buscarChistePorTexto(Connection conn,String text) {
		JokeInserter.buscarChistesPorTexto(conn, text);
	}
	/**
	 * Llama al metodo que añade un chiste con Statement
	 * @param conn
	 */
	private static void AnadirChisteStatement(Connection conn) {
		JokeInserter.AnadirChisteStatement(conn);
	}
	/**
	 * Llama al metodo que añade un chiste con PreparedStatement
	 * @param conn
	 */
	private static void AnadirChistePreparedStatement(Connection conn) {
		JokeInserter.AnadirChistePreparedStatement(conn);
	}
	
	/**
	 * Vacía la BDD y la vuelve a llenar con los datos de los ficheros json
	 */
	private static void ReiniciarBDD() {
		jokes.clear(); // Vacía la lista de chistes
		CargarDatos(); // Carga los chistes
		if (JdbcUtils.conexionBdd(url, usuario, password)) {
			String query = """
					    DROP TABLE IF EXISTS jokes_flags;
					    DROP TABLE IF EXISTS jokes;
					    DROP TABLE IF EXISTS flags;
					    DROP TABLE IF EXISTS types;
					    DROP TABLE IF EXISTS categories;
					    DROP TABLE IF EXISTS language;
					"""; // ESTO ELIMINA TODAS LAS TABLAS
			JdbcUtils.ejecutarDDL(query); // Elimina la bdd
			String script = UtilsAuxiliar.leerDesdeFichero("/jokes.txt"); // Recoge el script en un array
			if (script != null) {
				String[] queries = script.split(";"); // Lo splitea para tener cada script de creacion en un string
				for (String q : queries) { // Lo recorre hasta que no quedan
					JdbcUtils.ejecutarDDL(q); // Ejecuta cada script
				}
				JokeInserter.insertJokes(jokes, JdbcUtils.getConnection()); // Inserta los chistes en la bdd
				System.out.println("BDD reiniciada");
			} else {
				System.out.println("Error leyendo el archivo jokes.txt");
			}
		} else {
			System.out.println("Error de conexion");
		}
	}

	/**
	 * Carga los datos de los chistes desde los ficheros json y los mete en la lista
	 */
	private static void CargarDatos() {
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-en.json")));
		jokesAux.forEach(j -> j.setLanguageCode(1));
		jokes.addAll(jokesAux);
		jokesAux.clear();
		
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-de.json")));
		jokesAux.forEach(j -> j.setLanguageCode(2));
		jokes.addAll(jokesAux);
		jokesAux.clear();
		
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-es.json")));
		jokesAux.forEach(j -> j.setLanguageCode(3));
		jokes.addAll(jokesAux);
		jokesAux.clear();
		
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-fr.json")));
		jokesAux.forEach(j -> j.setLanguageCode(4));
		jokes.addAll(jokesAux);
		jokesAux.clear();
		
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-pt.json")));
		jokesAux.forEach(j -> j.setLanguageCode(5));
		jokes.addAll(jokesAux);
		jokesAux.clear();
		
		jokesAux.addAll(UtilsAuxiliar.leerchistesDesdeJson(UtilsAuxiliar.leerDesdeFichero("/jokes-cs.json")));
		jokesAux.forEach(j -> j.setLanguageCode(6));
		jokes.addAll(jokesAux);
		jokesAux.clear();		
	}
}
