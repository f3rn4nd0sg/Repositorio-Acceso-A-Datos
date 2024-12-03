 package entidades;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

//Solo me quedo con los atributos que necesito 
@Data
public class TiempoCSV {
	private String fechaHora;
	private double temperatura;

	// Pillar fecha hora hacerle split y meterlos a estos atributos para que no me
	// de problemmas
	private String fecha;
	private String hora;

	// Lee la línea super tocha, la splitea por comas y asigna los atributos que
	// quiero

	public static TiempoCSV tiempoDesdeCSV(String linea) {
		String[] lineaPartida = linea.split(",");

		TiempoCSV tiempoCSV = new TiempoCSV();

		// Elimino la parte innecesaria de la fecha rara, los segundos tampoco hacen
		// falta
		tiempoCSV.fechaHora = lineaPartida[5].replace(":00 +0000 UTC", "");
		tiempoCSV.temperatura = Double.parseDouble(lineaPartida[6]);

		tiempoCSV.fecha = tiempoCSV.fechaHora.substring(0, 10);
		tiempoCSV.hora = tiempoCSV.fechaHora.substring(11, 16);

		return tiempoCSV;
	}

	// Método para leer todo el csv
	public static ArrayList<TiempoCSV> leerCSV(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			ArrayList<TiempoCSV> listaTiemposCSV = new ArrayList<TiempoCSV>();
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				TiempoCSV tiempo = tiempoDesdeCSV(line);
				listaTiemposCSV.add(tiempo);
			}
			System.out.println("Se ha leido bien el fichero");
			return listaTiemposCSV;
		} catch (IOException e) {
			// Devuelve nulo por si acaso peta al leer
			System.out.println("Error al leer fichero :(");
			return null;
		}
	}

	// Transforma las fechas pasadas a LocalDateTime, compara en
	// toda la lista de datos si el dato está correspondido entre las fechas
	// Construye macroString con todas las fechas que coincide, cuando termina con
	// los datos de una fecha, \n y así con las siguientes
	// No muy eficiente 

	public static void imprimirEvolucionTemperatura(List<TiempoCSV> datos, String fechaInicioStr,
			String fechaFinalStr) {
		// Formateadores de fecha y hora
		DateTimeFormatter fechaHoraFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateTimeFormatter fechaSalidaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Convertir las cadenas de fecha de inicio y final a objetos LocalDateTime
		LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr + " 00:00", fechaHoraFormatter);
		LocalDateTime fechaFinal = LocalDateTime.parse(fechaFinalStr + " 23:59", fechaHoraFormatter);

		// StringBuilder para construir la salida
		StringBuilder salida = new StringBuilder();
		String fechaActual = "";

		// Iterar a través de cada intervalo de 3 horas entre fechaInicio y fechaFinal
		for (LocalDateTime fechaHora = fechaInicio; !fechaHora.isAfter(fechaFinal); fechaHora = fechaHora
				.plusHours(3)) {
			String nuevaFecha = fechaHora.format(fechaSalidaFormatter);
			String horaFormato = fechaHora.format(DateTimeFormatter.ofPattern("HH:mm"));

			// Agregar una nueva línea si la fecha cambia
			if (!fechaActual.equals(nuevaFecha)) {
				if (salida.length() > 0) {
					salida.append("\n");
				}
				salida.append(nuevaFecha).append("  ");
				fechaActual = nuevaFecha;
			}

			// Buscar la temperatura correspondiente a la fecha y hora actual
			for (TiempoCSV tiempo : datos) {
				try {
					// Verifica si la fecha y hora coincide con la fechaHora actual
					if (tiempo.getFecha().equals(fechaHora.toLocalDate().toString()) && tiempo.getHora().equals(horaFormato)) {
						// Agrega hora y la temperatura al StringBuilder
						salida.append(horaFormato).append("->").append(String.format("%.2f", tiempo.getTemperatura()))
								.append(" ");
						// Sale del bucle
						break;
					}
				} catch (Exception e) {
					System.out.println("Error al procesar el registro: " + tiempo.getFechaHora());
				}
			}
		}

		// Imprime el macroString formado con todas las fechas
		System.out.println(salida.toString().trim());
	}

}
