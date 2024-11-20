package com.fer.Ejercicio_02_15_Obligatorio.entidades;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Joke {
    private int id;
    private String category;
    private String type;
    private String setup;
    private String delivery;
    private String joke;
    private Flags flags;
    private boolean safe;
    private String lang;
    private int languageCode;

    @Data
    @AllArgsConstructor
    public static class Flags {
        private boolean nsfw;
        private boolean religious;
        private boolean political;
        private boolean racist;
        private boolean sexist;
        private boolean explicit;
    }
	
	public void setLanguage() {
		switch (languageCode) {
		case 1:
			lang = "en";
			break;
		case 2:
			lang = "de";
			break;
		case 3:
			lang = "es";
			break;
		case 4:
			lang = "fr";
			break;
		case 5:
			lang = "pt";
			break;
		case 6:
			lang = "cs";
			break;
		default:
			lang = "unknown";
			break;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void setLanguageCode(int languageCode) {
        this.languageCode = languageCode;
        setLanguage();
    }
}
