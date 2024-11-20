package com.fer.jdbc.utilidades;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fer.Ejercicio_02_15_Obligatorio.App;
import com.fer.Ejercicio_02_15_Obligatorio.entidades.Joke;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class UtilsAuxiliar {
	public static String leerDesdeFichero(String fileName) {
		StringBuilder content = new StringBuilder();

		// Usar el ClassLoader para cargar el archivo desde resources
		try (InputStream inputStream = App.class.getResourceAsStream(fileName);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (Exception e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
			return null;
		}
			
		return content.toString();
	}

	public static List<Joke> leerchistesDesdeJson(String jsonString) {
	    try {
	        JsonObject rootObject = JsonParser.parseString(jsonString).getAsJsonObject();
	        JsonArray jokesArray = rootObject.getAsJsonArray("jokes");
	        Type jokeListType = new TypeToken<List<Joke>>() {}.getType();
	        return new Gson().fromJson(jokesArray, jokeListType);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}
