package com.proyecto.gestionseriestv.vista;

import com.proyecto.gestionseriestv.controlador.PlataformaControl;
import com.proyecto.gestionseriestv.controlador.SerieControl;
import com.proyecto.gestionseriestv.modelo.Plataforma;
import com.proyecto.gestionseriestv.modelo.Serie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SerieControl serieControl;
    private PlataformaControl plataformaControl;

    private JTabbedPane tabbedPane;
    private JPanel panelSeries;
    private JTable tablaSeries;
    private DefaultTableModel modeloTablaSeries;
    private JButton btnNuevaSerie;
    private JButton btnEditarSerie;
    private JButton btnEliminarSerie;
    private JButton btnBuscarSerie;
    private JButton btnMostrarTodasSeries;
    private JTextField txtBuscarSerieTexto;
    private JComboBox<String> comboFiltroColumnaSerie;
//Botones , Barra de estado
    private JPanel panelPlataformas;
    private JTable tablaPlataformas;
    private DefaultTableModel modeloTablaPlataformas;
    private JButton btnNuevaPlataforma;
    private JButton btnEditarPlataforma;
    private JButton  btnEliminarPlataforma;
    private JButton btnMostrarTodasPlataformas;

    private JLabel lblStatus; 

    public VentanaPrincipal(SerieControl serieCtrl, PlataformaControl platCtrl) {
        this.serieControl = serieCtrl;
        this.plataformaControl = platCtrl;

        setTitle("Gestión de Series y Plataformas de TV");
        setSize(1200, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        cargarDatosIniciales();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // --- Pestaña de Series ---
        panelSeries = new JPanel(new BorderLayout(10, 10));
        panelSeries.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      final  JPanel panelControlesSeries = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelControlesSeries.add(new JLabel("Buscar por:"));
        comboFiltroColumnaSerie = new JComboBox<>(new String[]{"TITULO", "GENERO", "PLATAFORMA"});
        panelControlesSeries.add(comboFiltroColumnaSerie);
        panelControlesSeries.add(new JLabel("Texto:"));
        txtBuscarSerieTexto = new JTextField(20);
        txtBuscarSerieTexto.putClientProperty("JTextField.placeholderText", "Escriba para buscar..."); 
        panelControlesSeries.add(txtBuscarSerieTexto);

        btnBuscarSerie = new JButton("Buscar");
        btnBuscarSerie.putClientProperty("JButton.buttonType", "primary"); 
        panelControlesSeries.add(btnBuscarSerie);

        btnMostrarTodasSeries = new JButton("Mostrar Todas");
        panelControlesSeries.add(btnMostrarTodasSeries);

        panelControlesSeries.add(Box.createHorizontalStrut(20)); 

        btnNuevaSerie = new JButton("Nueva Serie");
        panelControlesSeries.add(btnNuevaSerie);

        btnEditarSerie = new JButton("Editar Serie");
        panelControlesSeries.add(btnEditarSerie);

        btnEliminarSerie = new JButton("Eliminar Serie");
        panelControlesSeries.add(btnEliminarSerie);
        panelSeries.add(panelControlesSeries, BorderLayout.NORTH);

   final     String[] columnasSeries = {"ID", "Título", "Género", "Temporadas", "Año", "Plataforma"};
        modeloTablaSeries = new DefaultTableModel(columnasSeries, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaSeries = new JTable(modeloTablaSeries);
        tablaSeries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaSeries.getTableHeader().setReorderingAllowed(false);
        tablaSeries.setFillsViewportHeight(true); 
        tablaSeries.setRowHeight(28); 
        tablaSeries.putClientProperty("JTable.alternateRowColor", UIManager.getColor("Table.alternateRowColor"));
        panelSeries.add(new JScrollPane(tablaSeries), BorderLayout.CENTER);
        tabbedPane.addTab("Gestión de Series", panelSeries); 

        // --- Pestaña de Plataformas ---
        panelPlataformas = new JPanel(new BorderLayout(10, 10));
        panelPlataformas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

     final   JPanel panelControlesPlataformas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnNuevaPlataforma = new JButton("Nueva Plataforma");
        panelControlesPlataformas.add(btnNuevaPlataforma);

        btnEditarPlataforma = new JButton("Editar Plataforma");
        panelControlesPlataformas.add(btnEditarPlataforma);

        btnEliminarPlataforma = new JButton("Eliminar Plataforma");
        panelControlesPlataformas.add(btnEliminarPlataforma);

        btnMostrarTodasPlataformas = new JButton("Mostrar Todas");
        panelControlesPlataformas.add(btnMostrarTodasPlataformas);
        panelPlataformas.add(panelControlesPlataformas, BorderLayout.NORTH);

      final  String[] columnasPlataformas = {"ID", "Nombre", "País de Origen"};
        modeloTablaPlataformas = new DefaultTableModel(columnasPlataformas, 0) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaPlataformas = new JTable(modeloTablaPlataformas);
        tablaPlataformas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPlataformas.getTableHeader().setReorderingAllowed(false);
        tablaPlataformas.setFillsViewportHeight(true); 
        tablaPlataformas.setRowHeight(28); 
        panelPlataformas.add(new JScrollPane(tablaPlataformas), BorderLayout.CENTER);
        tabbedPane.addTab("Gestión de Plataformas", panelPlataformas); 

        add(tabbedPane, BorderLayout.CENTER); 

        // Barra de estado
        lblStatus = new JLabel("Listo.");
        lblStatus.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
      add(lblStatus, BorderLayout.SOUTH);


        // Action Listeners
        btnNuevaSerie.addActionListener(e -> abrirFormularioNuevaSerie());
        btnEditarSerie.addActionListener(e -> abrirFormularioEditarSerie());
        btnEliminarSerie.addActionListener(e -> eliminarSerieSeleccionada());
        btnBuscarSerie.addActionListener(e -> buscarSeries());
        btnMostrarTodasSeries.addActionListener(e -> cargarDatosSeries());

        btnNuevaPlataforma.addActionListener(e -> abrirFormularioNuevaPlataforma());
        btnEditarPlataforma.addActionListener(e -> abrirFormularioEditarPlataforma());
        btnEliminarPlataforma.addActionListener(e -> eliminarPlataformaSeleccionada());
        btnMostrarTodasPlataformas.addActionListener(e -> cargarDatosPlataformas());
    }

    private void cargarDatosIniciales() {
        cargarDatosSeries();
        cargarDatosPlataformas();
        lblStatus.setText("Datos cargados. " + modeloTablaSeries.getRowCount() + " series, " + modeloTablaPlataformas.getRowCount() + " plataformas.");
    }

    public void cargarDatosSeries() {
        List<Serie> series = serieControl.cargarTodasLasSeries();
        mostrarSeriesEnTabla(series);
        if (series != null) {
            lblStatus.setText(series.size() + " series cargadas.");
        } else {
            lblStatus.setText("Error al cargar series.");
        }
    }

 // En VentanaPrincipal.java
    private void buscarSeries() {
        String opcionSeleccionadaEnCombo = (String) comboFiltroColumnaSerie.getSelectedItem();
        String columnaParaDAO = opcionSeleccionadaEnCombo; // Por defecto

        // Aquí está la clave: si el texto del combo es diferente, mapearlo.
        // Ajusta "ID PLATAFORMA" al texto exacto que tienes en tu JComboBox para esta opción.
        if ("ID PLATAFORMA".equalsIgnoreCase(opcionSeleccionadaEnCombo) || 
            "Plataforma (Nombre)".equalsIgnoreCase(opcionSeleccionadaEnCombo) || // Ejemplo si el texto es otro
            "PLATAFORMA".equalsIgnoreCase(opcionSeleccionadaEnCombo) ) { // Si ya es "PLATAFORMA", también vale
            columnaParaDAO = "PLATAFORMA";
        }
        // No necesitas más else if si "TITULO" y "GENERO" ya son los valores correctos en el combo.

        final   String texto = txtBuscarSerieTexto.getText();
        List<Serie> series = serieControl.buscarSeries(columnaParaDAO, texto); // Pasas la columna corregida
        mostrarSeriesEnTabla(series);
        if (series != null) {
            lblStatus.setText(series.size() + " series encontradas.");
        } else {
            lblStatus.setText("Error en la búsqueda de series.");
        }
    }


    public void mostrarSeriesEnTabla(List<Serie> series) {
        modeloTablaSeries.setRowCount(0);
        if (series != null) {
            for (Serie serie : series) {
                modeloTablaSeries.addRow(new Object[]{
                        serie.getId(), serie.getTitulo(), serie.getGenero(),
                        serie.getNumeroTemporadas(), serie.getAño(),
                        serie.getPlataforma() != null ? serie.getPlataforma().getNombre() : "N/D"
                });
            }
        }
    }

    private void abrirFormularioNuevaSerie() {
        FormularioSerie form = new FormularioSerie(this, true, serieControl, null);
        form.setVisible(true);
        if (form.isGuardadoExitoso()) {
            cargarDatosSeries();
            mostrarMensaje("Nueva serie registrada con éxito.");
            lblStatus.setText("Nueva serie registrada. Total: " + modeloTablaSeries.getRowCount());
        }
    }

    private void abrirFormularioEditarSerie() {
final int filaSeleccionada = tablaSeries.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione una serie para editar.");
            return;
        }
        int idSerie = (Integer) modeloTablaSeries.getValueAt(filaSeleccionada, 0);
        Serie serieAEditar = serieControl.obtenerSeriePorIdParaEdicion(idSerie);

        if (serieAEditar == null) {
            mostrarError("No se pudo obtener los detalles de la serie ID: " + idSerie + " para editar.\nConsulte la consola del controlador para más detalles.");
            return;
        }
final  FormularioSerie form = new FormularioSerie(this, true, serieControl, serieAEditar);
        form.setVisible(true);
        if (form.isGuardadoExitoso()) {
            cargarDatosSeries();
            mostrarMensaje("Serie actualizada con éxito.");
            lblStatus.setText("Serie ID " + idSerie + " actualizada.");
        }
    }

    private void eliminarSerieSeleccionada() {
        int filaSeleccionada = tablaSeries.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione una serie para eliminar.");
            return;
        }
       final int idSerie = (Integer) modeloTablaSeries.getValueAt(filaSeleccionada, 0);
       final String tituloSerie = (String) modeloTablaSeries.getValueAt(filaSeleccionada, 1);
       final  int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la serie '" + tituloSerie + "' (ID: " + idSerie + ")?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
        final    boolean exito = serieControl.eliminarSerie(idSerie);
            if (exito) {
                cargarDatosSeries();
                mostrarMensaje("Serie eliminada con éxito.");
                lblStatus.setText("Serie ID " + idSerie + " eliminada. Total: " + modeloTablaSeries.getRowCount());
            } else {
                mostrarError("No se pudo eliminar la serie.\nConsulte la consola del controlador para más detalles.");
            }
        }
    }

    public void cargarDatosPlataformas() {
      final  List<Plataforma> plataformas = plataformaControl.cargarTodasLasPlataformas();
        mostrarPlataformasEnTabla(plataformas);
        if (plataformas != null) {
            lblStatus.setText(plataformas.size() + " plataformas cargadas.");
        } else {
             lblStatus.setText("Error al cargar plataformas.");
        }
    }

    public void mostrarPlataformasEnTabla(List<Plataforma> plataformas) {
        modeloTablaPlataformas.setRowCount(0);
        if (plataformas != null) {
            for (Plataforma plat : plataformas) {
                modeloTablaPlataformas.addRow(new Object[]{plat.getId(), plat.getNombre(), plat.getPaisOrigen()});
            }
        }
    }

    private void abrirFormularioNuevaPlataforma() {
      final  FormularioPlataforma form = new FormularioPlataforma(this, true, plataformaControl, null);
        form.setVisible(true);
        if (form.isGuardadoExitoso()) {
            cargarDatosPlataformas();
            mostrarMensaje("Nueva plataforma registrada con éxito.");
            lblStatus.setText("Nueva plataforma registrada. Total: " + modeloTablaPlataformas.getRowCount());
        }
    }

    private void abrirFormularioEditarPlataforma() {
   final     int filaSeleccionada = tablaPlataformas.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione una plataforma para editar.");
            return;
        }
     final   int idPlataforma = (Integer) modeloTablaPlataformas.getValueAt(filaSeleccionada, 0);
        Plataforma plataformaAEditar = plataformaControl.obtenerPlataformaPorIdParaEdicion(idPlataforma);
        if (plataformaAEditar == null) {
            mostrarError("No se pudo obtener los detalles de la plataforma ID: " + idPlataforma + " para editar.\nConsulte la consola del controlador.");
            return;
        }
        FormularioPlataforma form = new FormularioPlataforma(this, true, plataformaControl, plataformaAEditar);
        form.setVisible(true);
        if (form.isGuardadoExitoso()) {
            cargarDatosPlataformas();
            mostrarMensaje("Plataforma actualizada con éxito.");
            lblStatus.setText("Plataforma ID " + idPlataforma + " actualizada.");
        }
    }

    private void eliminarPlataformaSeleccionada() {
     final   int filaSeleccionada = tablaPlataformas.getSelectedRow();
        if (filaSeleccionada == -1) {
            mostrarError("Seleccione una plataforma para eliminar.");
            return;
        }
     final   int idPlataforma = (Integer) modeloTablaPlataformas.getValueAt(filaSeleccionada, 0);
      final  String nombrePlataforma = (String) modeloTablaPlataformas.getValueAt(filaSeleccionada, 1);
        final int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la plataforma '" + nombrePlataforma + "' (ID: " + idPlataforma + ")?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmacion == JOptionPane.YES_OPTION) {
          final  boolean exito = plataformaControl.eliminarPlataforma(idPlataforma);
            if (exito) {
                cargarDatosPlataformas();
                mostrarMensaje("Plataforma eliminada con éxito.");
                lblStatus.setText("Plataforma ID " + idPlataforma + " eliminada. Total: " + modeloTablaPlataformas.getRowCount());

            } else {
                mostrarError("No se pudo eliminar la plataforma.\nConsulte la consola del controlador.");
            }
        }
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        lblStatus.setText("Error: " + mensaje.substring(0, Math.min(mensaje.length(), 50)) + "..."); // Muestra parte del error en status
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
        lblStatus.setText(mensaje); // Muestra mensaje en status
    }
}