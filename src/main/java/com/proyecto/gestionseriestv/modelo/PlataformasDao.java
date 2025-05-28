package com.proyecto.gestionseriestv.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class PlataformasDao {

    private GestorBaseDatos gestorDB;

    public PlataformasDao(GestorBaseDatos gestorDB) {
        this.gestorDB = gestorDB;
    }
    	
    public boolean registrar(Plataforma plataforma) {
    	String sql="INSERT INTO PLATAFORMA(ID,NOMBRE,PAIS_ORIGEN) values(?,?,?)";
    	boolean flag = false;
    	try (Connection conect = this.gestorDB.dameConexion();
        	PreparedStatement sentencia=conect.prepareStatement(sql)){

    		
    		sentencia.setLong(1, plataforma.getId());
    		sentencia.setString(2, plataforma.getNombre());
    		sentencia.setString(3, plataforma.getPaisOrigen());
    		
    		sentencia.executeUpdate();
    		flag=true;
    	}catch(Exception e) {
    		System.out.println("Ocurrio un error en el Registro dao de plataforma" + e.getMessage());
    	}
    	
    	
		return flag;
    	
    }
    
    public boolean editar(Plataforma plataforma) {
    	boolean flag =false;
    	 // Asegúrate del nombre de la tabla y columnas
        String sql = "UPDATE PLATAFORMA SET NOMBRE = ?, PAIS_ORIGEN = ? WHERE ID = ?";
        try (Connection conect = this.gestorDB.dameConexion();
             PreparedStatement sentencia = conect.prepareStatement(sql)) {
            sentencia.setString(1, plataforma.getNombre());
            sentencia.setString(2, plataforma.getPaisOrigen());
            sentencia.setInt(3, plataforma.getId()); // Usar setInt si el ID es numérico

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                flag = true;
                System.out.println("DAO: Plataforma con ID " + plataforma.getId() + " actualizada.");
            } else {
                System.out.println("DAO: No se encontró plataforma con ID " + plataforma.getId() + " para editar.");
            }
        } catch (SQLException e) {
            System.err.println("DAO Error: Ocurrió un error al editar plataforma con ID " + plataforma.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    	
		return flag;
    	
    }
    
    public boolean eliminar(int id) {
    	 boolean flag = false;
    	    // Asegúrate de que el nombre de la tabla es el correcto (PLATAFORMAS o PLATAFORMA)
    	    String sql = "DELETE FROM PLATAFORMA WHERE ID = ?"; 
    	    try (Connection conect = this.gestorDB.dameConexion();
    	         PreparedStatement sentencia = conect.prepareStatement(sql)) {
    	        sentencia.setInt(1, id);
    	        int filasAfectadas = sentencia.executeUpdate();
    	        if (filasAfectadas > 0) {
    	            flag = true; // Solo es éxito si realmente se borró algo
    	            System.out.println("DAO: Plataforma con ID " + id + " eliminada exitosamente.");
    	        } else {
    	            System.out.println("DAO: No se encontró plataforma con ID " + id + " para eliminar.");
    	        }
    	    } catch (SQLException e) {
    	        System.err.println("DAO Error: Ocurrió un error al eliminar plataforma con ID " + id + ": " + e.getMessage());
    	        e.printStackTrace();
    	    }
    	
		return flag;
    	
    }  public List<Plataforma> consultarTodas() {
        List<Plataforma> listaPlataformas = new ArrayList<>();
        String sql = "SELECT ID, NOMBRE, PAIS_ORIGEN FROM PLATAFORMA ORDER BY NOMBRE"; 

        try (Connection conect = this.gestorDB.dameConexion();
             Statement sentencia = conect.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {

            while (resultado.next()) {
                int id = resultado.getInt("ID");
                String nombre = resultado.getString("NOMBRE");
                String paisOrigen = resultado.getString("PAIS_ORIGEN");
                Plataforma plataforma = new Plataforma(id, nombre, paisOrigen);
                listaPlataformas.add(plataforma);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar todas las plataformas: " + e.getMessage());
        }
        return listaPlataformas;
  
    
  
    
    
    }}

