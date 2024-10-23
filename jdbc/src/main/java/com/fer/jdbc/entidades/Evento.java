package com.fer.jdbc.entidades;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Evento {
	private int id;
	private String nombre;
	private String descripcion;
	private Double precio;
	private LocalDate fecha;
}
