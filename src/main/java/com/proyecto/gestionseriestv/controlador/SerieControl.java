package com.proyecto.gestionseriestv.controlador;

import com.proyecto.gestionseriestv.modelo.Plataforma;
import com.proyecto.gestionseriestv.modelo.Serie;
import com.proyecto.gestionseriestv.modelo.SeriesDao;
import com.proyecto.gestionseriestv.modelo.PlataformasDao; // Cambiado de PlataformaDao a PlataformasDao
// import com.proyecto.gestionseriestv.vista.FormularioSerie; // El controlador no debería conocer la vista concreta
// import com.proyecto.gestionseriestv.vista.VentanaPrincipal; // El controlador no debería conocer la vista concreta


import java.util.List;
import java.util.ArrayList;

public class SerieControl {

    private SeriesDao seriesDao;
    private PlataformasDao plataformaDao; 

    public SerieControl(SeriesDao seriesDao, PlataformasDao plataformaDao) { 
        this.seriesDao = seriesDao;
        this.plataformaDao = plataformaDao;
    }

    // Método para que la vista obtenga la lista de todas las plataformas
    public List<Plataforma> obtenerTodasLasPlataformasParaVista() {
        List<Plataforma> plataformas = plataformaDao.consultarTodas();
        if (plataformas == null) {
            System.out.println("Controlador: DAO de plataformas devolvió null al obtener todas las plataformas.");
            return new ArrayList<>();
        }
        return plataformas;
    }
    
    public Serie obtenerSeriePorIdParaEdicion(int idSerie) {
        
        List<Serie> todas = seriesDao.consultar(null, null); 
        if (todas != null) {
            for (Serie s : todas) {
                if (s.getId() == idSerie) {
                    return s; // Devuelve la serie encontrada
                }
            }
        }
        System.out.println("Controlador: No se encontró la serie con ID " + idSerie + " para edición.");
        return null; // No encontrada
    }


    public boolean registrarSerie(String idStr, String titulo, String genero, String numTemporadasStr, String anioStr, Plataforma plataformaSeleccionada) {
        int id, numTemporadas, anio;
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Controlador Error: Título vacío al registrar.");
            return false;
        }
        if (plataformaSeleccionada == null) {
            System.out.println("Controlador Error: Plataforma no seleccionada al registrar.");
            return false;
        }
        try {
            id = Integer.parseInt(idStr.trim());
            numTemporadas = Integer.parseInt(numTemporadasStr.trim());
            anio = Integer.parseInt(anioStr.trim());
            if (numTemporadas <= 0 || anio <= 1900 || anio > 2100) {
                 System.out.println("Controlador Error: Valores numéricos fuera de rango al registrar.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Controlador Error: Formato numérico incorrecto al registrar. " + e.getMessage());
            return false;
        }

        Serie nuevaSerie = new Serie(id, titulo.trim(), genero.trim(), numTemporadas, anio, plataformaSeleccionada);
        boolean exito = seriesDao.registrar(nuevaSerie);

        if (exito) {
            System.out.println("Controlador: Serie registrada con éxito: " + nuevaSerie.getTitulo());
        } else {
            System.out.println("Controlador Error: Fallo al registrar serie en DAO. ID podría ya existir.");
        }
        return exito;
    }

    public boolean actualizarSerie(int idOriginal, String titulo, String genero, String numTemporadasStr, String anioStr, Plataforma plataformaSeleccionada) {
        int numTemporadas, anio;
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Controlador Error: Título vacío al actualizar.");
            return false;
        }
        if (plataformaSeleccionada == null) {
            System.out.println("Controlador Error: Plataforma no seleccionada al actualizar.");
            return false;
        }
        try {
            numTemporadas = Integer.parseInt(numTemporadasStr.trim());
            anio = Integer.parseInt(anioStr.trim());
            if (numTemporadas <= 0 || anio <= 1900 || anio > 2100) {
                System.out.println("Controlador Error: Valores numéricos fuera de rango al actualizar.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Controlador Error: Formato numérico incorrecto al actualizar. " + e.getMessage());
            return false;
        }

        Serie serieActualizada = new Serie(idOriginal, titulo.trim(), genero.trim(), numTemporadas, anio, plataformaSeleccionada);
        boolean exito = seriesDao.editar(serieActualizada);

        if (exito) {
            System.out.println("Controlador: Serie actualizada con éxito: " + serieActualizada.getTitulo());
        } else {
            System.out.println("Controlador Error: Fallo al actualizar serie en DAO.");
        }
        return exito;
    }

    public boolean eliminarSerie(int idSerie) {
        boolean exito = seriesDao.eliminar(idSerie);
        if (exito) {
            System.out.println("Controlador: Serie con ID " + idSerie + " eliminada.");
        } else {
            System.out.println("Controlador Error: Fallo al eliminar serie con ID " + idSerie + " en DAO.");
        }
        return exito;
    }

    public List<Serie> buscarSeries(String columnaBusqueda, String textoBusqueda) {
        System.out.println("Controlador: Buscando series por " + columnaBusqueda + " = " + textoBusqueda);
        List<Serie> series = seriesDao.consultar(columnaBusqueda, textoBusqueda);
        if (series == null) {
            System.out.println("Controlador: DAO devolvió null al buscar series.");
            return new ArrayList<>();
        }
        System.out.println("Series encontradas: " + series.size());
        return series;
    }

    public List<Serie> cargarTodasLasSeries() {
        List<Serie> todasLasSeries = seriesDao.consultar(null, null);
        if (todasLasSeries == null) {
             System.out.println("Controlador: DAO devolvió null al cargar todas las series.");
            return new ArrayList<>();
        }
        System.out.println("Controlador: Cargadas todas las series: " + todasLasSeries.size());
        return todasLasSeries;
    }
}