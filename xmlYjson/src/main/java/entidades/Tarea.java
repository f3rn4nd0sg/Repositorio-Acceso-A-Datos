package entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tarea {

	private long userId;
	private long id;
	private String title;
	private boolean completed;
	
	public boolean isCompleted() {
		return completed;
	}
}
