package com.fer.jdbc.utilidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

import com.fer.Ejercicio_02_15_Obligatorio.entidades.Joke;

public class JokeInserter {

	static String query;
	static ResultSet rs;
	
	/**
	 * Inserta un chiste en la tabla jokes_flags
	 * @param joke Joke a insertar
	 * @param conn Conexiona  la BDD
	 */
	public static void insertJokesFlags(Joke joke, Connection conn) {
		// Preparo el insert en jokes_flags
		String insertFlagSQL = "INSERT INTO jokes_flags (joke_id_chiste, joke_id_idioma, flag_id) VALUES (?, ?, ?)";
		try (PreparedStatement flagStatement = conn.prepareStatement(insertFlagSQL)) {
			// Compruebo cada flag
			if (joke.getFlags().isNsfw()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "nsfw", conn);
			}
			if (joke.getFlags().isReligious()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "religious", conn);
			}
			if (joke.getFlags().isPolitical()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "political", conn);
			}
			if (joke.getFlags().isRacist()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "racist", conn);
			}
			if (joke.getFlags().isSexist()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "sexist", conn);
			}
			if (joke.getFlags().isExplicit()) {
				insertFlag(flagStatement, joke.getId(), joke.getLanguageCode(), "explicit", conn);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserta una lista de chistes en la tabla jokes
	 * 
	 * @param jokes Lista de chistes
	 * @param conn  Conexion a la BDD
	 */
	public static void insertJokes(List<Joke> jokes, Connection conn) {
		// Preparo el insert en jokes
		String insertJokeSQL = "INSERT INTO jokes (id_idioma, category_id, type_id ,joke, id_chiste) VALUES (?, ?, ?, ?, ?)";
		try {
			for (Joke joke : jokes) {
				try (PreparedStatement jokeStatement = conn.prepareStatement(insertJokeSQL)) {
					// Preparo los campos(?) con los valores de joke
					jokeStatement.setInt(1, joke.getLanguageCode());
					jokeStatement.setInt(2, getCategoryId(joke.getCategory(), conn));
					jokeStatement.setInt(3, getTypeId(joke.getType(), conn));
					jokeStatement.setString(4,
							joke.getType().equals("twopart") ? joke.getSetup() + ";" + joke.getDelivery()
									: joke.getJoke());
					jokeStatement.setInt(5, joke.getId());

					jokeStatement.executeUpdate();
				}
				insertJokesFlags(joke, conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obtiene el id de una categoría
	 * @param category categoria del que buscar el id
	 * @param conn Conexion a la BDD
	 * @return el id de la categoría pasada por parámetro
	 * @throws SQLException SQLException si no se encuentra y/o falla la consulta
	 */
	private static int getCategoryId(String category, Connection conn) throws SQLException {
		// Preparo la consulta para obtener el id de la categoría
		query = "SELECT id FROM categories WHERE name = ?";
		// Uso el método devolverPreparedStatement de JdbcUtils para ejecutar la
		// consulta
		ResultSet rs = JdbcUtils.devolverPreparedStatement(query, category);
		// Si se encuentra la categoría, devuelvo su id
		if (rs != null && rs.next()) {
			return rs.getInt("id");
		} else {
			// Si no se encuentra la categoría, lanzo una excepción
			throw new SQLException("No se encuentra categoria: " + category);
		}
	}

	/**
	 * Obtiene el id de un tipo
	 * @param type tipo del que buscar el id
	 * @param conn Conexion a la BDD
	 * @return el id del tipo pasado por parámetro
	 * @throws SQLException SQLException si no se encuentra y/o falla la consulta
	 */
	private static int getTypeId(String type, Connection conn) throws SQLException {
		// Preparo la consulta para obtener el id del tipo
		query = "SELECT id FROM types WHERE type_name = ?";
		// Uso el método devolverPreparedStatement de JdbcUtils para ejecutar la
		// consulta
		ResultSet rs = JdbcUtils.devolverPreparedStatement(query, type);
		// Si se encuentra el tipo, devuelvo su id
		if (rs != null && rs.next()) {
			return rs.getInt("id");
		} else {
			// Si no se encuentra el tipo, lanzo una excepción
			throw new SQLException("No se encuentra tipo: " + type);
		}
	}

	/**
	 * Obtiene el último id de chiste de un idioma
	 * @param languageId id del idioma
	 * @param conn Conexion a la BDD
	 * @return el último id de chiste de un idioma + 1
	 */
	private static int getLastId(String languageId, Connection conn) {
		query = "SELECT MAX(id_chiste) FROM jokes WHERE id_idioma = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(languageId));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Inserta un flag en la tabla jokes_flags
	 * 
	 * @param flagStmt   query para insert
	 * @param jokeId     id del chiste
	 * @param languageId id del idioma
	 * @param flagName   nombre del flag
	 * @param conn       Conexion a la BDD
	 * @throws SQLException si peta la consulta
	 */
	private static void insertFlag(PreparedStatement flagStmt, int jokeId, int languageId, String flagName,
			Connection conn) throws SQLException {

		flagStmt.setInt(1, jokeId);
		flagStmt.setInt(2, languageId);
		flagStmt.setInt(3, getFlagId(flagName, conn));

		flagStmt.executeUpdate();
	}

	/**
	 * Obtiene el id de un flag a partir del nombre de este
	 */
	private static int getFlagId(String flagName, Connection conn) throws SQLException {
		// Preparo la consulta para obtener el id de la flag
		query = "SELECT id FROM flags WHERE flag_name = ?";
		// Uso el método devolverPreparedStatement de JdbcUtils para ejecutar la
		// consulta
		ResultSet rs = JdbcUtils.devolverPreparedStatement(query, flagName);
		// Si se encuentra la flag, devuelvo su id
		if (rs != null && rs.next()) {
			return rs.getInt("id");
		} else {
			// Si no se encuentra la flag, lanzo una excepción
			throw new SQLException("No se encuentra bandera: " + flagName);
		}
	}

	/**
	 * Inserta un chiste con Statement
	 * 
	 * @param conn Conexión a la BDD
	 */
	public static void AnadirChisteStatement(Connection conn) {
		Joke joke = obtenerDatosChiste();
		joke.setId(getLastId(String.valueOf(joke.getLanguageCode()), conn));

		// Insertar el chiste en la base de datos
		try (Statement stmt = conn.createStatement()) {
			String jokeText = joke.getType().equals("twopart") ? joke.getSetup() + ";" + joke.getDelivery()
					: joke.getSetup();
			String insertJokeSQL = String.format(
					"INSERT INTO jokes (id_chiste, id_idioma, category_id, type_id, joke) VALUES (%d, %d, %d, %d, '%s')",
					joke.getId(), joke.getLanguageCode(), JokeInserter.getCategoryId(joke.getCategory(), conn),
					JokeInserter.getTypeId(joke.getType(), conn), jokeText);
			stmt.executeUpdate(insertJokeSQL);

			JokeInserter.insertJokesFlags(joke, conn);
			System.out.println("Chiste añadido correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error al añadir el chiste.");
		}
	}
	/**
	 * Añade un chiste con PreparedStatement
	 * @param conn Conexión a BDD
	 */
	public static void AnadirChistePreparedStatement(Connection conn) {
		Joke joke = obtenerDatosChiste();
		joke.setId(getLastId(String.valueOf(joke.getLanguageCode()), conn));

		// Insertar el chiste en la base de datos
		String insertJokeSQL = "INSERT INTO jokes (id_chiste, id_idioma, category_id, type_id, joke) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(insertJokeSQL)) {
			String jokeText = joke.getType().equals("twopart") ? joke.getSetup() + ";" + joke.getDelivery()
					: joke.getSetup();
			stmt.setInt(1, joke.getId());
			stmt.setInt(2, joke.getLanguageCode());
			stmt.setInt(3, getCategoryId(joke.getCategory(), conn));
			stmt.setInt(4, getTypeId(joke.getType(), conn));
			stmt.setString(5, jokeText);
			stmt.executeUpdate();

			JokeInserter.insertJokesFlags(joke, conn);
			System.out.println("Chiste añadido correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error al añadir el chiste.");
		}
	}

	/**
	 * Recoge los Todos los datos para agregar una joke
	 * @return Joke La broma creada
	 */
	private static Joke obtenerDatosChiste() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		List<String> categories = List.of("Any", "Misc", "Programming", "Dark", "Pun", "Spooky", "Christmas");
		List<String> types = List.of("single", "twopart");
		List<String> languages = List.of("English", "German", "Spanish", "French", "Portuguese", "Czech");

		System.out.println("Elige una categoría:");
		for (int i = 0; i < categories.size(); i++) {
			System.out.println((i + 1) + ". " + categories.get(i));
		}
		int categoryIndex = sc.nextInt() - 1;
		String category = categories.get(categoryIndex);

		System.out.println("Elige un tipo:");
		for (int i = 0; i < types.size(); i++) {
			System.out.println((i + 1) + ". " + types.get(i));
		}
		int typeIndex = sc.nextInt() - 1;
		String type = types.get(typeIndex);

		System.out.println("Elige un idioma:");
		for (int i = 0; i < languages.size(); i++) {
			System.out.println((i + 1) + ". " + languages.get(i));
		}
		int languageIndex = sc.nextInt() - 1;
		int languageCode = languageIndex + 1;

		sc.nextLine(); // Consumir el salto de línea

		System.out.println("Introduce el texto del chiste:");
		String setup = sc.nextLine();
		String delivery = "";
		if (type.equals("twopart")) {
			System.out.println("Introduce el texto de la segunda parte del chiste:");
			delivery = sc.nextLine();
		}

		System.out.println("El chiste es NSFW? (true/false):");
		boolean nsfw = sc.nextBoolean();
		System.out.println("El chiste es religioso? (true/false):");
		boolean religious = sc.nextBoolean();
		System.out.println("El chiste es político? (true/false):");
		boolean political = sc.nextBoolean();
		System.out.println("El chiste es racista? (true/false):");
		boolean racist = sc.nextBoolean();
		System.out.println("El chiste es sexista? (true/false):");
		boolean sexist = sc.nextBoolean();
		System.out.println("El chiste es explícito? (true/false):");
		boolean explicit = sc.nextBoolean();

		return new Joke(0, category, type, setup, delivery, "",
				new Joke.Flags(nsfw, religious, political, racist, sexist, explicit), true, "", languageCode);
	}

	/**
	 * Busca chistes que contengan el texto pasado por parámetros Imprime todos los
	 * chistes que contengan esa palabra
	 * 
	 * @param conn Conexion BDD
	 * @param textoBusqueda EL texto a buscar
	 */
	public static void buscarChistesPorTexto(Connection conn, String textoBusqueda) {
        String query = "buscar_chistes_por_texto(?)";
        ResultSet rs = JdbcUtils.resultSetCallableStatement(query, textoBusqueda);
        processResultSet(rs, textoBusqueda);
    }


	/**
	 * Busca chistes que no tengan flags llamando a la función buscar_chistes_sin_flags en la bdd
	 * @param conn Conexión a la BDD
	 */
    public static void buscarChistesSinFlags(Connection conn) {
        String query = "obtener_chistes_sin_flags()";
        ResultSet rs = JdbcUtils.resultSetCallableStatement(query);
        processResultSet(rs, null);
    }

    /**
     * Procesa el resultado del resultSet y muestras las jokes por pantalla.
     *
     * @param rs            La result set a procesar
     * @param textoBusqueda El texto de búsqueda a buscar (null si no se ha buscado)
     */
    private static void processResultSet(ResultSet rs, String textoBusqueda) {
        try {
            if (rs != null) {
                while (rs.next()) {
                    int idChiste = rs.getInt("id_chiste");
                    int idIdioma = rs.getInt("id_idioma");
                    int categoryId = rs.getInt("category_id");
                    int typeId = rs.getInt("type_id");
                    String joke = rs.getString("joke");
                    System.out.printf("ID: %d, Idioma: %d, Categoría: %d, Tipo: %d, Chiste: %s%n",
                            idChiste, idIdioma, categoryId, typeId, joke);
                }
            } else if (textoBusqueda != null) {
                System.out.println("No hay jokes que conetengan " + textoBusqueda);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        textoBusqueda = null;
    }

}
