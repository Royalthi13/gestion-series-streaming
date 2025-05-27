package com.proyecto.gestionseriestv.vista;

import com.proyecto.gestionseriestv.controlador.SerieControl;
import com.proyecto.gestionseriestv.modelo.Plataforma;
import com.proyecto.gestionseriestv.modelo.Serie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormularioSerie extends JDialog {

    private static final long serialVersionUID = 1L;
    private SerieControl serieControl;
    private Serie serieAEditar;

    private JTextField txtId, txtTitulo, txtGenero, txtNumTemporadas, txtAnio;
    private JComboBox<Plataforma> comboPlataforma;
    private DefaultComboBoxModel<Plataforma> modeloComboPlataforma;
    private JButton btnGuardar, btnCancelar;

    private boolean guardadoExitoso = false;

    public FormularioSerie(Frame parent, boolean modal, SerieControl serieCtrl, Serie serieParaEditar) {
        super(parent, modal);
        this.serieControl = serieCtrl;
        this.serieAEditar = serieParaEditar;

        if (this.serieAEditar == null) {
            setTitle("Registrar Nueva Serie");
        } else {
            setTitle("Editar Serie - ID: " + this.serieAEditar.getId());
        }

        initComponents();

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelCampos.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtId = new JTextField(10);
        txtId.putClientProperty("JTextField.placeholderText", "ID numérico (si es nueva)"); 
        panelCampos.add(txtId, gbc);
        gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 1; panelCampos.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtTitulo = new JTextField(25);
        txtTitulo.putClientProperty("JTextField.placeholderText", "Ej: Stranger Things"); 
        panelCampos.add(txtTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelCampos.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtGenero = new JTextField(20);
        txtGenero.putClientProperty("JTextField.placeholderText", "Ej: Ciencia Ficción, Terror"); 
        panelCampos.add(txtGenero, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelCampos.add(new JLabel("Nº Temporadas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtNumTemporadas = new JTextField(5);
        txtNumTemporadas.putClientProperty("JTextField.placeholderText", "Ej: 4"); 
        panelCampos.add(txtNumTemporadas, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panelCampos.add(new JLabel("Año Lanzamiento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        txtAnio = new JTextField(5);
        txtAnio.putClientProperty("JTextField.placeholderText", "Ej: 2016"); 
        panelCampos.add(txtAnio, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panelCampos.add(new JLabel("Plataforma:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        modeloComboPlataforma = new DefaultComboBoxModel<>();
        comboPlataforma = new JComboBox<>(modeloComboPlataforma);
        panelCampos.add(comboPlataforma, gbc);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); 
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 
        btnGuardar = new JButton("Guardar");
        btnGuardar.putClientProperty("JButton.buttonType", "primary"); 

        btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        // Hacer que el botón Guardar sea el botón por defecto (reacciona a Enter)
        getRootPane().setDefaultButton(btnGuardar);

        cargarPlataformasDisponibles();
        if (serieAEditar != null) {
            cargarDatosSerieParaEdicion();
        } else {
            txtId.setEditable(true);
        }

        btnGuardar.addActionListener(e -> guardarSerie());
        btnCancelar.addActionListener(e -> {
            guardadoExitoso = false;
            dispose();
        });
    }

    private void cargarPlataformasDisponibles() {
        List<Plataforma> plataformas = serieControl.obtenerTodasLasPlataformasParaVista();
        modeloComboPlataforma.removeAllElements();
        modeloComboPlataforma.addElement(null); 
        if (plataformas != null) {
            for (Plataforma p : plataformas) {
                modeloComboPlataforma.addElement(p);
            }
        }
        if (serieAEditar == null || serieAEditar.getPlataforma() == null) {
             comboPlataforma.setSelectedItem(null);
        }
    }

    private void cargarDatosSerieParaEdicion() {
        if (serieAEditar != null) {
            txtId.setText(String.valueOf(serieAEditar.getId()));
            txtId.setEditable(false);
            txtTitulo.setText(serieAEditar.getTitulo());
            txtGenero.setText(serieAEditar.getGenero());
            txtNumTemporadas.setText(String.valueOf(serieAEditar.getNumeroTemporadas()));
            txtAnio.setText(String.valueOf(serieAEditar.getAño()));
            if (serieAEditar.getPlataforma() != null) {
                modeloComboPlataforma.setSelectedItem(serieAEditar.getPlataforma());
            } else {
                comboPlataforma.setSelectedItem(null); 
            }
        }
    }

    private void guardarSerie() {
        String idStr = txtId.getText();
        String titulo = txtTitulo.getText();
        String genero = txtGenero.getText();
        String numTempStr = txtNumTemporadas.getText();
        String anioStr = txtAnio.getText();
        Plataforma plataformaSel = (Plataforma) modeloComboPlataforma.getSelectedItem();

        boolean exitoOperacion;
        if (serieAEditar == null) {
            exitoOperacion = serieControl.registrarSerie(idStr, titulo, genero, numTempStr, anioStr, plataformaSel);
        } else {
            exitoOperacion = serieControl.actualizarSerie(serieAEditar.getId(), titulo, genero, numTempStr, anioStr, plataformaSel);
        }

        if (exitoOperacion) {
            this.guardadoExitoso = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la serie. Verifique los datos o la consola.",
                    "Error al Guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}