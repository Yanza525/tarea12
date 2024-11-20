package tarea12;

import java.util.List;

public class Grupo {
	// Como esta es la PK no va a haber problemas con los nulos
	private int grupoID;
	private String nombreGrupo;
	private List<Alumno> alumnos;

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public Grupo() {
	}

	public Grupo(int id, String nombre) {
		this.grupoID = id;
		this.nombreGrupo = nombre;
	}

	public Grupo(int id) {
		this.grupoID = id;
	}

	public int getGrupoID() {
		return grupoID;
	}

	public void setGrupoID(int grupoID) {
		this.grupoID = grupoID;
	}

	public String getNombreGrupo() {
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}

	@Override
	public String toString() {
		return "GrupoID: " + grupoID + ", Nombre grupo: " + nombreGrupo;
	}

}
