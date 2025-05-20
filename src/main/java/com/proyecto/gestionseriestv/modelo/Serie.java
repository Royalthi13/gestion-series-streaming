package com.proyecto.gestionseriestv.modelo;

public class Serie {
	private int id;
	private String titulo;
	private String genero;
	private int numeroTemporadas;
	private int año;
	private Plataforma plataforma;


public Serie(int id, String titulo, String genero, int numeroTemporadas, int año, Plataforma plataforma) {
	this.id = id;
	this.titulo = titulo;
	this.genero = genero;
	this.numeroTemporadas = numeroTemporadas;
	this.año = año;
	this.plataforma = plataforma;
}
//Getter y setters
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitulo() {
	return titulo;
}
public void setTitulo(String titulo) {
	this.titulo = titulo;
}
public String getGenero() {
	return genero;
}
public void setGenero(String genero) {
	this.genero = genero;
}
public int getNumeroTemporadas() {
	return numeroTemporadas;
}
public void setNumeroTemporadas(int numeroTemporadas) {
	this.numeroTemporadas = numeroTemporadas;
}
public int getAño() {
	return año;
}
public void setAño(int año) {
	this.año = año;
}
public Plataforma getPlataforma() {
	return plataforma;
}
public void setPlataforma(Plataforma plataforma) {
	this.plataforma = plataforma;
}
@Override
public String toString() {
	return "Plataforma [id=" + id + ", titulo=" + titulo + ", genero=" + genero + ", numeroTemporadas="
			+ numeroTemporadas + ", año=" + año + ", plataforma=" + plataforma + "]";
}


}
