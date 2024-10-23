package com.fer.xmlYjson;

import java.util.ArrayList;
import java.util.List;

import JsonUtils.JsonUtils;
import XmlUtils.InternetUtils;
import entidades.Film;
import entidades.People;

/**
 * Hello world!
 *
 */

public class App {
	public static void main(String[] args) {
		// XmlUtils.procesarXmlSax(); // Solamente para archivos extremadamente grandes

		// pruebasInternetUtils();
		pruebasJson();
	}

	public static void pruebasInternetUtils() {

		// Este de abajo no hace falta token para la API
		// System.out.println(InternetUtils.readUrl("https://swapi.dev/api/starships/9/?format=json"));

		// Este de abajo si hace falta token para la API
		System.out
				.println(InternetUtils.readUrlList("https://api.football-data.org/v4/teams/86/matches?status=SCHEDULED",
						"a01fd77280894ede8f41bbb380be14de"));
	}

	public static void pruebasJson() {
		// JsonUtils.leerJsonDesdeFichero("C:/ficheros/profesor.json");
		// JsonUtils.leerLuke("C:/ficheros/luke.json");
		// JsonUtils.devolverTareasInternet("https://jsonplaceholder.typicode.com/todos").stream().filter(e->e.isCompleted()==true).forEach(e->System.out.println(e));
		// System.out.println(JsonUtils.leerStarWars("https://swapi.dev/api/people/2"));
		// Este metodo es una pasada, le pasas la url y la clase que es el bicho
		// System.out.println(JsonUtils.leerGenerico("https://swapi.dev/api/films/1",Film.class));

		// Coger todas las películas
		List<Film> peliculas = new ArrayList<Film>();
		for (int i = 1; i < 7; i++) {
			// LE PASO UNA URL Y UNA CLASE Y LO PARSEA SOLO :0
			peliculas.add(JsonUtils.leerGenerico("https://swapi.dev/api/films/ " + i + "/?format=json", Film.class));
		}
		peliculas.forEach(e -> System.out.println(e));

		peliculas.stream()
	    .forEach(e -> {
	        System.out.println("\n\n" + e.getTitle());
	        System.out.println("-------------------------");
	        e.getCharacters().forEach(p -> {
	            People people = JsonUtils.leerGenerico(p + "/?format=json", People.class);
	            System.out.println(people.getName());
	        });
	    });

	}
}
