package com.proyecto.gestionseriestv.vista;

import com.proyecto.gestionseriestv.controlador.PlataformaControl;
import com.proyecto.gestionseriestv.modelo.Plataforma;

import javax.swing.*;
import java.awt.*;

public class FormularioPlataforma extends JDialog {

    private static final long serialVersionUID = 1L;
	private PlataformaControl plataformaControl;
    private Plataforma plataformaAEditar;

    private JTextField txtId, txtNombre, txtPaisOrigen;
    private JButton btnGuardar, btnCancelar;

    private boolean guardadoExitoso = false;

    public FormularioPlataforma(Frame parent, boolean modal, PlataformaControl platCtrl, Plataforma plataformaParaEditar) {
        super(parent, modal);
        this.plataformaControl = platCtrl;
        this.plataformaAEditar = plataformaParaEditar;

        if (this.plataformaAEditar == null) {
            setTitle("Registrar Nueva Plataforma");
        } else {
            setTitle("Editar Plataforma - ID: " + this.plataformaAEditar.getId());
        }
        
        initComponents();

        if (this.plataformaAEditar != null) {
            cargarDatosPlataformaParaEdicion();
        } else {
            txtId.setEditable(true); // ID editable solo para nuevas
        }
        
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelCampos.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtId = new JTextField(10);
        panelCampos.add(txtId, gbc);
        gbc.weightx = 0.0;

        gbc.gridx = 0; gbc.gridy = 1; panelCampos.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtNombre = new JTextField(25);
        panelCampos.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelCampos.add(new JLabel("País de Origen:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPaisOrigen = new JTextField(20);
        panelCampos.add(txtPaisOrigen, gbc);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarPlataforma());
        btnCancelar.addActionListener(e -> {
            guardadoExitoso = false;
            dispose();
        });
    }
    
    private void cargarDatosPlataformaParaEdicion() {
        if (plataformaAEditar != null) {
            txtId.setText(String.valueOf(plataformaAEditar.getId()));
            txtId.setEditable(false);
            txtNombre.setText(plataformaAEditar.getNombre());
            txtPaisOrigen.setText(plataformaAEditar.getPaisOrigen());
        }
    }

    private void guardarPlataforma() {
        String idStr = txtId.getText();
        String nombre = txtNombre.getText();
        String pais = txtPaisOrigen.getText();

        boolean exitoOperacion;
        if (plataformaAEditar == null) { 
            exitoOperacion = plataformaControl.registrarPlataforma(idStr, nombre, pais);
        } else { 
            exitoOperacion = plataformaControl.actualizarPlataforma(plataformaAEditar.getId(), nombre, pais);
        }

        if (exitoOperacion) {
            this.guardadoExitoso = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la plataforma. Verifique los datos o la consola.",
                    "Error al Guardar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isGuardadoExitoso() {
        return guardadoExitoso;
    }
}