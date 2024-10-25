package com.fer.jdbc.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JdbcUtils {
	static Connection con; // variable conexion
	static ResultSet rs; // la tabla de resultados al hacer la consulta
	static Statement statement; // para hacer las consultas
	static PreparedStatement stmt; //para hacer consultas seguras

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
	 * esta falla, Ejemplo de SQL injection. Introduce 1000 or 1=1 y te devuelve todo :(
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
	
	/**
	 * Método genérico que le pasas una sql preparedStatement y tantos atributos como interrogantes hay en la sql
	 * @param sql query de sql
	 * @param parameters Tantos parametros como interrogantes tenga la sql
	 * @return ResultSet de la preparedStatement
	 */
	public static ResultSet devolverPreparedStatement(String sql,Object...parameters) { //Este object es como un array indefinido de atributos
		return devolverPreparedStatement(sql, Arrays.asList(parameters));		
	}
	
	/**
	 * Método genérico que le pasas una sql preparedStatement y una lista de atributos donde sustituir los datos en los interroganres de la sql
	 * @param sql query de sql
	 * @param parameters Lista de parametros
	 * @return ResultSet de la preparedStatement
	 */
	public static ResultSet devolverPreparedStatement(String sql,List<Object> parameters) {
		if(parameters.size() != countMatches(sql, '?'))
			return null;
		else {
			try {
				stmt = con.prepareStatement(sql);
				for(int i = 0; i < parameters.size();i++) {
					stmt.setObject(i+1, parameters.get(i));
				}
				return stmt.executeQuery(sql);
			} catch (SQLException e) {
				return null;
			}
		}
		
	}
	
	/**
	 * Dado un string y un caracter a buscar, devuelve cuantas veces aparece este en el string
	 * @param cadena
	 * @param caracterBuscado
	 * @return Número de veces que aparece el  caracter
	 */
	private static int countMatches(String cadena,char caracterBuscado) {
		return (int)cadena.chars().filter(e->e==caracterBuscado).count();
	}

}
