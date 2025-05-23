package com.proyecto.gestionseriestv.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    	String sql=" update Plataforma set nombre=?,pais_origen=?  where id=?";
    	try(Connection conect = this.gestorDB.dameConexion();
    		PreparedStatement sentencia=conect.prepareStatement(sql)){
    		
    		sentencia.setString(1, plataforma.getNombre());
    		sentencia.setString(2, plataforma.getPaisOrigen());
    		sentencia.setLong(3, plataforma.getId());

    		
    		sentencia.executeUpdate();
    		flag=true;
    		
    		
    	}catch(Exception e ) {
    		System.out.println("Ocurrio un error en EDITAR dao de plataforma" + e.getMessage());
    	}
    	
		return flag;
    	
    }
    
    public boolean eliminar(int id) {
    	boolean flag =false;
    	String sql=" delete from Plataforma where id=?";
    	try(Connection conect = this.gestorDB.dameConexion();
    		PreparedStatement sentencia=conect.prepareStatement(sql)){
    		
    		sentencia.setInt(1, id);
    		
    		sentencia.executeUpdate();
    		flag=true;
    		
    		
    	}catch(Exception e ) {
    		System.out.println("Ocurrio un error en Eliminar dao de plataforma" + e.getMessage());
    	}
    	
    	
    	
		return flag;
    	
    }
    public  List<Plataforma>consultar(String columnTexto , String texto){
    	
    	List<Plataforma> listaPlataformas=new ArrayList<>();
    	
    	return null;
    }
    
    
    }

