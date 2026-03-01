package modelo_de_capas.igu;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import modelo_de_capas.logica.Colonia;
import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.PuntoAvistamiento;

public class NuevoAvistamientoDialog extends JDialog {

    private final Controladora control;
    private final String coordena;

    // Colonias y gatos filtrados
    private List<Colonia> coloniasEnPunto = new ArrayList<>();
    private List<Gato> gatosEnColonias = new ArrayList<>();

    // UI
    private JLabel lblColonias;
    private JList<Gato> listaGatos;
    private DefaultListModel<Gato> modeloGatos;

    private JRadioButton rbGatoExistente;
    private JRadioButton rbGatoNuevo;

    // Panel gato existente
    private JPanel panelGatoExistente;
    private JComboBox<String> cmbEstadoExistente;
    private JButton btnFotoExistente;
    private File fotoExistenteSeleccionada;

    // Panel gato nuevo
    private JPanel panelGatoNuevo;
    private JTextField txtNombreNuevo;
    private JTextField txtColorNuevo;
    private JTextField txtZonaNuevo;
    private JTextArea txtCaractNuevo;
    private JComboBox<String> cmbEstadoNuevo;
    private JButton btnFotoNuevo;
    private File fotoNuevoSeleccionada;

    public NuevoAvistamientoDialog(Frame parent, boolean modal,
                                   Controladora control,
                                   String coordena) {
        super(parent, modal);
        this.control = control;
        this.coordena = coordena;

        setTitle("Nuevo punto de avistamiento");
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initData();
        initUI();
    }

    private void initData() {
        double lat = 0;
        double lon = 0;
        try {
            String[] partes = coordena.split(",");
            lat = Double.parseDouble(partes[0].trim());
            lon = Double.parseDouble(partes[1].trim());
        } catch (Exception e) {
            System.out.println("⚠ No se pudo parsear coordena: " + coordena);
        }

        coloniasEnPunto = control.buscarColoniasQueContienenPunto(lat, lon);

        //---------- buscar gatos en la colonia (actualmente no anda)
        gatosEnColonias.clear();
        if (coloniasEnPunto != null) {
            for (Colonia c : coloniasEnPunto) {
                try {
                    List<Gato> gatosC = c.getListaGato();
                    if (gatosC != null) {
                        gatosEnColonias.addAll(gatosC);
                    }
                } catch (Exception e) {
                    System.out.println("⚠ No se pudo obtener gatos de colonia " + c.getNombre());
                }
            }
        }
    }

    private void initUI() {
        //---------- header
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblCoord = new JLabel("Coordenada seleccionada: " + coordena);
        lblCoord.setFont(new Font("SansSerif", Font.BOLD, 16));

        lblColonias = new JLabel();
        lblColonias.setFont(new Font("SansSerif", Font.PLAIN, 14));

        if (coloniasEnPunto == null || coloniasEnPunto.isEmpty()) {
            lblColonias.setText("No se encontró ninguna colonia que contenga este punto.");
        } else {
            StringBuilder sb = new StringBuilder("Colonias que contienen el punto: ");
            for (int i = 0; i < coloniasEnPunto.size(); i++) {
                if (i > 0) sb.append(" / ");
                sb.append(coloniasEnPunto.get(i).getNombre());
            }
            lblColonias.setText(sb.toString());
        }

        panelTop.add(lblCoord, BorderLayout.NORTH);
        panelTop.add(lblColonias, BorderLayout.SOUTH);

        add(panelTop, BorderLayout.NORTH);

        //---------- cuerpo
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //---------- botones
        JPanel panelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbGatoExistente = new JRadioButton("Usar un gato existente");
        rbGatoNuevo = new JRadioButton("Registrar un gato nuevo");

        ButtonGroup group = new ButtonGroup();
        group.add(rbGatoExistente);
        group.add(rbGatoNuevo);

        if (!gatosEnColonias.isEmpty()) {
            rbGatoExistente.setSelected(true);
        } else {
            rbGatoExistente.setEnabled(false);
            rbGatoNuevo.setSelected(true);
        }

        panelRadios.add(rbGatoExistente);
        panelRadios.add(rbGatoNuevo);

        panelCentro.add(panelRadios);
        panelCentro.add(Box.createVerticalStrut(10));

        // Panel gato existente
        panelGatoExistente = crearPanelGatoExistente();
        panelCentro.add(panelGatoExistente);
        panelCentro.add(Box.createVerticalStrut(15));

        // Panel gato nuevo
        panelGatoNuevo = crearPanelGatoNuevo();
        panelCentro.add(panelGatoNuevo);

        actualizarVisibilidadPaneles();

        rbGatoExistente.addActionListener(e -> actualizarVisibilidadPaneles());
        rbGatoNuevo.addActionListener(e -> actualizarVisibilidadPaneles());

        JScrollPane scrollCentro = new JScrollPane(panelCentro);
        scrollCentro.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollCentro, BorderLayout.CENTER);

        // ---------------- BOTTOM ----------------
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnGuardar = new JButton("Guardar avistamiento");

        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> onGuardar());

        panelBottom.add(btnCancelar);
        panelBottom.add(btnGuardar);

        add(panelBottom, BorderLayout.SOUTH);
    }

    private JPanel crearPanelGatoExistente() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Gato existente"));

        modeloGatos = new DefaultListModel<>();
        for (Gato g : gatosEnColonias) {
            modeloGatos.addElement(g);
        }

        listaGatos = new JList<>(modeloGatos);
        listaGatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaGatos.setCellRenderer(new ListCellRenderer<Gato>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends Gato> list,
                                                          Gato value,
                                                          int index,
                                                          boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel lbl = new JLabel();
                String nombre = value.getNombre() != null ? value.getNombre() : "(sin nombre)";
                String colNombre = (value.getColonia() != null) ? value.getColonia().getNombre() : "Sin colonia";
                lbl.setText(nombre + " - " + colNombre);
                lbl.setOpaque(true);
                lbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
                if (isSelected) {
                    lbl.setBackground(new Color(180, 200, 255));
                } else {
                    lbl.setBackground(Color.WHITE);
                }
                return lbl;
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaGatos);
        scrollLista.setPreferredSize(new Dimension(300, 120));

        panel.add(scrollLista, BorderLayout.CENTER);

        JPanel panelEdicion = new JPanel();
        panelEdicion.setLayout(new BoxLayout(panelEdicion, BoxLayout.Y_AXIS));
        panelEdicion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelEdicion.add(new JLabel("Estado de salud (opcional para actualizar):"));
        cmbEstadoExistente = new JComboBox<>(new String[]{
                "", "Bueno", "Regular", "Malo", "Crítico"
        });
        panelEdicion.add(cmbEstadoExistente);
        panelEdicion.add(Box.createVerticalStrut(8));

        btnFotoExistente = new JButton("Cambiar foto (opcional)...");
        btnFotoExistente.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(NuevoAvistamientoDialog.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                fotoExistenteSeleccionada = chooser.getSelectedFile();
            }
        });
        panelEdicion.add(btnFotoExistente);

        panel.add(panelEdicion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelGatoNuevo() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Registrar nuevo gato"));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        txtNombreNuevo = new JTextField();
        txtColorNuevo = new JTextField();
        txtZonaNuevo = new JTextField();
        txtCaractNuevo = new JTextArea(3, 20);
        txtCaractNuevo.setLineWrap(true);
        txtCaractNuevo.setWrapStyleWord(true);

        cmbEstadoNuevo = new JComboBox<>(new String[]{
                "", "Bueno", "Regular", "Malo", "Crítico"
        });

        btnFotoNuevo = new JButton("Seleccionar foto...");
        btnFotoNuevo.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(NuevoAvistamientoDialog.this);
            if (res == JFileChooser.APPROVE_OPTION) {
                fotoNuevoSeleccionada = chooser.getSelectedFile();
            }
        });

        panel.add(crearFila("Nombre (opcional):", txtNombreNuevo));
        panel.add(Box.createVerticalStrut(5));
        panel.add(crearFila("Color:", txtColorNuevo));
        panel.add(Box.createVerticalStrut(5));
        panel.add(crearFila("Zona:", txtZonaNuevo));
        panel.add(Box.createVerticalStrut(5));

        JPanel filaCaract = new JPanel(new BorderLayout());
        filaCaract.add(new JLabel("Características:"), BorderLayout.NORTH);
        filaCaract.add(new JScrollPane(txtCaractNuevo), BorderLayout.CENTER);
        panel.add(filaCaract);
        panel.add(Box.createVerticalStrut(5));

        panel.add(crearFila("Estado de salud:", cmbEstadoNuevo));
        panel.add(Box.createVerticalStrut(5));

        JPanel filaFoto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filaFoto.add(btnFotoNuevo);
        panel.add(filaFoto);

        return panel;
    }

    private JPanel crearFila(String etiqueta, JComponent comp) {
        JPanel fila = new JPanel(new BorderLayout(5, 5));
        fila.add(new JLabel(etiqueta), BorderLayout.WEST);
        fila.add(comp, BorderLayout.CENTER);
        return fila;
    }

    private void actualizarVisibilidadPaneles() {
        boolean usarExistente = rbGatoExistente.isSelected() && !gatosEnColonias.isEmpty();
        panelGatoExistente.setEnabled(usarExistente);
        panelGatoNuevo.setEnabled(!usarExistente);

        for (Component c : panelGatoExistente.getComponents()) c.setEnabled(usarExistente);
        for (Component c : panelGatoNuevo.getComponents()) c.setEnabled(!usarExistente);
    }

    private void onGuardar() {
        try {
            PuntoAvistamiento nuevo;

            // ¿qué colonia usar? si solo hay una, esa. Si hay varias, se podría elegir,
            // por ahora tomamos la primera para asociar al gato nuevo / existente si hace falta.
            Colonia coloniaAsociada = coloniasEnPunto.isEmpty() ? null : coloniasEnPunto.get(0);

            if (rbGatoExistente.isSelected() && !gatosEnColonias.isEmpty()) {
                Gato seleccionado = listaGatos.getSelectedValue();
                if (seleccionado == null) {
                    JOptionPane.showMessageDialog(this,
                            "Seleccioná un gato de la lista o elegí registrar uno nuevo.",
                            "Falta seleccionar gato",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nuevoEstado = (String) cmbEstadoExistente.getSelectedItem();
                if (nuevoEstado != null && nuevoEstado.isBlank()) {
                    nuevoEstado = null;
                }

                nuevo = control.registrarAvistamientoConGatoExistente(
                        coordena,
                        seleccionado,
                        coloniaAsociada,
                        nuevoEstado,
                        fotoExistenteSeleccionada
                );

            } else {
                //---------- validaciones
                String color = txtColorNuevo.getText().trim();
                if (color.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "El color del gato es obligatorio.",
                            "Dato faltante",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String nombre = txtNombreNuevo.getText();
                String zona = txtZonaNuevo.getText();
                String caract = txtCaractNuevo.getText();
                String estadoNuevo = (String) cmbEstadoNuevo.getSelectedItem();
                if (estadoNuevo != null && estadoNuevo.isBlank()) {
                    estadoNuevo = null;
                }

                nuevo = control.registrarAvistamientoConGatoNuevo(
                        coordena,
                        coloniaAsociada,
                        nombre,
                        color,
                        caract,
                        zona,
                        estadoNuevo,
                        fotoNuevoSeleccionada
                );
            }

            JOptionPane.showMessageDialog(this,
                    "Avistamiento registrado con éxito (ID: " + nuevo.getId() + ")",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al registrar el avistamiento:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
