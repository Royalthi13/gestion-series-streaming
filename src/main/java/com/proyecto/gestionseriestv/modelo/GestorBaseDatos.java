package com.proyecto.gestionseriestv.modelo;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestorBaseDatos {
	private Connection conexion=null;
	 final String url = "jdbc:oracle:thin:@//192.168.10.6:1521/XEPDB1";
	 final String usuario="adrian";
	final String pwd="adrian";


	public Connection dameConexion() {
		try {
			this.conexion = DriverManager.getConnection(url, usuario, pwd);

			if (this.conexion != null) {

				System.out.println("Conexión establecida exitosamente.");            }
		} catch (SQLException e) {
			System.out.println("ERROR SQL: No se pudo conectar a la base de datos - " + e.getMessage());
			this.conexion = null;
		} catch (Exception e) {
			System.out.println("ERROR INESPERADO: Al intentar conectar - " + e.getMessage());
			this.conexion = null;
		}
		return this.conexion; 
	}

	public void verificarYCrearTablas() {
	  if (this.conexion == null) {
	    	    	dameConexion();
	        if (this.conexion== null) { 
	        	System.out.println("No se pudo establecer la conexion.");
	            return; 	        }
	    }

	final    String tablaBuscadaPlataforma = "PLATAFORMA";
	final    String tablaBuscadaSerie = "SERIE";
	    boolean plataformaEncontrada = false;
	    boolean serieEncontrada = false;

		    try (Statement stmt = this.conexion.createStatement()) {
	        DatabaseMetaData dbMeta = this.conexion.getMetaData();
	        String esquemaUsr = this.usuario.toUpperCase(); 

	        // se verifica si la PLATAFORMA existe
	        try (ResultSet tables = dbMeta.getTables(null, esquemaUsr, tablaBuscadaPlataforma, new String[]{"TABLE"})) {
	            if (tables.next()) {
	                plataformaEncontrada = true;

	            }
	        }

	        // Verificar si existe la tabla SERIE
	        try (ResultSet tables = dbMeta.getTables(null, esquemaUsr, tablaBuscadaSerie, new String[]{"TABLE"})) {
	            if (tables.next()) {
	                serieEncontrada = true;

	            }
	        } 

	        
	        if (!plataformaEncontrada) {
	            String sqlCreacionPlataforma = "CREATE TABLE PLATAFORMA(" +
	                                           "ID NUMBER(3) PRIMARY KEY, " +
	                                           "NOMBRE VARCHAR2(50) NOT NULL, " +
	                                           "PAIS_ORIGEN VARCHAR2(50) NOT NULL)";
	            stmt.executeUpdate(sqlCreacionPlataforma);
	        }

	        
	        if (!serieEncontrada) {
	            String sqlCreacionSerie = "CREATE TABLE SERIE(" +
	                                      "ID NUMBER(3) PRIMARY KEY , " +
	                                      "TITULO VARCHAR2(100) NOT NULL , " +
	                                      "GENERO VARCHAR2(50) , " +
	                                      "NUM_TEMPORADAS NUMBER(3) , " +
	                                      "AÑO_LANZAMIENTO NUMBER(4) , " + 
	                                      "ID_PLATAFORMA NUMBER(3), " +
	                                      "CONSTRAINT fk_serie_plataforma FOREIGN KEY (ID_PLATAFORMA) REFERENCES PLATAFORMA(ID) ON DELETE CASCADE" +
	                                      ")";
	            stmt.executeUpdate(sqlCreacionSerie);
	        }

	    } catch (SQLException eSQL) {
	        System.err.println("ERROR SQL durante la verificación o creación de tablas: " + eSQL.getMessage());
	        
	    } 
	    
	}
	
	

	public void cerrarConexion() {
		try {
			if (this.conexion != null && !this.conexion.isClosed()) {
				this.conexion.close();
				
			}
		} catch (SQLException e) {
			System.out.println("ERROR SQL: Al cerrar la conexión - " + e.getMessage());
		}
	}
}
