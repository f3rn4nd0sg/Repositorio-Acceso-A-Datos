package entidades;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class People {

	private String name;
	private String height;
	private List<String> films;
	
	@Override
	public String toString() {
		return "Nombre: " + this.name + " \nAltura: " + this.height + "\nPelículas: " + films.toString();
	}
	
}
