package com.proyecto.gestionseriestv.app;


import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;

import com.proyecto.gestionseriestv.controlador.PlataformaControl;
import com.proyecto.gestionseriestv.controlador.SerieControl;
import com.proyecto.gestionseriestv.modelo.GestorBaseDatos;
import com.proyecto.gestionseriestv.modelo.PlataformasDao;
import com.proyecto.gestionseriestv.modelo.SeriesDao;
import com.proyecto.gestionseriestv.vista.VentanaPrincipal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainApp {

    public static void main(String[] args) {




        // Esto es un tema  de FLATLAF que lo integro con swing haciendo que se vea mas moderno
        try {
            UIManager.setLookAndFeel(new FlatDraculaIJTheme());

            System.out.println("FlatLaf Dracula theme aplicado.");

            int borderRadius = 8;
            UIManager.put("Component.arc", borderRadius);
            UIManager.put("Button.arc", borderRadius);
            UIManager.put("TextComponent.arc", borderRadius); 
            UIManager.put("ComboBox.arc", borderRadius);      
            UIManager.put("ProgressBar.arc", borderRadius + 4); 
            UIManager.put("ScrollPane.arc", borderRadius);    





        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Error al inicializar FlatLaf: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Error general al configurar FlatLaf: " + ex.getMessage());
            ex.printStackTrace();
        }


        // EJECUTAR LA CREACIÓN DE LA GUI  DE SWING
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GestorBaseDatos gestorDB = new GestorBaseDatos();
                System.out.println("Verificando y/o creando tablas...");
                gestorDB.verificarYCrearTablas();
                System.out.println("Verificación de tablas completada.");

                SeriesDao seriesDao = new SeriesDao(gestorDB);
                PlataformasDao plataformasDao = new PlataformasDao(gestorDB);

                SerieControl serieControl = new SerieControl(seriesDao, plataformasDao);
                PlataformaControl plataformaControl = new PlataformaControl(plataformasDao);

                VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(serieControl, plataformaControl);
                ventanaPrincipal.setVisible(true);
                System.out.println("Aplicación iniciada y VentanaPrincipal visible.");
            }
        });
    }
}