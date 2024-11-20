package tarea12;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Alumno {
	private int nia;
	private String nombre;
	private String apellidos;
	private char genero;
	private LocalDate fechaNacimiento;
	private String ciclo;
	private String curso;
	@JsonIgnore
	private Grupo grupo;

	public Alumno() {
	}

	public Alumno(int nia, String nombre, String apellidos, char genero, LocalDate fechaNacimiento, String ciclo,
			String curso, Grupo grupo) {
		this.nia = nia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.ciclo = ciclo;
		this.curso = curso;
		this.grupo = grupo;
	}

	public int getNia() {
		return nia;
	}

	public void setNia(int nia) {
		this.nia = nia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public char getGenero() {
		return genero;
	}

	public void setGenero(char genero) {
		this.genero = genero;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCiclo() {
		return ciclo;
	}

	public void setCiclo(String ciclo) {
		this.ciclo = ciclo;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	@Override
	public String toString() {
		return "Alumno [NIA=" + nia + ", Nombre=" + nombre + ", Apellidos=" + apellidos + ", Género=" + genero
				+ ", Fecha Nacimiento=" + fechaNacimiento + ", Ciclo=" + ciclo + ", Curso=" + curso + ", Grupo="
				+ grupo.getNombreGrupo() + "]";
	}
}
