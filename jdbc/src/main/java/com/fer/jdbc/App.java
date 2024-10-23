package com.fer.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fer.jdbc.entidades.Evento;
import com.fer.jdbc.utilidades.JdbcUtils;

/**
 * Hello world!
 *
 */




public class App {
	static String url = "jdbc:postgresql://localhost:5432/eventos";
	static String usuario = "postgres";
	static String password = "postgres";

	public static void main(String[] args) {
		//ejemplo1();
		
		
		if(JdbcUtils.conexionBdd(url, usuario, password)) {
			ResultSet rs = JdbcUtils.devolverConsulta("select * from evento");
			try {
				while(rs.next()) {
					System.out.println(rs.getObject(1));
				}
				//Compruebo que funciona el ejecutar una DML
				System.out.println("Registros modificados: " + JdbcUtils.ejecutarDML("Insert into evento(nombre,descripcion,precio,fecha) values('Evento4','Descripcion4','10','2024-10-23') "));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		JdbcUtils.cerrarBdd();
			
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
	private static List<Evento> ejemplo2(){ //Carga todos los eventos en una lista y la devuelve
		List<Evento> eventos = new ArrayList<Evento>();
		Connection con = null;
		ResultSet rs = null;
		try {
			con = DriverManager.getConnection(url, usuario, password);
			Statement statement = con.createStatement();
			String sentenciaSQL = "SELECT * FROM evento ORDER BY fecha";
			rs = statement.executeQuery(sentenciaSQL);
			while (rs.next()) { // en la result set, la recorre y crea un evento para cada
				eventos.add(new Evento(rs.getInt(1)
						,rs.getString(2)
						,rs.getString(3)
						,rs.getDouble(4)
						,rs.getDate(5).toLocalDate()));
			}
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs != null && con != null && !rs.isClosed() && !con.isClosed()) {
					rs.close();
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return eventos != null? eventos:null;
		}
	}
}
