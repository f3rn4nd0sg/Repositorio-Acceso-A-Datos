package com.fer.jdbc.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {
	static Connection con; // variable conexion
	static ResultSet rs; // la tabla de resultados al hacer la consulta
	static Statement statement; // para hacer las consultas

	/**
	 * Dados un parametros, abre la BDD y el statement para poder hacer consultas
	 * 
	 * @param url
	 * @param usuario
	 * @param password
	 * @return true si se ha podido, false si no se ha podido
	 */
	public static boolean conexionBdd(String url, String usuario, String password) {
		try {
			con = DriverManager.getConnection(url, usuario, password);
			statement = con.createStatement();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Cierra la BDD
	 * 
	 * @return true si se ha podido cerrar, false si no
	 */
	public static boolean cerrarBdd() {
		try {
			con.close();
			statement.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Dado una query consulta BDD devuelve tabla ResultSet de la consulta o null si
	 * esta falla
	 * 
	 * @param query
	 * @return ResultSet de la consulta
	 */
	public static ResultSet devolverConsulta(String query) {
		try {

			return statement.executeQuery(query);
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Dado una query de modificación de BDD (Update, Insert, Delete), ejecuta la
	 * query y devuelve el numero de filas adectadas
	 * 
	 * @param query
	 * @return numero de filas afectadas, -1 si algo ha fallado
	 */
	public static int ejecutarDML(String query) {
		try {
			return statement.executeUpdate(query);
		} catch (SQLException e) {
			return -1;
		}
	}

}
