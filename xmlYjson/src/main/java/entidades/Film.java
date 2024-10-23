package entidades;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Film {

	private String title;
	private long episode_id;
	private String opening_crawl;
	private List<String> characters;
	
	public String toString() {
		return "Titulo: " + this.title + " \nEpisodio: " + this.episode_id + "\nOpening Crawl: " + this.opening_crawl + "\nPersonajes: " + this.characters.toString();
	}
}
