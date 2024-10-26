package entidades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * Clase NasaImage, al llamar a la API, devuelve cada día los datos de la foto de astronomía del día (Astronomy Picture of the Day)
 * como su fecha, explicación, título y url para poder verla
 */
public class NasaImage {
	private String date;
	private String explanation;
	private String title;
	private String url;
	
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Convierte el objeto de vuelta a JSON con formato bonito
        String prettyJson = gson.toJson(this);
        return prettyJson;
	}
}
