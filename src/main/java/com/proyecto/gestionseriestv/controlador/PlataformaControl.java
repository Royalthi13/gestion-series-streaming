package com.proyecto.gestionseriestv.controlador;

import com.proyecto.gestionseriestv.modelo.Plataforma;
import com.proyecto.gestionseriestv.modelo.PlataformasDao; // Usando el nombre que tienes en SerieControl
// import com.proyecto.gestionseriestv.vista.VentanaPrincipal; // El controlador no debe conocer la vista concreta
// import com.proyecto.gestionseriestv.vista.FormularioPlataforma;

import java.util.List;
import java.util.ArrayList;

public class PlataformaControl {

    private PlataformasDao plataformaDao; // Usando el nombre que tienes en SerieControl

    public PlataformaControl(PlataformasDao plataformaDao) {
        this.plataformaDao = plataformaDao;
    }

    // Método que VentanaPrincipal necesita
    public List<Plataforma> obtenerTodasLasPlataformasParaVista() {
        List<Plataforma> plataformas = plataformaDao.consultarTodas(); // Asegúrate que este método exista en PlataformasDao
        if (plataformas == null) {
            System.out.println("PlataformaControl: DAO devolvió null al obtener todas las plataformas.");
            return new ArrayList<>();
        }
        return plataformas;
    }

    // Método que VentanaPrincipal necesita para editar
    public Plataforma obtenerPlataformaPorIdParaEdicion(int idPlataforma) {
        // Este método DEBE existir en tu PlataformasDao como, por ejemplo, consultarPorId(int id)
        // Plataforma p = plataformaDao.consultarPorId(idPlataforma);
        // if (p == null) {
        //     System.out.println("PlataformaControl: No se encontró plataforma con ID " + idPlataforma);
        // }
        // return p;

        // Simulación si aún no tienes consultarPorId en PlataformasDao:
        List<Plataforma> todas = plataformaDao.consultarTodas();
        if (todas != null) {
            for (Plataforma plat : todas) {
                if (plat.getId() == idPlataforma) {
                    return plat;
                }
            }
        }
        System.out.println("PlataformaControl: No se encontró plataforma con ID " + idPlataforma + " (simulación).");
        return null;
    }


    public boolean registrarPlataforma(String idStr, String nombre, String paisOrigen) {
        int id;
        if (nombre == null || nombre.trim().isEmpty() || paisOrigen == null || paisOrigen.trim().isEmpty()) {
            System.out.println("PlataformaControl Error: Nombre o País vacíos al registrar.");
            return false; 
        }
        try {
            id = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            System.out.println("PlataformaControl Error: ID no numérico al registrar. " + e.getMessage());
            return false; 
        }

        Plataforma nuevaPlataforma = new Plataforma(id, nombre.trim(), paisOrigen.trim());
        boolean exito = plataformaDao.registrar(nuevaPlataforma); 

        if (exito) {
             System.out.println("PlataformaControl: Plataforma registrada: " + nuevaPlataforma.getNombre());
        } else {
            System.out.println("PlataformaControl Error: Fallo al registrar plataforma en DAO.");
        }
        return exito;
    }

    public boolean actualizarPlataforma(int idOriginal, String nombre, String paisOrigen) {
        if (nombre == null || nombre.trim().isEmpty() || paisOrigen == null || paisOrigen.trim().isEmpty()) {
            System.out.println("PlataformaControl Error: Nombre o País vacíos al actualizar.");
            return false;
        }

        Plataforma plataformaActualizada = new Plataforma(idOriginal, nombre.trim(), paisOrigen.trim());
        boolean exito = plataformaDao.editar(plataformaActualizada); // Asume que PlataformasDao.editar devuelve boolean

        if (exito) {
            System.out.println("PlataformaControl: Plataforma actualizada: " + plataformaActualizada.getNombre());
        } else {
            System.out.println("PlataformaControl Error: Fallo al actualizar plataforma en DAO.");
        }
        return exito;
    }

    public boolean eliminarPlataforma(int idPlataforma) {
        boolean exito = plataformaDao.eliminar(idPlataforma); // Asume que PlataformasDao.eliminar devuelve boolean
        if (exito) {
             System.out.println("PlataformaControl: Plataforma con ID " + idPlataforma + " eliminada.");
        } else {
            System.out.println("PlataformaControl Error: Fallo al eliminar plataforma con ID " + idPlataforma + " en DAO.");
        }
        return exito;
    }

    public List<Plataforma> cargarTodasLasPlataformas() { // Similar a obtenerTodasLasPlataformasParaVista pero para uso general del controlador
        List<Plataforma> todasLasPlataformas = plataformaDao.consultarTodas();
        if (todasLasPlataformas == null) {
            System.out.println("PlataformaControl: DAO devolvió null al cargar todas las plataformas.");
            return new ArrayList<>();
        }
        System.out.println("PlataformaControl: Cargadas todas las plataformas. Total: " + todasLasPlataformas.size());
        return todasLasPlataformas;
    }
}