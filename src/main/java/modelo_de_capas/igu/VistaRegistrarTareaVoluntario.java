package modelo_de_capas.igu;

import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.HogarTransito;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class VistaRegistrarTareaVoluntario extends JPanel {

    private final MainFrame mainFrame;
    private final Controladora control;

    private CardLayout cardLayout;
    private JPanel panelCards;

    private Gato gatoSeleccionado;

    // Paso 2 – campos comunes
    private JTextField txtUbicacion;
    private JLabel lblResumenGato;
    private JLabel lblResumenVoluntario;

    // tipo de tarea
    private enum TipoTarea { NINGUNO, ALIMENTACION, CAPTURA, CONTROL_VET, TRANSPORTE }
    private TipoTarea tipoSeleccionado = TipoTarea.NINGUNO;

    // subformularios
    private JPanel panelTipoContainer;
    private JPanel panelAlimentacion;
    private JPanel panelCaptura;
    private JPanel panelControlVet;
    private JPanel panelTransporte;

    // campos ALIMENTACION
    private JTextField txtAliTipo;
    private JTextField txtAliCantidad;

    // campos CAPTURA
    private JTextField txtCapNombreVet;
    private JTextField txtCapNumeroVet;
    private JTextField txtCapDocumento;

    // campos CONTROL VET
    private JTextField txtCVNombreVet;
    private JTextField txtCVNumeroVet;
    private JTextField txtCVTipo;
    private JTextField txtCVDocumento;

    // campos TRANSPORTE
    private JTextField txtTransHoraEntrega; // formato HH:mm
    private JTextField txtTransAclaracion;

    public VistaRegistrarTareaVoluntario(MainFrame mainFrame, Controladora control) {
        this.mainFrame = mainFrame;
        this.control = control;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(230, 230, 230));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Registrar tarea de voluntario");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        add(lblTitulo, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        panelCards.add(crearPanelSeleccionGato(), "SELECCION_GATO");
        panelCards.add(crearPanelFormularioTarea(), "FORM_TAREA");

        add(panelCards, BorderLayout.CENTER);

        cardLayout.show(panelCards, "SELECCION_GATO");
    }

    //---------- seleccion de gato
    private JPanel crearPanelSeleccionGato() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 230, 230));

        JLabel lbl = new JLabel("1) Selecciona el gato para el cual vas a registrar la tarea:");
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(lbl, BorderLayout.NORTH);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panel.add(scroll, BorderLayout.CENTER);

        List<Gato> gatos = control.listarGatos();
        if (gatos == null || gatos.isEmpty()) {
            JLabel lblNo = new JLabel("No hay gatos registrados.");
            lblNo.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lblNo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedor.add(lblNo);
        } else {
            for (Gato g : gatos) {
                JPanel card = crearCardGatoSeleccion(g);
                contenedor.add(card);
                contenedor.add(Box.createVerticalStrut(10));
            }
        }

        JButton btnCancelar = new JButton("Volver");
        btnCancelar.addActionListener(e -> mainFrame.irAHome());
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(230, 230, 230));
        panelSur.add(btnCancelar);

        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearCardGatoSeleccion(Gato g) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(120, 120));
        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(scaled));
            } else {
                lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
            }
        } catch (Exception e) {
            lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(235, 235, 235));
        info.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblNombre = new JLabel(g.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel lblColor = new JLabel("Color: " + g.getColor());
        JLabel lblCaract = new JLabel("Características: " + g.getCaracteristicas());

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblColor);
        info.add(Box.createVerticalStrut(5));
        info.add(lblCaract);

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                seleccionarGatoYPasarAFormulario(g);
            }
        });

        return card;
    }

    private void seleccionarGatoYPasarAFormulario(Gato g) {
        this.gatoSeleccionado = g;
        actualizarResumenEnFormulario();
        cardLayout.show(panelCards, "FORM_TAREA");
    }

    //---------- formulario de latarea
    private JPanel crearPanelFormularioTarea() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 230, 230));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(new Color(230, 230, 230));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        lblResumenGato = new JLabel("Gato: (ninguno seleccionado)");
        lblResumenGato.setFont(new Font("SansSerif", Font.PLAIN, 16));

        lblResumenVoluntario = new JLabel("Voluntario: " + control.getUsuarioLogueado().getUsername());
        lblResumenVoluntario.setFont(new Font("SansSerif", Font.PLAIN, 16));

        header.add(lblResumenGato);
        header.add(lblResumenVoluntario);

        panel.add(header, BorderLayout.NORTH);

        // Centro: formulario general + subformularios
        JPanel centro = new JPanel();
        centro.setLayout(new BorderLayout());
        centro.setBackground(new Color(240, 240, 240));
        centro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Datos generales (ubicación)
        JPanel panelGeneral = new JPanel(new GridBagLayout());
        panelGeneral.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        panelGeneral.add(new JLabel("Ubicación:"), gbc);

        txtUbicacion = new JTextField(25);
        gbc.gridx = 1;
        panelGeneral.add(txtUbicacion, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 2;
        JLabel lblInfoFechaHora = new JLabel("La tarea se registrará con la fecha y hora actuales.");
        panelGeneral.add(lblInfoFechaHora, gbc);
        gbc.gridwidth = 1;

        centro.add(panelGeneral, BorderLayout.NORTH);

        // Botones de tipo
        JPanel panelTipos = new JPanel();
        panelTipos.setBackground(new Color(240, 240, 240));
        panelTipos.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnAli = new JButton("Alimentación");
        JButton btnCap = new JButton("Captura para castración");
        JButton btnCV = new JButton("Control veterinario");
        JButton btnTrans = new JButton("Transporte a hogar transitorio");

        panelTipos.add(btnAli);
        panelTipos.add(btnCap);
        panelTipos.add(btnCV);
        panelTipos.add(btnTrans);

        centro.add(panelTipos, BorderLayout.CENTER);

        // Panel para subformularios
        panelTipoContainer = new JPanel(new CardLayout());
        panelTipoContainer.setBackground(new Color(240, 240, 240));

        panelAlimentacion = crearPanelAlimentacion();
        panelCaptura = crearPanelCaptura();
        panelControlVet = crearPanelControlVet();
        panelTransporte = crearPanelTransporte();

        panelTipoContainer.add(new JPanel(), "NINGUNO");
        panelTipoContainer.add(panelAlimentacion, "ALIMENTACION");
        panelTipoContainer.add(panelCaptura, "CAPTURA");
        panelTipoContainer.add(panelControlVet, "CONTROL_VET");
        panelTipoContainer.add(panelTransporte, "TRANSPORTE");

        centro.add(panelTipoContainer, BorderLayout.SOUTH);

        panel.add(centro, BorderLayout.CENTER);

        // Botones abajo
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(230, 230, 230));

        JButton btnRegistrar = new JButton("Registrar tarea");
        JButton btnVolver = new JButton("Volver a selección de gato");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        panel.add(panelBotones, BorderLayout.SOUTH);

        // Listeners de tipo
        btnAli.addActionListener(e -> seleccionarTipoTarea(TipoTarea.ALIMENTACION));
        btnCap.addActionListener(e -> seleccionarTipoTarea(TipoTarea.CAPTURA));
        btnCV.addActionListener(e -> seleccionarTipoTarea(TipoTarea.CONTROL_VET));
        btnTrans.addActionListener(e -> seleccionarTipoTarea(TipoTarea.TRANSPORTE));

        btnVolver.addActionListener(e -> cardLayout.show(panelCards, "SELECCION_GATO"));
        btnRegistrar.addActionListener(e -> registrarTarea());

        return panel;
    }

    private JPanel crearPanelAlimentacion() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(235, 235, 235));
        p.setBorder(BorderFactory.createTitledBorder("Datos de Alimentación"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Tipo de alimento:"), gbc);

        txtAliTipo = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtAliTipo, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Cantidad (ej. 50g, moderada):"), gbc);

        txtAliCantidad = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtAliCantidad, gbc);

        return p;
    }

    private JPanel crearPanelCaptura() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(235, 235, 235));
        p.setBorder(BorderFactory.createTitledBorder("Datos de Captura para castración"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Nombre del veterinario:"), gbc);

        txtCapNombreVet = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCapNombreVet, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Número de contacto:"), gbc);

        txtCapNumeroVet = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCapNumeroVet, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Documento (referencia):"), gbc);

        txtCapDocumento = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCapDocumento, gbc);

        return p;
    }

    private JPanel crearPanelControlVet() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(235, 235, 235));
        p.setBorder(BorderFactory.createTitledBorder("Datos de Control veterinario"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Nombre del veterinario:"), gbc);

        txtCVNombreVet = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCVNombreVet, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Número de contacto:"), gbc);

        txtCVNumeroVet = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCVNumeroVet, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Tipo de control:"), gbc);

        txtCVTipo = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCVTipo, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Documento (referencia):"), gbc);

        txtCVDocumento = new JTextField(20);
        gbc.gridx = 1;
        p.add(txtCVDocumento, gbc);

        return p;
    }

    private JPanel crearPanelTransporte() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(235, 235, 235));
        p.setBorder(BorderFactory.createTitledBorder("Datos de Transporte a hogar transitorio"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Hora de entrega (HH:mm):"), gbc);

        txtTransHoraEntrega = new JTextField(10);
        gbc.gridx = 1;
        p.add(txtTransHoraEntrega, gbc);

        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Aclaración:"), gbc);

        txtTransAclaracion = new JTextField(25);
        gbc.gridx = 1;
        p.add(txtTransAclaracion, gbc);

        return p;
    }

    private void actualizarResumenEnFormulario() {
        if (gatoSeleccionado != null) {
            lblResumenGato.setText("Gato: " + gatoSeleccionado.getNombre());
        } else {
            lblResumenGato.setText("Gato: (ninguno seleccionado)");
        }
    }

    private void seleccionarTipoTarea(TipoTarea tipo) {
        this.tipoSeleccionado = tipo;
        CardLayout cl = (CardLayout) panelTipoContainer.getLayout();

        switch (tipo) {
            case ALIMENTACION -> cl.show(panelTipoContainer, "ALIMENTACION");
            case CAPTURA -> cl.show(panelTipoContainer, "CAPTURA");
            case CONTROL_VET -> cl.show(panelTipoContainer, "CONTROL_VET");
            case TRANSPORTE -> cl.show(panelTipoContainer, "TRANSPORTE");
            default -> cl.show(panelTipoContainer, "NINGUNO");
        }
    }

    private void registrarTarea() {
        if (gatoSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero seleccioná un gato.",
                    "Falta gato",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (tipoSeleccionado == TipoTarea.NINGUNO) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccioná el tipo de tarea (alimentación, captura, control vet o transporte).",
                    "Falta tipo de tarea",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String ubicacion = txtUbicacion.getText().trim();
        if (ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "La ubicación es obligatoria.",
                    "Faltan datos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            switch (tipoSeleccionado) {
                case ALIMENTACION -> registrarAlimentacion(ubicacion);
                case CAPTURA -> registrarCaptura(ubicacion);
                case CONTROL_VET -> registrarControlVet(ubicacion);
                case TRANSPORTE -> registrarTransporte(ubicacion);
            }

            JOptionPane.showMessageDialog(
                    this,
                    "La tarea se registró correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Volvemos al home del mainframe
            mainFrame.irAHome();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocurrió un error al registrar la tarea:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void registrarAlimentacion(String ubicacion) {
        String tipo = txtAliTipo.getText().trim();
        String cant = txtAliCantidad.getText().trim();
        control.registrarAlimentacion(gatoSeleccionado, tipo, cant, ubicacion);
    }

    private void registrarCaptura(String ubicacion) {
        String nomVet = txtCapNombreVet.getText().trim();
        String numVet = txtCapNumeroVet.getText().trim();
        String doc = txtCapDocumento.getText().trim();
        control.registrarCapturaCastracion(gatoSeleccionado, nomVet, numVet, doc, ubicacion);
    }

    private void registrarControlVet(String ubicacion) {
        String nomVet = txtCVNombreVet.getText().trim();
        String numVet = txtCVNumeroVet.getText().trim();
        String tipo = txtCVTipo.getText().trim();
        String doc = txtCVDocumento.getText().trim();
        control.registrarControlVeterinario(gatoSeleccionado, nomVet, numVet, tipo, doc, ubicacion);
    }

    private void registrarTransporte(String ubicacion) throws Exception {
        String horaEntregaStr = txtTransHoraEntrega.getText().trim();
        String aclaracion = txtTransAclaracion.getText().trim();

        if (horaEntregaStr.isEmpty()) {
            throw new IllegalArgumentException("La hora de entrega es obligatoria para el transporte.");
        }

        LocalTime horaEntrega;
        try {
            horaEntrega = LocalTime.parse(horaEntregaStr); // espera formato HH:mm
        } catch (Exception e) {
            throw new IllegalArgumentException("La hora de entrega debe tener formato HH:mm.");
        }

        // Pedimos hogar de tránsito disponible
        List<HogarTransito> hogaresDispo = control.listarHogaresTransitoDisponibles();
        if (hogaresDispo == null || hogaresDispo.isEmpty()) {
            throw new IllegalStateException("No hay hogares de tránsito disponibles.");
        }

        String[] opciones = new String[hogaresDispo.size()];
        for (int i = 0; i < hogaresDispo.size(); i++) {
            HogarTransito h = hogaresDispo.get(i);
            String nombre = (h.getNombre() != null) ? h.getNombre() : "Hogar " + h.getId();
            String dir = (h.getDireccion() != null) ? h.getDireccion() : "";
            opciones[i] = nombre + " - " + dir;
        }

        String elegido = (String) JOptionPane.showInputDialog(
                this,
                "Seleccioná el hogar de tránsito:",
                "Hogares disponibles",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (elegido == null) {
            throw new IllegalStateException("No se seleccionó hogar de tránsito. Cancelado.");
        }

        int idx = java.util.Arrays.asList(opciones).indexOf(elegido);
        HogarTransito hogarSeleccionado = hogaresDispo.get(idx);

        control.registrarTransporteHogarTransito(
                gatoSeleccionado,
                ubicacion,
                aclaracion,
                horaEntrega,
                hogarSeleccionado
        );
    }
}
