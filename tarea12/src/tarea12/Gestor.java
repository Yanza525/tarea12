package tarea12;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Gestor {
	final String URL = "jdbc:mysql://localhost:3306/Alumnos12";
	final String USUARIO = "root";
	final String CONTRASENIA = "Yanza.525";
	Scanner sc = new Scanner(System.in);
	final Connection conexion = establecerConexion();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	List<Grupo> grupitos = new ArrayList<>();
	final String sqlInsertarAlumnos = "INSERT INTO alumno (NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, GrupoID) VALUES (?,?,?,?,?,?,?,?)";

	public void eleccionMenu() {
		int opcion;
		while (true) {
			System.out.println("Elige una opcion");
			mostrarMenu();
			opcion = sc.nextInt();
			sc.nextLine();
			switch (opcion) {
			case 1 -> insertarAlumnos();
			case 2 -> insertarGrupos();
			case 3 -> mostrarAlumnos();
			case 4 -> guardarAlumnosFicheroTexto();
			case 5 -> leerAlumnosFicheroTexto();
			case 6 -> modificarNombrePorPK();
			case 7 -> eliminarAlumnoPorNia();
			case 8 -> eliminarAlumnosPorGrupo();
			case 9 -> guardarAlumnosFicheroJSON();
			case 10 -> leerAlumnosFicheroJSON();
			case 0 -> {
				if (conexion != null) {
					try {
						conexion.close();

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("Adios");
				return;
			}

			default -> System.out.println("Opcion invalida");
			}

		}
	}

	public void mostrarMenu() {
		System.out.println("1.Insertar alumnos");
		System.out.println("2.Insertar grupos");
		System.out.println("3.Mostrar alumnos");
		System.out.println("4.Guardar alumnos en fichero");
		System.out.println("5.Leer alumnos de un fichero");
		System.out.println("6.Modificar el nombre de un alumno(Por NIA)");
		System.out.println("7.Eliminar alumno (Por NIA)");
		System.out.println("8.Eliminar alumnos (Por grupo)");
		System.out.println("9.Guardar grupos en fichero Json");
		System.out.println("10.Leer grupos de fichero Json");
		System.out.println("0. Salir");
	}

	public Connection establecerConexion() {
		Connection conexion = null;
		try {
			conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENIA);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conexion;
	}

	public void consultarGruposExistentes() {
		grupitos.clear();
		int idGrupo;
		String nombreGrupo;
		String sql = "SELECT GrupoID, NombreGrupo from Grupo";
		try {
			Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				idGrupo = rs.getInt("GrupoID");
				nombreGrupo = rs.getString("NombreGrupo");
				Grupo grupito = new Grupo(idGrupo, nombreGrupo);
				grupitos.add(grupito);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertarGrupos() {
		int cont = 0;
		System.out.println("Dime el ID del grupo");
		int idGrupo = sc.nextInt();
		sc.nextLine();
		System.out.println("Dime el nombre del grupo");
		String nombre = sc.nextLine();
		consultarGruposExistentes();
		for (Grupo grupo : grupitos) {
			if (grupo.getGrupoID() == idGrupo) {
				cont++;
			}
		}
		if (cont == 0) {
			Grupo grupoInsertar = new Grupo(idGrupo, nombre);
			grupitos.add(grupoInsertar);
			String sql = "INSERT INTO Grupo (GrupoID, NombreGrupo) VALUES (?,?)";
			try {
				PreparedStatement ps = conexion.prepareStatement(sql);
				ps.setInt(1, grupoInsertar.getGrupoID());
				ps.setString(2, grupoInsertar.getNombreGrupo());
				ps.executeUpdate();
				System.out.println("Grupo creado correctamente");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Tu grupo ya existe");
		}

	}

	public void insertarAlumnos() {
		System.out.println("Introduce los datos del alumno");
		System.out.println("NIA: ");
		int nia = sc.nextInt();
		sc.nextLine();
		System.out.println("Nombre: ");
		String nombre = sc.nextLine();
		System.out.println("Apellidos: ");
		String apellidos = sc.nextLine();
		System.out.println("Genero: ");
		char genero = sc.next().charAt(0);
		sc.nextLine();
		System.out.println("Fecha de nacimiento: ");
		String fechaNacimiento = sc.nextLine();
		LocalDate fechaNacimientoLD;
		try {
			// Convertimos el String en LocalDate
			fechaNacimientoLD = LocalDate.parse(fechaNacimiento, formatter);
		} catch (DateTimeParseException e) {
			System.out.println("Formato incorrecto");
			// Si el usuario inserta mal la fecha, usaremos la fecha actual.
			fechaNacimientoLD = LocalDate.now();
		}
		System.out.println("Ciclo: ");
		String ciclo = sc.nextLine();
		System.out.println("Curso: ");
		String curso = sc.nextLine();
		System.out.println("Grupo: ");

		int grupoID = sc.nextInt();
		sc.nextLine();
		Grupo grupoInsertar = new Grupo();
		consultarGruposExistentes();
		int cont = 0;
		for (Grupo grupo : grupitos) {
			if (grupo.getGrupoID() == grupoID) {
				grupoInsertar = grupo;
				Alumno alumnoInsertar = new Alumno(nia, nombre, apellidos, genero, fechaNacimientoLD, ciclo, curso,
						grupoInsertar);
				String sql = sqlInsertarAlumnos;
				try {
					PreparedStatement ps = conexion.prepareStatement(sql);
					ps.setInt(1, alumnoInsertar.getNia());
					ps.setString(2, alumnoInsertar.getNombre());
					ps.setString(3, alumnoInsertar.getApellidos());
					ps.setString(4, String.valueOf(alumnoInsertar.getGenero()));
					ps.setDate(5, java.sql.Date.valueOf(alumnoInsertar.getFechaNacimiento()));
					ps.setString(6, alumnoInsertar.getCiclo());
					ps.setString(7, alumnoInsertar.getCurso());
					ps.setInt(8, grupoID);
					ps.executeUpdate();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Alumno insertado correctamente");

			} else {
				cont++;
			}
		}
		if (cont == grupitos.size()) {
			System.out.println("Tu grupo no existe");
		}

	}

	public void mostrarAlumnos() {
		String sql = "SELECT a.Nia, a.Nombre, a.Apellidos, a.Genero, a.FechaNacimiento, a.Ciclo, a.Curso, a.GrupoID, g.NombreGrupo FROM "
				+ "alumno a JOIN Grupo g ON a.GrupoID = g.GrupoID";
		try {
			Statement st = conexion.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				System.out.println("\n Nia: " + rs.getInt("Nia") + "\n Nombre: " + rs.getString("Nombre")
						+ "\n Apellidos: " + rs.getString("Apellidos") + "\n Genero: " + rs.getString("Genero")
						+ "\n Fecha de nacimiento: " + rs.getDate("FechaNacimiento") + "\n Ciclo: "
						+ rs.getString("Ciclo") + "\n Curso: " + rs.getString("Curso") + "\n Grupo ID: "
						+ rs.getInt("GrupoID") + "\n Nombre grupo: " + rs.getString("NombreGrupo")
						+ "\n -------------------------------------------------------:)");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void guardarAlumnosFicheroTexto() {
		String sql = "SELECT NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, GrupoID FROM alumno";
		Statement st;
		BufferedWriter bw;
		try {
			st = conexion.createStatement();
			ResultSet rs = st.executeQuery(sql);
			bw = new BufferedWriter(new FileWriter("alumnitos.txt"));
			while (rs.next()) {
				bw.write(rs.getInt("NIA") + ",");
				bw.write(rs.getString("Nombre") + ",");
				bw.write(rs.getString("Apellidos") + ",");
				bw.write(rs.getString("Genero") + ",");
				bw.write(rs.getDate("FechaNacimiento") + ",");
				bw.write(rs.getString("Ciclo") + ",");
				bw.write(rs.getString("Curso") + ",");
				bw.write(rs.getString("GrupoID"));
				bw.newLine();
			}
			System.out.println("Alumnos guardados correctamente");
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void leerAlumnosFicheroTexto() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("alumnitos.txt"));
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(",");
				String sql = sqlInsertarAlumnos;
				PreparedStatement ps = conexion.prepareStatement(sql);
				ps.setInt(1, Integer.parseInt(datos[0]));
				ps.setString(2, datos[1]);
				ps.setString(3, datos[2]);
				ps.setString(4, datos[3]);
				ps.setDate(5, Date.valueOf(datos[4]));
				ps.setString(6, datos[5]);
				ps.setString(7, datos[6]);
				ps.setInt(8, Integer.parseInt(datos[7]));
				ps.executeUpdate();

			}
			System.out.println("Alumnos cargados correctamente a la base de datos");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void modificarNombrePorPK() {
		System.out.println("Dime el Nia del alumno a modificar");
		int nia = sc.nextInt();
		sc.nextLine();
		System.out.println("Dime el nuevo nombre: ");
		String nuevoNombre = sc.nextLine();
		String sql = "UPDATE alumno SET Nombre = ? WHERE Nia =? ";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setString(1, nuevoNombre);
			ps.setInt(2, nia);
			ps.executeUpdate();
			System.out.println("Nombre cambiado con exito");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void eliminarAlumnoPorNia() {
		System.out.println("Dime el Nia del alumno que deseas eliminar: ");
		int nia = sc.nextInt();
		sc.nextLine();
		String sql = "DELETE FROM alumno WHERE Nia =? ";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setInt(1, nia);
			ps.executeUpdate();
			System.out.println("Alumno eliminado corrctamente");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminarAlumnosPorGrupo() {
		consultarGruposExistentes();
		System.out.println("Grupos actuales:");
		for (Grupo grupo : grupitos) {
			grupo.toString();
		}
		System.out.println("Dime el ID del grupo para borrar los alumnos que pertenezcan a el");
		int grupoID = sc.nextInt();
		sc.nextLine();

		String sql = "DELETE FROM alumno WHERE GrupoID=?";
		try {
			PreparedStatement ps = conexion.prepareStatement(sql);
			ps.setInt(1, grupoID);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void guardarAlumnosFicheroJSON() {
		consultarGruposExistentes();
		try {
			// Configurar ObjectMapper para serializar objetos Java a JSON
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.registerModule(new JavaTimeModule());
			objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			// Iterar sobre los grupos y agregar los alumnos a cada grupo
			for (Grupo grupo : grupitos) {
				List<Alumno> alumnos = obtenerAlumnosPorGrupo(grupo); // Obtener alumnos de este grupo
				grupo.setAlumnos(alumnos); // Asignar la lista de alumnos al grupo
			}

			// Guardar los grupos con sus alumnos en un archivo JSON
			objectMapper.writeValue(new File("GruposAlumnos.json"), grupitos);

			System.out.println("Grupos y alumnos guardados en JSON con éxito.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Alumno> obtenerAlumnosPorGrupo(Grupo grupo) {
		List<Alumno> alumnos = new ArrayList<>();
		String sql = "SELECT NIA, Nombre, Apellidos, Genero, FechaNacimiento, Ciclo, Curso, GrupoID FROM Alumno WHERE GrupoID = ?";

		try (PreparedStatement ps = conexion.prepareStatement(sql)) {
			ps.setInt(1, grupo.getGrupoID()); // Filtrar por el ID del grupo
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Alumno alumno = new Alumno(rs.getInt("NIA"), rs.getString("Nombre"), rs.getString("Apellidos"),
						rs.getString("Genero").charAt(0), rs.getDate("FechaNacimiento").toLocalDate(),
						rs.getString("Ciclo"), rs.getString("Curso"), grupo);
				alumnos.add(alumno);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return alumnos;
	}

	public void leerAlumnosFicheroJSON() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // Necesario para manejar LocalDate
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Para formato ISO-8601

		try {
			// Leer archivo JSON y convertirlo en lista de grupos
			List<Grupo> grupos = mapper.readValue(new File("GruposAlumnos.json"), new TypeReference<List<Grupo>>() {
			});

			// Insertar los datos en la base de datos
			for (Grupo grupo : grupos) {
				for (Alumno alumno : grupo.getAlumnos()) {
					insertarAlumnoEnBD(alumno, grupo.getGrupoID());
				}
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo JSON: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void insertarAlumnoEnBD(Alumno alumno, int grupoID) {
		String sql = sqlInsertarAlumnos;

		try (PreparedStatement pst = conexion.prepareStatement(sql)) {
			pst.setInt(1, alumno.getNia());
			pst.setString(2, alumno.getNombre());
			pst.setString(3, alumno.getApellidos());
			pst.setString(4, String.valueOf(alumno.getGenero())); // Convertir char a String
			pst.setDate(5, java.sql.Date.valueOf(alumno.getFechaNacimiento())); // Convertir LocalDate a java.sql.Date
			pst.setString(6, alumno.getCiclo());
			pst.setString(7, alumno.getCurso());
			pst.setInt(8, grupoID);

			pst.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Error al insertar alumnos");
			e.printStackTrace();
		}
	}

}
