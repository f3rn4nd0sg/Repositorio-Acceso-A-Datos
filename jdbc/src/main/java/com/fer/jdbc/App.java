package com.fer.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fer.jdbc.entidades.Evento;
import com.fer.jdbc.utilidades.JdbcUtils;
import com.github.javafaker.Faker;

/**
 * Hello world!
 *
 */

public class App {
	static Faker faker = new Faker();
	static String url = "jdbc:postgresql://localhost:5432/eventos";
	static String usuario = "postgres";
	static String password = "postgres";

	public static void main(String[] args) {
		// ejemplo1();
		// ejemplo3();
		// JdbcUtils.cerrarBdd();
		// ejemplo4(0);
		// insertarPersonas();
		//insertarFaker();
		//ejemploCallable1();
		//llamarFunctionMedianteStatement();
		//llamarPersonasMedianteStatement();
		//llamarFunctionRegistro();
		llamarProcedureGenerico();

	}

	public static void ejemplo1() {
		try {
			String url = "jdbc:postgresql://localhost:5432/eventos";
			String usuario = "postgres";
			String password = "postgres";
			Connection con = DriverManager.getConnection(url, usuario, password);
			Statement statement = con.createStatement();
			String sentenciaSQL = "SELECT nombre,descripcion FROM evento ORDER BY fecha";
			ResultSet rs = statement.executeQuery(sentenciaSQL);
			System.out.println("nombre" + "\t" + "descripcion");
			System.out.println("-----------------------------------------");
			while (rs.next()) { // en la result set, la recorre y la imprime
				System.out.println(rs.getString(1) + "\t " + rs.getString(2));
			}
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "finally", "unused" })
	private static List<Evento> ejemplo2() { // Carga todos los eventos en una lista y la devuelveç
		List<Evento> eventos = new ArrayList<Evento>();
		Connection con = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url, usuario, password);
			Statement statement = con.createStatement();
			String sentenciaSQL = "SELECT * FROM evento ORDER BY fecha";
			rs = statement.executeQuery(sentenciaSQL);
			while (rs.next()) { // en la result set, la recorre y crea un evento para cada
				eventos.add(new Evento(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4),
						rs.getDate(5).toLocalDate()));
			}
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null && con != null && !rs.isClosed() && !con.isClosed()) {
					rs.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return eventos != null ? eventos : null;
		}
	}

	public static void ejemplo3() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Introduzca el id a buscar");
		String id = sc.nextLine();

		JdbcUtils.conexionBdd(url, usuario, password);

		ResultSet rs = JdbcUtils.devolverConsulta("Select * from evento where id=" + id);

		try {
			while (rs.next()) {
				System.out.println(rs.getObject(1));
			}
		} catch (SQLException e) {
			System.out.println("Ha petado");
		}

		sc.close();
	}

	// Ejemplo de select con preparedStatemente, no se come el SQL injection
	public static void ejemplo4(int id) {
		try {
			Connection con = DriverManager.getConnection(url, usuario, password);
			PreparedStatement stmt = con.prepareStatement("select * from evento where id = ?");
			// aqui le dice que al primer interrogante, ponga el 3
			stmt.setObject(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getInt(1) + " " + rs.getString(2));
			}
		} catch (SQLException e) {

		}
	}

	public static void insertarPersonas() {
		String sql = "INSERT INTO personas(nombre,email) " + "VALUES" + "('Persona1','email1@gmail.com'),"
				+ "('Persona2','email2@gmail.com')," + "('Persona3','email3@gmail.com'),"
				+ "('Persona4','email4@gmail.com')," + "('Persona5','email5@gmail.com')";
		JdbcUtils.conexionBdd(url, usuario, password);
		JdbcUtils.ejecutarDML(sql);
		System.out.println("Inserción Realizada con éxito");
		JdbcUtils.cerrarBdd();
	}

	public static void insertarFaker() {
		String sql = "INSERT INTO personas(nombre, email) VALUES ";

		for (int i = 0; i < 499; i++) {
			sql += "('" + faker.name().firstName().replace("'", "") + "', '" + faker.internet().emailAddress() + "'), ";
		}
		sql += "('" + faker.name().firstName().replace("'", "") + "', '" + faker.internet().emailAddress() + "')";

		JdbcUtils.conexionBdd(url, usuario, password);
		JdbcUtils.ejecutarDML(sql);
		System.out.println("Inserción faker realizada con éxito");

		JdbcUtils.cerrarBdd();
	}

	/**
	 * Este llama a la function directamente
	 */
	public static void ejemploCallable1() {
		try {
			Connection con = DriverManager.getConnection(url, usuario, password);

			CallableStatement cStmt = con.prepareCall("{call cantidadpersonas(?)}");
			cStmt.registerOutParameter(1, Types.INTEGER);
			cStmt.setString(1, "%Bayer%");
			cStmt.execute();
			int resultado = cStmt.getInt(1);
			System.out.println("Resultado: " + resultado);

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Este llama a la function mediante un select y el statement, más comodo (personalmente)
	 */
	public static void llamarFunctionMedianteStatement() {
		try {
			JdbcUtils.conexionBdd(url, usuario, password);
			ResultSet rs = JdbcUtils.devolverConsulta("Select * from cantidadpersonas('%Fer%')");
			while(rs.next())
				System.out.println(rs.getInt(1));
			JdbcUtils.cerrarBdd();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void llamarPersonasMedianteStatement() {
		try {
			JdbcUtils.conexionBdd(url, usuario, password);
			ResultSet rs = JdbcUtils.devolverConsulta("Select * from devolver_cantidad_personas()");
			while(rs.next())
				System.out.println(rs.getInt(1));
			JdbcUtils.cerrarBdd();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void llamarFunctionRegistro() {
		try {
			Connection con = DriverManager.getConnection(url, usuario, password);
			CallableStatement stmt = con.prepareCall("{call listar_personas_out()}");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				System.out.println("ID: " + rs.getObject(1) + " Nombre: " + rs.getObject(2));
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void llamarProcedureGenerico() {
		JdbcUtils.conexionBdd(url, usuario, password);
		int numero = (int) JdbcUtils.ejecutarCallableStatement("cantidadpersonas(?)", "%Bayer%");
		System.out.println("La cantidad de personas es: " +numero);
		JdbcUtils.cerrarBdd();
	}

}
