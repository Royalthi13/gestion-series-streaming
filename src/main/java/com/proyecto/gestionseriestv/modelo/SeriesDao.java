package com.proyecto.gestionseriestv.modelo;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SeriesDao {
	private GestorBaseDatos gestorDB;

	public SeriesDao(GestorBaseDatos gestorDB) {
		this.gestorDB = gestorDB;
	}

	public boolean registrar(Serie serie) {
		String sql = "INSERT INTO SERIE(ID,TITULO,GENERO,NUM_TEMPORADAS,AÑO_LANZAMIENTO,ID_PLATAFORMA) values(?,?,?,?,?,?)";
		boolean flag = false;
		try (Connection conect = this.gestorDB.dameConexion();
				PreparedStatement sentencia = conect.prepareStatement(sql)) {

			sentencia.setInt(1, serie.getId());
			sentencia.setString(2, serie.getTitulo());
			sentencia.setString(3, serie.getGenero());
			sentencia.setInt(4, serie.getNumeroTemporadas());
			sentencia.setInt(5, serie.getAño());
			sentencia.setInt(6, serie.getPlataforma().getId());

			sentencia.executeUpdate();
			flag = true;
		} catch (Exception e) {
			System.out.println("Ocurrio un error en el Registro dao de Serie" + e.getMessage());
		}

		return flag;// Para comparación insensible a mayúsculas
        // Búsqueda parcial e insensible, SIN el .replace("'", "''")

	}

	public boolean editar(Serie serie) {
		boolean flag = false;

		String sql = "UPDATE SERIE SET TITULO = ?, GENERO = ?, NUM_TEMPORADAS = ?, AÑO_LANZAMIENTO = ?, ID_PLATAFORMA = ? WHERE ID = ?";

		try (Connection conect = this.gestorDB.dameConexion();
				PreparedStatement sentencia = conect.prepareStatement(sql)) {

			sentencia.setString(1, serie.getTitulo());
			sentencia.setString(2, serie.getGenero());
			sentencia.setInt(3, serie.getNumeroTemporadas());
			sentencia.setInt(4, serie.getAño());
			sentencia.setInt(5, serie.getPlataforma().getId());
			sentencia.setInt(6, serie.getId());

			int filasAfectadas = sentencia.executeUpdate();
			if (filasAfectadas > 0) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("Ocurrio un error en Editar dao de Serie" + e.getMessage());
		}
		return flag;
	}

	public boolean eliminar(int id) {
		boolean flag = false;
		String sql = "DELETE FROM SERIE WHERE ID = ?";

		try (Connection conect = this.gestorDB.dameConexion();
				PreparedStatement sentencia = conect.prepareStatement(sql)) {

			sentencia.setInt(1, id);

			int filasAfectadas = sentencia.executeUpdate();
			if (filasAfectadas > 0) {
				flag = true;
			}

		} catch (Exception e) {
			System.out.println("Ocurrio un error en Eliminar dao de plataforma" + e.getMessage());
		}

		return flag;

	}

	public List<Serie> consultar(String columnaBusqueda, String textoBusqueda) {
        List<Serie> listaSeries = new ArrayList<>();
        String sql = "SELECT ID, TITULO, GENERO, NUM_TEMPORADAS, AÑO_LANZAMIENTO, ID_PLATAFORMA FROM SERIE";

       
        if (columnaBusqueda != null && !columnaBusqueda.trim().isEmpty() &&
            textoBusqueda != null && !textoBusqueda.trim().isEmpty()) {

            String columnaEnDB = "";
            String valorFormateadoParaSQL = "";

            if ("TITULO".equalsIgnoreCase(columnaBusqueda)) {
                columnaEnDB = "UPPER(TITULO)"; 
                valorFormateadoParaSQL = "'%" + textoBusqueda.toUpperCase() + "%'";
                sql += " WHERE " + columnaEnDB + " LIKE " + valorFormateadoParaSQL;
            } else if ("GENERO".equalsIgnoreCase(columnaBusqueda)) {
                columnaEnDB = "UPPER(GENERO)"; 
                valorFormateadoParaSQL = "'%" + textoBusqueda.toUpperCase() + "%'";
                sql += " WHERE " + columnaEnDB + " LIKE " + valorFormateadoParaSQL;
            } 
            else if ("PLATAFORMA".equalsIgnoreCase(columnaBusqueda)) {
                
                columnaEnDB = "ID_PLATAFORMA";
                valorFormateadoParaSQL = textoBusqueda; 
                
            }
            else {
                System.out.println("Filtro por columna '" + columnaBusqueda + "' no soportado .");
               
                return listaSeries;
            }
            sql += " WHERE " + columnaEnDB + " = " + valorFormateadoParaSQL;
        }
        

        

        try (Connection conect = this.gestorDB.dameConexion();
             Statement sentencia = conect.createStatement(); 
             ResultSet resultado = sentencia.executeQuery(sql)) { 

            while (resultado.next()) {
                int id = resultado.getInt("ID");
                String titulo = resultado.getString("TITULO");
                String genero = resultado.getString("GENERO");
                int numTemporadas = resultado.getInt("NUM_TEMPORADAS");
                int anioLanzamiento = resultado.getInt("AÑO_LANZAMIENTO");
                int idPlataforma = resultado.getInt("ID_PLATAFORMA");
                boolean plataformaEraNula = resultado.wasNull();

                Plataforma plataforma = null;
                if (!plataformaEraNula) {
                    
                    plataforma = obtenerPlataformaById(idPlataforma, conect);
                }

                Serie serie = new Serie(id, titulo, genero, numTemporadas, anioLanzamiento, plataforma);
                listaSeries.add(serie);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar series: " + e.getMessage());
           
        }
        return listaSeries;
    }
	
	
	
	 private Plataforma obtenerPlataformaById(int idPlataforma, Connection conexion) {
	        
	        String sqlPlataforma = "SELECT ID, NOMBRE, PAIS_ORIGEN FROM PLATAFORMA WHERE ID = " + idPlataforma;
	        Plataforma plataforma = null;
	        try (Statement stmt = conexion.createStatement();
	             ResultSet rs = stmt.executeQuery(sqlPlataforma)) {
	            if (rs.next()) {
	                
	                plataforma = new Plataforma(rs.getInt("ID"), rs.getString("NOMBRE"), rs.getString("PAIS_ORIGEN"));
	            }
	        } catch (SQLException e) {
	            
	            System.out.println("Nota: No se pudo cargar la plataforma con ID " + idPlataforma + ". " + e.getMessage());
	        }
	        return plataforma;
	    }


}
