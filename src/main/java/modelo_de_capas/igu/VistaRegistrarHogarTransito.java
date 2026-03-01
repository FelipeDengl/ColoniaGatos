package modelo_de_capas.igu;

import modelo_de_capas.logica.Controladora;

import javax.swing.*;
import java.awt.*;

public class VistaRegistrarHogarTransito extends JPanel {

    private final MainFrame mainFrame;
    private final Controladora control;

    private JTextField txtNombre;
    private JCheckBox chkDisponible;
    private JTextField txtDireccion;
    private JTextArea txtCaracteristica;

    public VistaRegistrarHogarTransito(MainFrame mainFrame, Controladora control) {
        this.mainFrame = mainFrame;
        this.control = control;

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // TÍTULO
        JLabel lblTitulo = new JLabel("Registrar hogar de tránsito");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(lblTitulo, BorderLayout.NORTH);

        // PANEL FORMULARIO
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(new Color(240, 240, 240));
        panelForm.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelForm.add(new JLabel("Nombre del hogar:"), gbc);

        txtNombre = new JTextField(25);
        gbc.gridx = 1;
        panelForm.add(txtNombre, gbc);

        // Disponible
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelForm.add(new JLabel("Disponible:"), gbc);

        chkDisponible = new JCheckBox("Actualmente disponible para recibir gatos");
        chkDisponible.setSelected(true);
        chkDisponible.setBackground(new Color(240, 240, 240));
        gbc.gridx = 1;
        panelForm.add(chkDisponible, gbc);

        // Dirección
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        panelForm.add(new JLabel("Dirección:"), gbc);

        txtDireccion = new JTextField(25);
        gbc.gridx = 1;
        panelForm.add(txtDireccion, gbc);

        // Característica
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelForm.add(new JLabel("Características (espacio, otros animales, etc.):"), gbc);

        txtCaracteristica = new JTextArea(4, 25);
        txtCaracteristica.setLineWrap(true);
        txtCaracteristica.setWrapStyleWord(true);
        JScrollPane scrollCaract = new JScrollPane(txtCaracteristica);
        gbc.gridx = 1;
        panelForm.add(scrollCaract, gbc);

        add(panelForm, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(230, 230, 230));

        JButton btnGuardar = new JButton("Guardar hogar");
        JButton btnCancelar = new JButton("Cancelar");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        //---------- listeners
        btnGuardar.addActionListener(e -> guardarHogar());
        btnCancelar.addActionListener(e -> mainFrame.irAHome());
    }

    private void guardarHogar() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String caracteristica = txtCaracteristica.getText().trim();
        boolean disponible = chkDisponible.isSelected();

        //---------- validacion de los datos que sean obligatorios
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El nombre del hogar es obligatorio.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La dirección es obligatoria.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // crea el HogarTransito y lo asocia al voluntario logueado
            control.crearHogarTransito(nombre, disponible, direccion, caracteristica);

            JOptionPane.showMessageDialog(
                    this,
                    "Hogar de tránsito creado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Opcional: limpiar campos
            txtNombre.setText("");
            txtDireccion.setText("");
            txtCaracteristica.setText("");
            chkDisponible.setSelected(true);

            // Volver al home
            mainFrame.irAHome();

        } catch (IllegalStateException ex) {
            // por si no hay voluntario logueado (seguridad extra)
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocurrió un error al guardar el hogar de tránsito:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
