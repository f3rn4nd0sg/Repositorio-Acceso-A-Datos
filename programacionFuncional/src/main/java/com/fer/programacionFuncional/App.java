package com.fer.programacionFuncional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fer.programacionFuncional.entidades.Usuario;

/**
 * Hello world!
 *
 */
public class App {

	public static List<Usuario> usuarios = new ArrayList<Usuario>();

	public static void tearUp() {
		usuarios.add(new Usuario(1, "Fer", 50000));
		usuarios.add(new Usuario(2, "Fer", 30000));
		usuarios.add(new Usuario(3, "Paco", 20000));
		usuarios.add(new Usuario(4, "Dani", 40000));
		usuarios.add(new Usuario(5, "Jose", 80000));
	}

	public static void tearDown() {
		usuarios.clear();
	}

	public static void forEach() {
		System.out.println("Sin stream");
		usuarios.forEach(e -> System.out.println(e));
		System.out.println("Con stream");
		usuarios.stream().forEach(e -> System.out.println(e));
	}

	public static void map() {
		// Extraigo de cada objeto el nombre y lo pongo en una lista teniendo el mismo
		// orden y todo
		// a partir de una lista de usuarios, crea una lista con sus nombres
		List<String> nombres = usuarios.stream().map(e -> e.getNombre()).collect(Collectors.toList());
		nombres.stream().forEach(e -> System.out.println(e));

		System.out.println("----------------------------------");

		// Imprime por pantalla todos los usuarios que su nombre tiene más de 4
		// caracteres
		List<Integer> letrasNombre = usuarios.stream().map(e -> e.getNombre().length()).filter(e -> e >= 4)
				.collect(Collectors.toList());

		letrasNombre.stream().forEach(e -> System.out.println(e));

		System.out.println("----------------------------------");

		// Imprime por pantalla todos los usuarios que ganen más de 30K
		usuarios.stream().filter(e -> e.getSueldo() > 30000).map(e -> e.getNombre()).collect(Collectors.toList())
				.forEach(e -> System.out.println(e));

	}

	public static void filter() {
		// No final
		// Personas que ganan más de 50000 por pantalla
		/*
		 * usuarios.stream() .filter(e->e.getSueldo() > 50000)
		 * .forEach(e->System.out.println(e));
		 */
		// Crear Lista de las Personas que ganan más de 50000 en una lista
		List<Usuario> usuariosRicos = usuarios.stream().filter(e -> e.getSueldo() > 50000).collect(Collectors.toList());

		usuariosRicos.stream().forEach(e -> System.out.println(e));
		List<Usuario> usuariosRicos2 = new ArrayList<Usuario>();

		usuarios.stream().filter(e -> e.getSueldo() > 50000).forEach(e -> usuariosRicos2.add(e));
	}

	public static void sumAvg() {
		// Cuanto nos gastamos en sueldos de los empleados

		Double sumaSueldos = usuarios.stream().mapToDouble(e -> e.getSueldo()).sum();

		OptionalDouble mediaSueldos = usuarios.stream().mapToDouble(e -> e.getSueldo()).average();

		Double mediaSueldos2 = usuarios.stream().mapToDouble(e -> e.getSueldo()).average().orElse(-1);

		System.out.println("La suma de los salarios es " + sumaSueldos);
		System.out.println("La media de los salarios es " + mediaSueldos.getAsDouble());
		System.out.println("La media de los salarios es " + mediaSueldos2);

	}

	public static void find() {
		Usuario usuarioGanaPoco = usuarios.stream().filter(e -> e.getSueldo() < 50000).findFirst()
				.orElse(new Usuario(-1, "Pobre", 0));

		System.out.println(usuarioGanaPoco);

		Usuario usuarioGanaPocoAlAzar = usuarios.stream().filter(e -> e.getSueldo() < 50000).findAny()
				.orElse(new Usuario(-1, "Pobre", 0));

		System.out.println(usuarioGanaPocoAlAzar);
	}

	public static void flatMap() {
		// A partir de una lista de listas las fusiona

		List<String> alumnosDam = new ArrayList<String>(Arrays.asList("Fran", "Paco", "Daniel"));
		List<String> alumnosDaw = new ArrayList<String>(Arrays.asList("Adrián", "Federico", "Manuel"));
		List<List<String>> alumnos = new ArrayList<List<String>>(Arrays.asList(alumnosDam, alumnosDaw));

		alumnos.stream().filter(e -> e.size() > 2).flatMap(e -> e.stream()) // Junta las listas en una sola
				.sorted().forEach(e -> System.out.println(e));
	}

	public static void peek() {
		// Igual que el forEach pero para operaciones intermedias Ej el forEach va a
		// final mientras que el peek va entre medias

		List<Usuario> pobres = usuarios.stream().filter(e -> e.getSueldo() < 20000)
				.peek(e -> e.setSueldo(e.getSueldo() + 1000)).collect(Collectors.toList());

		pobres.forEach(e -> System.out.println(e));
	}

	public static void partitionigBy() {
		// Final
		// Parte la lista original en dos sublistas. Una que cumple la condición de
		// partición y otra que no.
		Map<Boolean, List<Usuario>> sublistas = usuarios.stream()
				.collect(Collectors.partitioningBy(e -> e.getSueldo() > 10000));

		// Los que ganan +10000
		System.out.println("Usuarios que ganan más de 10000 Euros:");
		sublistas.get(true).forEach(e -> System.out.println(e));
		// Los que ganan -10000
		System.out.println("Usuarios que ganan más de 10000 Euros:");
		sublistas.get(false).forEach(e -> System.out.println(e));
	}

	public static void count() {
		long sueldoNegativo = usuarios.stream().filter(e -> e.getSueldo() < 0).count();
		System.out.println("Numeor de empleados con suedo negativo: " + sueldoNegativo);
	}

	public static void skipYLimit() {
		// Skip salta un numero de resultados
		// Limit limita el numero de elementos resueltos

		List<Usuario> usuarios345 = usuarios.stream().sorted(Comparator.comparingDouble(Usuario::getSueldo).reversed())
				.skip(2).limit(3).collect(Collectors.toList());

		usuarios345.forEach(e -> System.out.println(e));
	}

	public static void maxYmin() {
		Optional<Usuario> masgana = usuarios.stream().max(Comparator.comparingDouble(Usuario::getSueldo));
		if (masgana.isPresent()) {
			System.out.println(masgana.get().toString());
		}

		Usuario menosgana = usuarios.stream().min(Comparator.comparingDouble(Usuario::getSueldo))
				.orElse(new Usuario(1, "Fran", 0));

		System.out.println(menosgana);

	}

	public static void distinct() {
		long idsdistintos = usuarios.stream().distinct().count();

		System.out.println("Numero de usuarios con id distintos: " + idsdistintos);

		long nombresdistintos = usuarios.stream().map(e -> e.getNombre()).distinct().count();

		System.out.println("Usuarios con nombres distintos: " + nombresdistintos);
	}

	public static void match() {

		// Alguien gana mas de 100000?
		boolean ganaMas100000 = usuarios.stream().anyMatch(e -> e.getSueldo() > 100000);

		System.out.println(ganaMas100000);

		// Todos ganan más de 0)
		boolean ganaMas0 = usuarios.stream().allMatch(e -> e.getSueldo() > 0);

		System.out.println(ganaMas0);

		// Nadide gana menos de 0?
		boolean ganaMas0b = usuarios.stream().noneMatch(e -> e.getSueldo() < 0);

		System.out.println(ganaMas0b);
	}

	public static void offtopic1() {
		// Tabla de multiplicar del 7
		IntStream.rangeClosed(1, 10).forEach(e -> System.out.println(e + "*7 = " + e * 7));
	}

	public static void summarizingDouble() {

		// Recolecciona todas las estadisticas de un campo numérico

		DoubleSummaryStatistics estadisticas = usuarios.stream()
				.collect(Collectors.summarizingDouble(Usuario::getSueldo));
		
		System.out.println("Media: " + estadisticas.getAverage());
		System.out.println("Minimo: " + estadisticas.getMin());
		System.out.println("Maximo: " + estadisticas.getMax());
		System.out.println("Suma: " + estadisticas.getSum());
		System.out.println("Cuenta: " + estadisticas.getCount());
	}
	
	public static void reduce() {
		//Reduce: Reduce los datos que tengamos a un ÚNICO valor
		double multipicSueldos = usuarios.stream()
				.mapToDouble(e->e.getSueldo())
				.reduce(1, (a,b) -> a*b);
		
		System.out.println(multipicSueldos);
		
		double multiplicLetrasNombres = usuarios.stream()
				.mapToInt(e->e.getNombre().length())
				.reduce(1, (a,b) -> a*b);
		
		System.out.println(multiplicLetrasNombres);
	}
	
	public static void joining() {
		String nombreSeparados = usuarios.stream()
				.map(e->e.getNombre().toLowerCase())
				.distinct()
				.sorted()
				.collect(Collectors.joining(", ")).toString();
		System.out.println(nombreSeparados);
	}
	

	public static void main(String[] args) {
		tearUp();
		// forEach();
		// filter();
		// map();
		// sumAvg();
		// find();
		// flatMap();
		// peek();
		// count();
		// skipYLimit();
		// maxYmin();
		// distintct();
		// match();
		//offtopic1();
		//summarizingDouble();
		//reduce();
		//joining();
	}
}
