package modelo_de_capas.igu;

import modelo_de_capas.logica.Colonia;
import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Gato;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VistaRegistrarAvistamiento extends JPanel {

    private final Controladora control;
    private final MainFrame mainFrame;

    private MapaPanel mapaPanel;
    private JList<Gato> listaGatos;
    private DefaultListModel<Gato> modeloLista;

    private JButton btnNuevoGato;
    private JButton btnRegistrarAvistamiento;

    // coordenada elegida en el mapa (lat,lon)
    private String coordSeleccionada;
    // colonia donde cae esa coordenada (si hay)
    private Colonia coloniaActual;

    public VistaRegistrarAvistamiento(MainFrame mainFrame, Controladora control) {
        this.mainFrame = mainFrame;
        this.control = control;

        setLayout(new BorderLayout());

        initUI();
    }

    // ========================= UI =========================
    private void initUI() {

        //---------- lista gatos
        JPanel panelIzq = new JPanel(new BorderLayout());
        panelIzq.setPreferredSize(new Dimension(350, 0));

        JLabel lblTitulo = new JLabel("Gatos detectados en la colonia:");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modeloLista = new DefaultListModel<>();
        listaGatos = new JList<>(modeloLista);
        listaGatos.setFixedCellHeight(80);
        listaGatos.setCellRenderer(new GatoRenderer());

        JScrollPane scroll = new JScrollPane(listaGatos);

        btnNuevoGato = new JButton("Registrar nuevo gato");
        btnRegistrarAvistamiento = new JButton("Registrar avistamiento");

        JPanel panelBotones = new JPanel(new GridLayout(2, 1, 0, 5));
        panelBotones.add(btnNuevoGato);
        panelBotones.add(btnRegistrarAvistamiento);

        panelIzq.add(lblTitulo, BorderLayout.NORTH);
        panelIzq.add(scroll, BorderLayout.CENTER);
        panelIzq.add(panelBotones, BorderLayout.SOUTH);

        //---------- mapa
        mapaPanel = new MapaPanel();

        mapaPanel.setListenerClick((lat, lon) -> {
            coordSeleccionada = String.format(java.util.Locale.US, "%.6f,%.6f", lat, lon);
            coloniaActual = null;
            modeloLista.clear();

            // buscar colonias que contengan el punto 
            List<Colonia> colonias = control.buscarColoniasQueContienenPunto(lat, lon);
            if (colonias == null || colonias.isEmpty()) {
                // clic fuera de todas las colonias
                JOptionPane.showMessageDialog(
                        this,
                        "El punto seleccionado no pertenece a ninguna colonia registrada.",
                        "Fuera de colonia",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // por ahora usamos la primera
            coloniaActual = colonias.get(0);

            // cargar gatos de ESA colonia a partir de todos los gatos (no anda no se porque)
            List<Gato> todos = control.listarGatos();
            if (todos != null) {
                for (Gato g : todos) {
                    if (g.getColonia() != null &&
                        g.getColonia().getId().equals(coloniaActual.getId())) {
                        modeloLista.addElement(g);
                    }
                }
            }
        });

        //---------- listeners de los botones
        btnNuevoGato.addActionListener(e -> onNuevoGato());
        btnRegistrarAvistamiento.addActionListener(e -> onRegistrarAvistamiento());

        //---------- pantalla organizacion
        add(panelIzq, BorderLayout.WEST);
        add(mapaPanel, BorderLayout.CENTER);
    }

    //---------- nuevo gato
    private void onNuevoGato() {
        if (coordSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero seleccioná un punto en el mapa.",
                    "Coordenada faltante",
                    JOptionPane.WARNING_MESSAGE
            );
        return;
        }

        if (coloniaActual == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "El punto no pertenece a ninguna colonia.",
                    "Fuera de colonia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JTextField txtNombre = new JTextField();
        JTextField txtColor = new JTextField();
        JTextField txtCaract = new JTextField();
        JTextField txtZona = new JTextField();

        String[] estados = {"Sin datos", "Bueno", "Regular", "Malo", "Crítico"};
        JComboBox<String> cmbEstado = new JComboBox<>(estados);

        final File[] fotoSel = { null };
        JButton btnFoto = new JButton("Seleccionar foto");
        btnFoto.addActionListener(ev -> {
            JFileChooser ch = new JFileChooser();
            if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                fotoSel[0] = ch.getSelectedFile();
            }
        });

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Nombre (opcional, por defecto 'Gato'):"));
        panel.add(txtNombre);
        panel.add(new JLabel("Color:"));
        panel.add(txtColor);
        panel.add(new JLabel("Características:"));
        panel.add(txtCaract);
        panel.add(new JLabel("Zona:"));
        panel.add(txtZona);
        panel.add(new JLabel("Estado de salud:"));
        panel.add(cmbEstado);
        panel.add(btnFoto);

        int r = JOptionPane.showConfirmDialog(
                this, panel,
                "Registrar nuevo gato",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (r != JOptionPane.OK_OPTION) return;

        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) nombre = "Gato";

        String color = txtColor.getText().trim();
        String caract = txtCaract.getText().trim();
        String zona = txtZona.getText().trim();
        String estado = (String) cmbEstado.getSelectedItem();

        // crear gato
        Gato g = new Gato();
        g.setNombre(nombre);
        g.setColor(color);
        g.setCaracteristicas(caract + (zona.isEmpty() ? "" : " | Zona: "+zona));
        g.setEstadoSalud(estado);
        g.setColonia(coloniaActual);

        control.crearGato(g, fotoSel[0]);

        // registrar también el avistamiento usando el gato nuevo
        control.registrarAvistamientoConGatoExistente(
                coordSeleccionada,
                g,
                coloniaActual,
                estado,
                fotoSel[0]
        );

        // recargar lista
        recargarListaGatosColonia();

        JOptionPane.showMessageDialog(
                this,
                "Gato y avistamiento registrados correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //---------- registro de avistamiento (si esta el gato en la lista)
    private void onRegistrarAvistamiento() {

        if (coordSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero seleccioná un punto en el mapa.",
                    "Coordenada faltante",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (coloniaActual == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "El punto no pertenece a ninguna colonia.",
                    "Fuera de colonia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Gato seleccionado = listaGatos.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná un gato de la lista.",
                    "Gato faltante",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // pedir estado y foto
        String[] estados = {"Sin datos", "Bueno", "Regular", "Malo", "Crítico"};
        JComboBox<String> cmbEstado = new JComboBox<>(estados);

        final File[] fotoSel = { null };
        JButton btnFoto = new JButton("Seleccionar foto");
        btnFoto.addActionListener(ev -> {
            JFileChooser ch = new JFileChooser();
            if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                fotoSel[0] = ch.getSelectedFile();
            }
        });

        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Estado de salud (opcional):"));
        panel.add(cmbEstado);
        panel.add(btnFoto);

        int r = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Datos del avistamiento",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (r != JOptionPane.OK_OPTION) return;

        String estado = (String) cmbEstado.getSelectedItem();
        if (estado != null && !estado.isBlank()) {
            seleccionado.setEstadoSalud(estado);
            control.actualizarGato(seleccionado, fotoSel[0]); // también actualizamos foto si pasó una nueva
        }

        // registrar avistamiento
        control.registrarAvistamientoConGatoExistente(
                coordSeleccionada,
                seleccionado,
                coloniaActual,
                estado,
                fotoSel[0]
        );

        JOptionPane.showMessageDialog(
                this,
                "Avistamiento registrado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    //---------- se recarga la lista cada vez que se pone otro punto
    private void recargarListaGatosColonia() {
        modeloLista.clear();
        if (coloniaActual == null) return;

        List<Gato> todos = control.listarGatos();
        if (todos == null) return;

        for (Gato g : todos) {
            if (g.getColonia() != null &&
                g.getColonia().getId().equals(coloniaActual.getId())) {
                modeloLista.addElement(g);
            }
        }
    }

    //---------- lista gatos mas las fotos
    private static class GatoRenderer extends JPanel implements ListCellRenderer<Gato> {

        private final JLabel lblFoto = new JLabel();
        private final JLabel lblTexto = new JLabel();

        public GatoRenderer() {
            setLayout(new BorderLayout(10, 10));
            lblFoto.setPreferredSize(new Dimension(60, 60));
            lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 14));
            add(lblFoto, BorderLayout.WEST);
            add(lblTexto, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Gato> list,
                Gato gato,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            if (gato != null) {
                lblTexto.setText(gato.getNombre() + " (" + gato.getColor() + ")");
                try {
                    String path = gato.getFotoPath();
                    if (path != null && !path.isBlank()) {
                        ImageIcon icon = new ImageIcon(path);
                        Image img = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        lblFoto.setIcon(new ImageIcon(img));
                    } else {
                        lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
                    }
                } catch (Exception e) {
                    lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
                }
            }

            setBackground(isSelected ? new Color(220, 220, 220) : Color.WHITE);
            return this;
        }
    }

}
