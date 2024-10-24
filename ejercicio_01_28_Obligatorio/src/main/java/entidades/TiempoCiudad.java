package entidades;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// En GSON mismo nombre de variables que en el JSON
// TODO, guardar fecha
public class TiempoCiudad {
	private String name;
	// El main ES UNA LISTA
	private mainList main;
	// El weather ES UN ARRAY
	private List<weatherArray> weather;
	private sysList sys;

	private double temp;
	private String clima;
	private double tempCelsius;
	private double humedad;
	private String pais;
	private String dt;

	// Método para asegurar que se guardan los datos en el objeto al leer desde json
	public void guardarDatosJson() {
		this.clima = (String) this.weather.get(0).getMain();
		this.tempCelsius = this.main.getTemp() - 273.15;
		this.humedad = this.main.getHumidity();
		this.pais = this.sys.getCountry();
		// Convierte el dateTime que me da en json a un formato YYYY-MM-DD
		this.dt = Instant.ofEpochSecond(Long.parseLong(dt)).atZone(ZoneId.of("UTC")).toLocalDate().toString();
	}

	// Método para leer datos desde un String XML usando DOM, TODO probar a eliminar
	// todo lo que no necesito sacar por pantalla y tener un objeto más limpio
	public static TiempoCiudad leerDesdeXML(String xmlContent)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));
		document.getDocumentElement().normalize();

		// Leer datos de la ciudad
		Element cityElement = (Element) document.getElementsByTagName("city").item(0);
		String name = cityElement.getAttribute("name");
		String pais = getElementTextContent(document, "country");

		// Leer datos de temperatura
		Element tempElement = (Element) document.getElementsByTagName("temperature").item(0);
		double tempKelvin = Double.parseDouble(tempElement.getAttribute("value"));
		double tempCelsius = tempKelvin - 273.15;

		// Leer datos de humedad
		Element humidityElement = (Element) document.getElementsByTagName("humidity").item(0);
		double humedad = Double.parseDouble(humidityElement.getAttribute("value"));

		// Leer datos dateTime
		Element dtElement = (Element) document.getElementsByTagName("lastupdate").item(0);
		String dt = dtElement.getAttribute("value");
		// Convierte la ultima actualizacion de datos (parecido al DateTime) a un
		// formato YYYY-MM-DD
		dt = dt.split("T")[0];

		// Leer datos de 'main'
		double feelsLike = Double.parseDouble(getElementAttribute(document, "feels_like", "value"));
		double tempMin = Double.parseDouble(tempElement.getAttribute("min"));
		double tempMax = Double.parseDouble(tempElement.getAttribute("max"));
		double pressure = Double.parseDouble(getElementAttribute(document, "pressure", "value"));
		mainList main = new mainList(tempKelvin, feelsLike, tempMin, tempMax, pressure, humedad, 0, 0);

		// Leer datos del clima
		NodeList weatherNodes = document.getElementsByTagName("weather");
		List<weatherArray> weather = new ArrayList<>();
		String clima = null;
		for (int i = 0; i < weatherNodes.getLength(); i++) {
			Element weatherElement = (Element) weatherNodes.item(i);
			String mainValue = weatherElement.getAttribute("value");
			String description = weatherElement.getAttribute("value");
			String icon = weatherElement.getAttribute("icon");
			weather.add(new weatherArray(0, mainValue, description, icon));
			if (i == 0) {
				clima = mainValue;
			}
		}

		// Leer datos de 'sys'
		Element sunElement = (Element) document.getElementsByTagName("sun").item(0);
		String sunrise = sunElement.getAttribute("rise");
		String sunset = sunElement.getAttribute("set");
		sysList sys = new sysList(pais, sunrise, sunset);

		return new TiempoCiudad(name, main, weather, sys, tempKelvin, clima, tempCelsius, humedad, pais, dt);
	}

	// Métodos para lectura del xml más sencillos
	private static String getElementTextContent(Document document, String tagName) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		}
		return null;
	}

	// Este igual que el de arriba
	private static String getElementAttribute(Document document, String tagName, String attributeName) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Element element = (Element) nodeList.item(0);
			return element.getAttribute(attributeName);
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("Ciudad: %s (%s)\n\nTemperatura: %.2fº (%.2fK)\n\nHumedad: %.2f %% \n\nClima: %s -%s",
				name, this.sys.getCountry(), this.main.getTemp() - 273.15, this.main.getTemp(), this.main.getHumidity(),
				this.weather.get(0).getMain(), this.weather.get(0).getDescription());
	}

}

//Clases para guardar los datos de los arrays y listas
@Data
@AllArgsConstructor
class mainList {
	private double temp;
	private double feels_like;
	private double temp_min;
	private double temp_max;
	private double pressure;
	private double humidity;
	private double sea_level;
	private double grnd_level;
}

@Data
@AllArgsConstructor
class sysList {
	private String country;
	private String sunrise;
	private String sunset;
}

@Data
@AllArgsConstructor
class weatherArray {
	private int id;
	private String main;
	private String description;
	private String icon;
}
