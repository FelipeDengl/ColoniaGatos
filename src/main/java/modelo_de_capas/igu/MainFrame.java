package modelo_de_capas.igu;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Diagnostico;
import modelo_de_capas.logica.Estudio;
import modelo_de_capas.logica.Familia;
import modelo_de_capas.logica.Gato;
import modelo_de_capas.logica.Usuario;
import modelo_de_capas.logica.PuntoAvistamiento;
import modelo_de_capas.logica.Voluntario;
import modelo_de_capas.logica.HogarTransito;
import modelo_de_capas.logica.Tratamiento;

public class MainFrame extends JFrame {

    //---------- header
    private JPanel headerPanel;
    private JButton btnVoluntarios;
    private JButton btnFamilias;
    private JButton btnVeterinarios;
    private JButton btnGatos;
    private JButton btnAvatar;
    private JLabel lblTitulo;

    //---------- vistas
    private JPanel panelCentralActual;
    private JPanel panelHome;

    //---------- home
    private JPanel panelGatos;
    private JPanel panelUsuario;

    private final Controladora control;

    // Coordenada seleccionada (mapa viejo no anda
    private String coordenaSeleccionada;

    //---------- main
    public MainFrame(Controladora control) {
        this.control = control;

        setTitle("Colonia Gatos Posadas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initHeader();
        initContent();
    }

    //---------- vista (cambiar entre las diferentes "ventanas")
    public void setVistaCentral(JPanel nuevaVista) {
        if (panelCentralActual != null) {
            getContentPane().remove(panelCentralActual);
        }
        panelCentralActual = nuevaVista;
        getContentPane().add(panelCentralActual, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void irAHome() {
        setVistaCentral(panelHome);
    }

//---------- header
    private void initHeader() {
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 140, 77));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(0, 70));

        lblTitulo = new JLabel("  Colonia Gatos Posadas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 30));

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);

        btnVoluntarios = new JButton("Voluntarios");
        btnFamilias = new JButton("Familias");
        btnVeterinarios = new JButton("Veterinarios");
        btnGatos = new JButton("Gatos");

        //---------- la lista de voluntarios arriba en voluntarios
        btnVoluntarios.addActionListener(e -> mostrarVistaListaVoluntarios());

        btnGatos.addActionListener(e -> mostrarVistaListaGatos());

        menuPanel.add(btnVoluntarios);
        menuPanel.add(btnFamilias);
        menuPanel.add(btnVeterinarios);
        menuPanel.add(btnGatos);

        btnAvatar = new JButton("👤");
        btnAvatar.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnAvatar.setPreferredSize(new Dimension(60, 60));

        btnAvatar.addActionListener(e -> {
            setVistaCentral(panelHome);
            mostrarPanelUsuarioActual();
        });

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(menuPanel, BorderLayout.CENTER);
        headerPanel.add(btnAvatar, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void mostrarPanelUsuarioActual() {
        Usuario u = control.getUsuarioLogueado();
        if (u == null) mostrarPanelInvitado();
        else mostrarPanelUsuarioLogueado(u);
    }

//---------- home (icono de usuiario hay que cambiar)
    private void initContent() {

        panelHome = new JPanel(new BorderLayout());
        panelHome.setBackground(new Color(230, 230, 230));
        panelHome.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        panelGatos = new JPanel();
        panelGatos.setLayout(new BoxLayout(panelGatos, BoxLayout.Y_AXIS));
        panelGatos.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(panelGatos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panelUsuario = new JPanel();
        panelUsuario.setPreferredSize(new Dimension(320, 0));
        panelUsuario.setBackground(new Color(230, 230, 230));
        panelUsuario.setLayout(new BorderLayout());

        panelHome.add(scroll, BorderLayout.CENTER);
        panelHome.add(panelUsuario, BorderLayout.EAST);

        cargarListaGatos();
        mostrarPanelInvitado();

        setVistaCentral(panelHome);
    }
//---------- gatos
    private void cargarListaGatos() {
        panelGatos.removeAll();

        List<Gato> gatos = control.listarGatosSinAsignacion();

        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos registrados.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelGatos.add(lbl);
            panelGatos.revalidate();
            panelGatos.repaint();
            return;
        }

        for (Gato g : gatos) {
            panelGatos.add(crearCardGatoHome(g));
            panelGatos.add(Box.createVerticalStrut(20));
        }

        panelGatos.revalidate();
        panelGatos.repaint();
    }

    private JPanel crearCardGatoHome(Gato g) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(350, 350));

        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(scaled));
            } else lblImg.setIcon(new ImageIcon("placeholder_gato.png"));

        } catch (Exception e) {
            lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(240, 240, 240));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        Font infoFont = new Font("SansSerif", Font.PLAIN, 18);

        infoPanel.add(crearLabel("Nombre: " + g.getNombre(), infoFont));
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(crearLabel("Color: " + g.getColor(), infoFont));
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(crearLabel("Características: " + g.getCaracteristicas(), infoFont));
        infoPanel.add(Box.createVerticalStrut(8));

        String est = (g.getEstadoSalud() != null) ? g.getEstadoSalud() : "Sin datos";
        infoPanel.add(crearLabel("Estado de salud: " + est, infoFont));

        card.add(lblImg, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel crearLabel(String texto, Font font) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(font);
        return lbl;
    }

//---------- gatos
    private void mostrarVistaListaGatos() {

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(230, 230, 230));
        panelLista.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel contenedorGatos = new JPanel();
        contenedorGatos.setLayout(new BoxLayout(contenedorGatos, BoxLayout.Y_AXIS));
        contenedorGatos.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedorGatos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panelLista.add(scroll, BorderLayout.CENTER);

        contenedorGatos.removeAll();
        List<Gato> gatos = control.listarGatosSinAsignacion();


        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos registrados.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedorGatos.add(lbl);
        } else {
            for (Gato g : gatos) {
                JPanel card = crearCardGatoLista(g);
                contenedorGatos.add(card);
                contenedorGatos.add(Box.createVerticalStrut(15));
            }
        }

        contenedorGatos.revalidate();
        contenedorGatos.repaint();

        setVistaCentral(panelLista);
    }

    private JPanel crearCardGatoLista(Gato g) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(150, 150));
        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(scaled));
            } else lblImg.setIcon(new ImageIcon("placeholder_gato.png"));

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
                mostrarVistaPerfilGato(g);
            }
        });

        return card;
    }
    
//---------- perfil gatoss
    private void mostrarVistaPerfilGato(Gato gato) {

        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(220, 220, 220));
        panelPerfil.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(190, 190, 190));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(300, 300));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            String ruta = gato.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaled));
            } else lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
        } catch (Exception e) {
            lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(190, 190, 190));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblNombre = new JLabel(gato.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 30));

        JLabel lblTexto = new JLabel("<html>"
                + "Color: " + gato.getColor() + "<br><br>"
                + "Características: " + gato.getCaracteristicas()
                + "</html>");
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 18));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblTexto);

        panelTop.add(lblFoto, BorderLayout.WEST);
        panelTop.add(panelInfo, BorderLayout.CENTER);

        panelPerfil.add(panelTop, BorderLayout.NORTH);

//---------- avistamiento
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(new Color(210, 210, 210));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblAv = new JLabel("Avistamientos:");
        lblAv.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblAv.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelBottom.add(lblAv, BorderLayout.NORTH);

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(
                        new Object[]{"Avistamiento", "Dirección", "Fecha", "Hora", "Estado de salud"}, 0) {
                    @Override
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };

        List<PuntoAvistamiento> lista = control.listarAvistamientosPorGato(gato);

        if (lista != null) {
            for (PuntoAvistamiento p : lista) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getCoordena(),
                        p.getFecha(),
                        p.getHora(),
                        p.getEstadosalud()
                });
            }
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        panelBottom.add(scrollTabla, BorderLayout.CENTER);

        panelPerfil.add(panelBottom, BorderLayout.CENTER);

        setVistaCentral(panelPerfil);
    }

//---------- lista voluntairos
    private void mostrarVistaListaVoluntarios() {

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(230, 230, 230));
        panelLista.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panelLista.add(scroll, BorderLayout.CENTER);

        contenedor.removeAll();
        List<Voluntario> voluntarios = control.listarVoluntarios();

        if (voluntarios == null || voluntarios.isEmpty()) {
            JLabel lbl = new JLabel("No hay voluntarios registrados.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedor.add(lbl);
        } else {
            for (Voluntario v : voluntarios) {
                JPanel card = crearCardVoluntarioLista(v);
                contenedor.add(card);
                contenedor.add(Box.createVerticalStrut(15));
            }
        }

        contenedor.revalidate();
        contenedor.repaint();

        setVistaCentral(panelLista);
    }

    private JPanel crearCardVoluntarioLista(Voluntario v) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(100, 100));
        try {
            ImageIcon icon = new ImageIcon("user_icon.png"); // poné el .png en la raíz del proyecto o recursos
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblImg.setText("👤");
            lblImg.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(235, 235, 235));
        info.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblNombre = new JLabel(v.getNombre() != null ? v.getNombre() : "(Sin nombre)");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel lblDni = new JLabel("DNI: " + (v.getDni() != null ? v.getDni() : "-"));
        JLabel lblUser = new JLabel("Usuario: " + (v.getUsername() != null ? v.getUsername() : "-"));
        String rep = (v.getReputacion() != null) ? String.format("Reputación: %.1f", v.getReputacion()) : "Reputación: sin datos";
        JLabel lblRep = new JLabel(rep);

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblDni);
        info.add(Box.createVerticalStrut(5));
        info.add(lblUser);
        info.add(Box.createVerticalStrut(5));
        info.add(lblRep);

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarVistaPerfilVoluntario(v);
            }
        });

        return card;
    }

//---------- perfil
    private void mostrarVistaPerfilVoluntario(Voluntario voluntario) {

        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(220, 220, 220));
        panelPerfil.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        // TOP: foto + info del voluntario
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(190, 190, 190));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(200, 200));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            ImageIcon icon = new ImageIcon("user_icon.png");
            Image scaled = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblFoto.setText("👤");
        }

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(190, 190, 190));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblNombre = new JLabel(voluntario.getNombre() != null ? voluntario.getNombre() : "(Sin nombre)");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel lblDni = new JLabel("DNI: " + (voluntario.getDni() != null ? voluntario.getDni() : "-"));
        JLabel lblUser = new JLabel("Usuario: " + (voluntario.getUsername() != null ? voluntario.getUsername() : "-"));
        String rep = (voluntario.getReputacion() != null)
                ? String.format("Reputación: %.1f", voluntario.getReputacion())
                : "Reputación: sin datos";
        JLabel lblRep = new JLabel(rep);

        lblDni.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblUser.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblRep.setFont(new Font("SansSerif", Font.PLAIN, 18));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(15));
        panelInfo.add(lblDni);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblUser);
        panelInfo.add(Box.createVerticalStrut(5));
        panelInfo.add(lblRep);

        panelTop.add(lblFoto, BorderLayout.WEST);
        panelTop.add(panelInfo, BorderLayout.CENTER);

        panelPerfil.add(panelTop, BorderLayout.NORTH);

        //---------- hogares transito
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(new Color(210, 210, 210));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHogares = new JLabel("Hogares de tránsito del voluntario:");
        lblHogares.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHogares.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelBottom.add(lblHogares, BorderLayout.NORTH);

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(
                        new Object[]{"Nombre", "Dirección", "Disponible", "Características"}, 0) {
                    @Override
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };

        List<HogarTransito> hogares = control.listarHogaresTransitoPorVoluntario(voluntario);

        if (hogares != null) {
            for (HogarTransito h : hogares) {
                String disponible = (h.getDisponible() != null && h.getDisponible()) ? "Sí" : "No";
                modelo.addRow(new Object[]{
                        h.getNombre(),
                        h.getDireccion(),
                        disponible,
                        h.getCaracteristica()
                });
            }
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        panelBottom.add(scrollTabla, BorderLayout.CENTER);

        panelPerfil.add(panelBottom, BorderLayout.CENTER);

        setVistaCentral(panelPerfil);
    }

//---------- panel de funciones

    private void mostrarPanelInvitado() {
        panelUsuario.removeAll();
        panelUsuario.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(new Color(120, 120, 120));
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        Font btnFont = new Font("SansSerif", Font.BOLD, 18);

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFont(btnFont);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(220, 45));

        JButton btnRegister = new JButton("Registrarse");
        btnRegister.setFont(btnFont);
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(220, 45));

        card.add(Box.createVerticalStrut(10));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(15));
        card.add(btnRegister);
        card.add(Box.createVerticalGlue());

        panelUsuario.add(card);

        panelUsuario.revalidate();
        panelUsuario.repaint();

        btnLogin.addActionListener(e -> abrirLogin());
        btnRegister.addActionListener(e -> abrirRegistro());
    }

    private void mostrarPanelUsuarioLogueado(Usuario u) {
        panelUsuario.removeAll();
        panelUsuario.setLayout(new BorderLayout());

        JPanel card = new JPanel();
        card.setBackground(new Color(200, 200, 200));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblHola = new JLabel("Hola, " + u.getUsername());
        lblHola.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblHola.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnLogout = new JButton("Cerrar sesión");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(180, 35));

        card.add(lblHola);
        card.add(Box.createVerticalStrut(15));

        //---------- registrar colonia
        if (control.esAdministradorActual()) {
            JButton btnColonia = new JButton("Registrar colonia");
            btnColonia.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnColonia.setMaximumSize(new Dimension(220, 35));
            card.add(btnColonia);
            card.add(Box.createVerticalStrut(10));

            btnColonia.addActionListener(e -> abrirDialogRegistrarColonia());
        }

        //---------- avistamiento
        if (control.esVoluntarioActual()) {
            JButton btnAvistamiento = new JButton("Registrar avistamiento");
            btnAvistamiento.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnAvistamiento.setMaximumSize(new Dimension(220, 35));

            JButton btnHogarTransito = new JButton("Registrar hogar de tránsito");
            btnHogarTransito.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnHogarTransito.setMaximumSize(new Dimension(220, 35));

            JButton btnTarea = new JButton("Registrar tarea");
            btnTarea.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnTarea.setMaximumSize(new Dimension(220, 35));

            JButton btnVerGatosHogares = new JButton("Ver gatos en mis hogares");
            btnVerGatosHogares.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnVerGatosHogares.setMaximumSize(new Dimension(220, 35));

            card.add(btnAvistamiento);
            card.add(Box.createVerticalStrut(10));
            card.add(btnHogarTransito);
            card.add(Box.createVerticalStrut(10));
            card.add(btnTarea);
            card.add(Box.createVerticalStrut(10));
            card.add(btnVerGatosHogares);
            card.add(Box.createVerticalStrut(10));

            btnAvistamiento.addActionListener(e -> abrirVistaRegistrarAvistamiento());
            btnHogarTransito.addActionListener(e -> abrirVistaRegistrarHogarTransito());
            btnTarea.addActionListener(e -> abrirVistaRegistrarTareaVoluntario());
            btnVerGatosHogares.addActionListener(e -> abrirVistaGatosEnMisHogares());
        }
        if (control.esFamiliaActual()) {
            JButton btnPostularAdopcion = new JButton("Postularse para adopción");
            btnPostularAdopcion.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnPostularAdopcion.setMaximumSize(new Dimension(220, 35));
            card.add(Box.createVerticalStrut(10));
            card.add(btnPostularAdopcion);

            btnPostularAdopcion.addActionListener(e -> abrirVistaPostulacionAdopcion());
        }
        if (control.esVeterinarioActual()) {
            JButton btnVerGatosVet = new JButton("Ver gatos (veterinario)");
            btnVerGatosVet.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnVerGatosVet.setMaximumSize(new Dimension(260, 35));
            card.add(Box.createVerticalStrut(10));
            card.add(btnVerGatosVet);

            btnVerGatosVet.addActionListener(e -> mostrarVistaListaGatosVeterinario());
        }


        
        

        card.add(btnLogout);
        card.add(Box.createVerticalGlue());

        panelUsuario.add(card, BorderLayout.NORTH);

        panelUsuario.revalidate();
        panelUsuario.repaint();

        btnLogout.addActionListener(e -> {
            control.logout();
            mostrarPanelInvitado();
            setVistaCentral(panelHome);
        });
    }

    //---------- ventanas 
    private void abrirDialogRegistrarColonia() {
        RegistrarColoniaDialog dialog = new RegistrarColoniaDialog(this, true, control);
        dialog.setVisible(true);
    }

    private void abrirLogin() {
        LoginDialog dialog = new LoginDialog(this, control);
        dialog.setVisible(true);
    }

    private void abrirRegistro() {
        RegisterDialog dialog = new RegisterDialog(this, true, control);
        dialog.setVisible(true);
    }

    public void onLoginSuccess() {
        Usuario u = control.getUsuarioLogueado();
        if (u != null) mostrarPanelUsuarioLogueado(u);
        else mostrarPanelInvitado();
    }

    private void abrirVistaRegistrarAvistamiento() {
        VistaRegistrarAvistamiento vista = new VistaRegistrarAvistamiento(this, control);
        setVistaCentral(vista);
    }

    private void abrirVistaRegistrarHogarTransito() {
        if (!control.esVoluntarioActual()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo un voluntario puede crear un hogar de tránsito.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        VistaRegistrarHogarTransito vista = new VistaRegistrarHogarTransito(this, control);
        setVistaCentral(vista);
    }

    public void setCoordenaSeleccionadaDesdeMapa(double lat, double lon) {
        this.coordenaSeleccionada =
                String.format(java.util.Locale.US, "%.6f,%.6f", lat, lon);
    }

    public void abrirDialogNuevoAvistamiento() {
        JOptionPane.showMessageDialog(this,
                "Esta versión ya no usa diálogos.\nSe usa la nueva vista integrada.");
    }
    
    private void abrirVistaRegistrarTareaVoluntario() {
        if (!control.esVoluntarioActual()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo un voluntario puede registrar tareas.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        VistaRegistrarTareaVoluntario vista = new VistaRegistrarTareaVoluntario(this, control);
        setVistaCentral(vista);
    }

    private void abrirVistaGatosEnMisHogares() {
        if (!control.esVoluntarioActual()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo un voluntario puede ver gatos en hogares de tránsito.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(230, 230, 230));
        panelLista.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Gatos en mis hogares de tránsito");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panelLista.add(lblTitulo, BorderLayout.NORTH);

        JPanel contenedorGatos = new JPanel();
        contenedorGatos.setLayout(new BoxLayout(contenedorGatos, BoxLayout.Y_AXIS));
        contenedorGatos.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedorGatos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panelLista.add(scroll, BorderLayout.CENTER);

        List<Gato> gatos = control.listarGatosEnMisHogaresNoAsignados();

        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos actualmente en tus hogares de tránsito (o ya fueron asignados a una familia).");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedorGatos.add(lbl);
        } else {
            for (Gato g : gatos) {
                JPanel card = crearCardGatoEnHogar(g);
                contenedorGatos.add(card);
                contenedorGatos.add(Box.createVerticalStrut(15));
            }
        }

        setVistaCentral(panelLista);
    }

    private JPanel crearCardGatoEnHogar(Gato g) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(130, 130));
        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
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
        info.add(Box.createVerticalStrut(10));
        info.add(new JLabel("Click para registrar visita o asignar a familia"));

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarOpcionesGatoEnHogar(g);
            }
        });

        return card;
    }
    
    private void mostrarOpcionesGatoEnHogar(Gato g) {
        Object[] opciones = {"Registrar visita", "Asignar a familia", "Cancelar"};
        int op = JOptionPane.showOptionDialog(
                this,
                "¿Qué querés hacer con " + g.getNombre() + "?",
                "Acción sobre gato en hogar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (op == 0) { // Registrar visita
            registrarVisitaDesdeUI(g);
        } else if (op == 1) { // Asignar a familia
            asignarFamiliaDesdeUI(g);
        }
    }
    
    private void registrarVisitaDesdeUI(Gato gato) {
        List<Familia> familias = control.listarFamilias();
        if (familias == null || familias.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay familias registradas en el sistema.",
                    "Sin familias",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] opciones = new String[familias.size()];
        for (int i = 0; i < familias.size(); i++) {
            Familia f = familias.get(i);
            String nom = (f.getNombre() != null) ? f.getNombre() : "Familia " + f.getId();
            String dir = (f.getDireccion() != null) ? f.getDireccion() : "";
            opciones[i] = nom + " - " + dir;
        }

        String elegido = (String) JOptionPane.showInputDialog(
                this,
                "Seleccioná la familia a la que se realiza la visita:",
                "Familias",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (elegido == null) return; // canceló

        int idx = java.util.Arrays.asList(opciones).indexOf(elegido);
        Familia familiaSeleccionada = familias.get(idx);

        String descripcion = JOptionPane.showInputDialog(
                this,
                "Descripción de la visita (observaciones, estado del gato, etc.):",
                "Descripción",
                JOptionPane.PLAIN_MESSAGE
        );

        if (descripcion == null) return; // canceló

        descripcion = descripcion.trim();
        if (descripcion.isEmpty()) {
            descripcion = "Sin descripción";
        }

        try {
            control.registrarVisitaSeguimiento(gato, familiaSeleccionada, descripcion);
            JOptionPane.showMessageDialog(
                    this,
                    "Visita registrada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar la visita:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void asignarFamiliaDesdeUI(Gato gato) {
        List<Familia> familias = control.listarFamilias();
        if (familias == null || familias.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay familias registradas en el sistema.",
                    "Sin familias",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String[] opciones = new String[familias.size()];
        for (int i = 0; i < familias.size(); i++) {
            Familia f = familias.get(i);
            String nom = (f.getNombre() != null) ? f.getNombre() : "Familia " + f.getId();
            String dir = (f.getDireccion() != null) ? f.getDireccion() : "";
            opciones[i] = nom + " - " + dir;
        }

        String elegido = (String) JOptionPane.showInputDialog(
                this,
                "Seleccioná la familia a la que se asigna el gato:",
                "Asignar a familia",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (elegido == null) return; // canceló

        int idx = java.util.Arrays.asList(opciones).indexOf(elegido);
        Familia familiaSeleccionada = familias.get(idx);

        try {
            control.registrarAsignacionFamilia(gato, familiaSeleccionada);
            JOptionPane.showMessageDialog(
                    this,
                    "Gato asignado a la familia correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // después de asignar, recargamos la vista para que desaparezca
            abrirVistaGatosEnMisHogares();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al asignar el gato a la familia:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    private void abrirVistaPostulacionAdopcion() {
        if (!control.esFamiliaActual()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo una familia puede postularse para adopción.",
                    "Acceso restringido",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 230, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        //---------- titulo
        JLabel lblTitulo = new JLabel("Postularse para adopción");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblTitulo, BorderLayout.NORTH);

        //---------- lista de gatos
        JPanel contenedorGatos = new JPanel();
        contenedorGatos.setLayout(new BoxLayout(contenedorGatos, BoxLayout.Y_AXIS));
        contenedorGatos.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedorGatos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        panel.add(scroll, BorderLayout.CENTER);

        //---------- check box
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setOpaque(false);

        JCheckBox chkSoloAptos = new JCheckBox("Mostrar solo gatos aptos para adopción");
        chkSoloAptos.setOpaque(false);
        panelSur.add(chkSoloAptos);

        panel.add(panelSur, BorderLayout.SOUTH);

        //----------  Acción del checkbox
        chkSoloAptos.addActionListener(e -> {
            recargarListadoGatosPostulacion(contenedorGatos, chkSoloAptos.isSelected());
        });

        
        recargarListadoGatosPostulacion(contenedorGatos, chkSoloAptos.isSelected());

        setVistaCentral(panel);
    }

    private void recargarListadoGatosPostulacion(JPanel contenedorGatos, boolean soloAptos) {
        contenedorGatos.removeAll();

        List<Gato> gatos = control.listarGatosEnHogarParaAdopcion(soloAptos);

        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos disponibles en hogares de tránsito para adopción con el filtro actual.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedorGatos.add(lbl);
        } else {
            for (Gato g : gatos) {
                HogarTransito hogar = control.obtenerHogarActualDeGato(g);
                JPanel card = crearCardGatoPostulacion(g, hogar);
                contenedorGatos.add(card);
                contenedorGatos.add(Box.createVerticalStrut(15));
            }
        }

        contenedorGatos.revalidate();
        contenedorGatos.repaint();
    }
    
    private JPanel crearCardGatoPostulacion(Gato g, HogarTransito hogar) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        //---------- imagen
        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(150, 150));
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(scaled));
            } else {
                lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
            }
        } catch (Exception e) {
            lblImg.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        // Info
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(235, 235, 235));
        info.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblNombre = new JLabel(g.getNombre() != null ? g.getNombre() : "Gato");
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 18));

        JLabel lblColor = new JLabel("Color: " + (g.getColor() != null ? g.getColor() : "-"));
        JLabel lblCaract = new JLabel("Características: " + (g.getCaracteristicas() != null ? g.getCaracteristicas() : "-"));
        JLabel lblSalud = new JLabel("Estado de salud: " + (g.getEstadoSalud() != null ? g.getEstadoSalud() : "Sin datos"));

        String nombreHogar = (hogar != null && hogar.getNombre() != null) ? hogar.getNombre() : "Hogar de tránsito";
        String direccionHogar = (hogar != null && hogar.getDireccion() != null) ? hogar.getDireccion() : "Dirección no registrada";

        JLabel lblHogar = new JLabel("Hogar de tránsito: " + nombreHogar);
        JLabel lblDirHogar = new JLabel("Dirección: " + direccionHogar);

        Boolean apto = g.getAptoAdopcion();
        String txtApto = (apto != null && apto) ? "Sí" : "No";
        JLabel lblApto = new JLabel("Apto para adopción: " + txtApto);

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblColor);
        info.add(Box.createVerticalStrut(5));
        info.add(lblCaract);
        info.add(Box.createVerticalStrut(5));
        info.add(lblSalud);
        info.add(Box.createVerticalStrut(8));
        info.add(lblHogar);
        info.add(Box.createVerticalStrut(3));
        info.add(lblDirHogar);
        info.add(Box.createVerticalStrut(8));
        info.add(lblApto);
        info.add(Box.createVerticalStrut(10));

        //---------- postulacion
        JButton btnPostular = new JButton("Postularme por este gato");
        btnPostular.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnPostular.addActionListener(e -> postularsePorGato(g));

        info.add(btnPostular);

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        return card;
    }
    
    private void postularsePorGato(Gato g) {
        int conf = JOptionPane.showConfirmDialog(
                this,
                "¿Querés postularte para adoptar a " + g.getNombre() + "?",
                "Confirmar postulación",
                JOptionPane.YES_NO_OPTION
        );

        if (conf != JOptionPane.YES_OPTION) return;

        try {
            control.crearPostulacionParaFamiliaActual(g);
            JOptionPane.showMessageDialog(
                    this,
                    "Postulación registrada con estado Pendiente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar la postulación:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
        private void mostrarVistaListaGatosVeterinario() {

        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBackground(new Color(230, 230, 230));
        panelLista.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel contenedorGatos = new JPanel();
        contenedorGatos.setLayout(new BoxLayout(contenedorGatos, BoxLayout.Y_AXIS));
        contenedorGatos.setBackground(new Color(240, 240, 240));

        JScrollPane scroll = new JScrollPane(contenedorGatos);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        panelLista.add(scroll, BorderLayout.CENTER);

        contenedorGatos.removeAll();
        List<Gato> gatos = control.listarGatos();

        if (gatos == null || gatos.isEmpty()) {
            JLabel lbl = new JLabel("No hay gatos registrados.");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lbl.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contenedorGatos.add(lbl);
        } else {
            for (Gato g : gatos) {
                JPanel card = crearCardGatoListaVeterinario(g);
                contenedorGatos.add(card);
                contenedorGatos.add(Box.createVerticalStrut(15));
            }
        }

        contenedorGatos.revalidate();
        contenedorGatos.repaint();

        setVistaCentral(panelLista);
    }

    private JPanel crearCardGatoListaVeterinario(Gato g) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(220, 220, 220));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImg = new JLabel();
        lblImg.setPreferredSize(new Dimension(150, 150));
        try {
            String ruta = g.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
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
        JLabel lblEstado = new JLabel("Salud: " + (g.getEstadoSalud() != null ? g.getEstadoSalud() : "Sin datos"));
        JLabel lblApto = new JLabel("Apto adopción: " + (Boolean.TRUE.equals(g.getAptoAdopcion()) ? "Sí" : "No"));

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(5));
        info.add(lblColor);
        info.add(Box.createVerticalStrut(5));
        info.add(lblEstado);
        info.add(Box.createVerticalStrut(5));
        info.add(lblApto);

        card.add(lblImg, BorderLayout.WEST);
        card.add(info, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mostrarVistaPerfilGatoVeterinario(g);
            }
        });

        return card;
    }
    
        private void mostrarVistaPerfilGatoVeterinario(Gato gato) {

        JPanel panelPerfil = new JPanel(new BorderLayout());
        panelPerfil.setBackground(new Color(220, 220, 220));
        panelPerfil.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        //---------- mas cosas del header
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.setBackground(new Color(190, 190, 190));
        panelTop.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(300, 300));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            String ruta = gato.getFotoPath();
            if (ruta != null && !ruta.isEmpty()) {
                ImageIcon original = new ImageIcon(ruta);
                Image scaled = original.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(scaled));
            } else lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
        } catch (Exception e) {
            lblFoto.setIcon(new ImageIcon("placeholder_gato.png"));
        }

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(190, 190, 190));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel lblNombre = new JLabel(gato.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 30));

        String textoHtml = "<html>"
                + "Color: " + gato.getColor() + "<br><br>"
                + "Características: " + gato.getCaracteristicas() + "<br><br>"
                + "Estado de salud: " + (gato.getEstadoSalud() != null ? gato.getEstadoSalud() : "Sin datos") + "<br><br>"
                + "Apto adopción: " + (Boolean.TRUE.equals(gato.getAptoAdopcion()) ? "Sí" : "No")
                + "</html>";
        JLabel lblTexto = new JLabel(textoHtml);
        lblTexto.setFont(new Font("SansSerif", Font.PLAIN, 18));

        panelInfo.add(lblNombre);
        panelInfo.add(Box.createVerticalStrut(20));
        panelInfo.add(lblTexto);

        panelTop.add(lblFoto, BorderLayout.WEST);
        panelTop.add(panelInfo, BorderLayout.CENTER);

        panelPerfil.add(panelTop, BorderLayout.NORTH);

        //---------- diagnosticos 
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(new Color(210, 210, 210));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        JLabel lblHist = new JLabel("Historial médico (diagnósticos)");
        lblHist.setFont(new Font("SansSerif", Font.BOLD, 24));
        panelTitulo.add(lblHist, BorderLayout.WEST);

        JButton btnNuevoDiag = new JButton("Nuevo diagnóstico");
        panelTitulo.add(btnNuevoDiag, BorderLayout.EAST);

        panelBottom.add(panelTitulo, BorderLayout.NORTH);

        javax.swing.table.DefaultTableModel modelo =
                new javax.swing.table.DefaultTableModel(
                        new Object[]{"Fecha", "Diagnóstico", "Veterinario"}, 0) {
                    @Override
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };

        java.util.List<Diagnostico> lista = control.listarDiagnosticosPorGato(gato);

        for (Diagnostico d : lista) {
            String vetNombre = (d.getVeterinario() != null ? d.getVeterinario().getNombre() : "-");
            modelo.addRow(new Object[]{
                    d.getFecha(),
                    d.getNombre(),
                    vetNombre
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tabla);

        panelBottom.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVerEditar = new JButton("Ver / editar diagnóstico");
        JButton btnCertificado = new JButton("Emitir certificado aptitud");
        panelAcciones.add(btnVerEditar);
        panelAcciones.add(btnCertificado);

        panelBottom.add(panelAcciones, BorderLayout.SOUTH);

        panelPerfil.add(panelBottom, BorderLayout.CENTER);

        //---------- acciones historial medico
        btnNuevoDiag.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(
                    this,
                    "Título del diagnóstico:",
                    "Nuevo diagnóstico",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (nombre != null && !nombre.isBlank()) {
                Diagnostico d = control.crearDiagnosticoParaGato(gato, nombre.trim());
                mostrarDialogoDiagnostico(gato, d);
                mostrarVistaPerfilGatoVeterinario(gato);
            }
        });

        btnVerEditar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0 || fila >= lista.size()) {
                JOptionPane.showMessageDialog(this, "Seleccione un diagnóstico de la tabla.");
                return;
            }
            Diagnostico d = lista.get(fila);
            mostrarDialogoDiagnostico(gato, d);
            mostrarVistaPerfilGatoVeterinario(gato);
        });

        btnCertificado.addActionListener(e -> {
            String comentario = JOptionPane.showInputDialog(
                    this,
                    "Comentario del certificado de aptitud:",
                    "Emitir certificado",
                    JOptionPane.PLAIN_MESSAGE
            );
            if (comentario != null) {
                control.emitirCertificadoAptitud(gato, comentario.trim());
                JOptionPane.showMessageDialog(this,
                        "Certificado emitido y gato marcado como apto para adopción.");
                mostrarVistaPerfilGatoVeterinario(gato);
            }
        });

        setVistaCentral(panelPerfil);
    }
    private void mostrarDialogoDiagnostico(Gato gato, Diagnostico diag) {

        JDialog dialog = new JDialog(this, "Diagnóstico", true);
        dialog.setSize(750, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

//---------- cabecera de diagnotisco
        JPanel panelTop = new JPanel(new GridLayout(2, 2, 8, 8));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelTop.add(new JLabel("Gato:"));
        panelTop.add(new JLabel(gato.getNombre()));

        panelTop.add(new JLabel("Título diagnóstico:"));
        JTextField txtNombre = new JTextField(diag.getNombre() != null ? diag.getNombre() : "");
        panelTop.add(txtNombre);

        dialog.add(panelTop, BorderLayout.NORTH);

        //---------- panel central vertical
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

       //---------- estudio
        JLabel lblEst = new JLabel("Estudios");
        lblEst.setFont(new Font("SansSerif", Font.BOLD, 16));
        panelCentral.add(lblEst);

        java.util.List<Estudio> estudios = control.listarEstudiosPorDiagnostico(diag);

        DefaultTableModel modeloEst = new DefaultTableModel(
                new Object[]{"Nombre", "Descripción", "Documento"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Estudio e : estudios) {
            modeloEst.addRow(new Object[]{ e.getNombre(), e.getDescripcion(), e.getDocumento() });
        }

        JTable tablaEst = new JTable(modeloEst);
        JScrollPane scrollEst = new JScrollPane(tablaEst);
        scrollEst.setPreferredSize(new Dimension(700, 150));
        panelCentral.add(scrollEst);

        JPanel panelBotEst = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevoEst = new JButton("Nuevo estudio");
        JButton btnEditarEst = new JButton("Editar estudio");
        panelBotEst.add(btnNuevoEst);
        panelBotEst.add(btnEditarEst);
        panelCentral.add(panelBotEst);

        //---------- lo mismo para tratamientos
        JLabel lblTrat = new JLabel("Tratamientos");
        lblTrat.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTrat.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelCentral.add(lblTrat);

        java.util.List<Tratamiento> tratamientos = control.listarTratamientosPorDiagnostico(diag);

        DefaultTableModel modeloTrat = new DefaultTableModel(
                new Object[]{"Nombre", "Fecha", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        for (Tratamiento t : tratamientos) {
            modeloTrat.addRow(new Object[]{ t.getNombre(), t.getFecha(), t.getDescripcion() });
        }

        JTable tablaTrat = new JTable(modeloTrat);
        JScrollPane scrollTrat = new JScrollPane(tablaTrat);
        scrollTrat.setPreferredSize(new Dimension(700, 150));
        panelCentral.add(scrollTrat);

        JPanel panelBotTrat = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnNuevoTrat = new JButton("Nuevo tratamiento");
        JButton btnEditarTrat = new JButton("Editar tratamiento");
        panelBotTrat.add(btnNuevoTrat);
        panelBotTrat.add(btnEditarTrat);
        panelCentral.add(panelBotTrat);

        dialog.add(panelCentral, BorderLayout.CENTER);

        //---------- botones finales
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        panelSur.add(btnGuardar);
        panelSur.add(btnCancelar);
        dialog.add(panelSur, BorderLayout.SOUTH);

        //---------- acciones estudio
        btnNuevoEst.addActionListener(ev -> {
            JTextField txtNom = new JTextField();
            JTextField txtDesc = new JTextField();
            JTextField txtDoc = new JTextField();

            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("Nombre:")); p.add(txtNom);
            p.add(new JLabel("Descripción:")); p.add(txtDesc);
            p.add(new JLabel("Documento (ruta/ref):")); p.add(txtDoc);

            if (JOptionPane.showConfirmDialog(dialog, p, "Nuevo estudio",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                Estudio e = control.crearEstudioParaDiagnostico(diag,
                        txtNom.getText(), txtDesc.getText(), txtDoc.getText());

                estudios.add(e);
                modeloEst.addRow(new Object[]{ e.getNombre(), e.getDescripcion(), e.getDocumento() });
            }
        });

        btnEditarEst.addActionListener(ev -> {
            int fila = tablaEst.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(dialog, "Seleccione un estudio."); return; }
            Estudio est = estudios.get(fila);

            JTextField txtNom = new JTextField(est.getNombre());
            JTextField txtDesc = new JTextField(est.getDescripcion());
            JTextField txtDoc = new JTextField(est.getDocumento());

            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("Nombre:")); p.add(txtNom);
            p.add(new JLabel("Descripción:")); p.add(txtDesc);
            p.add(new JLabel("Documento:")); p.add(txtDoc);

            if (JOptionPane.showConfirmDialog(dialog, p, "Editar estudio",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                est.setNombre(txtNom.getText());
                est.setDescripcion(txtDesc.getText());
                est.setDocumento(txtDoc.getText());
                control.actualizarEstudio(est);

                modeloEst.setValueAt(est.getNombre(), fila, 0);
                modeloEst.setValueAt(est.getDescripcion(), fila, 1);
                modeloEst.setValueAt(est.getDocumento(), fila, 2);
            }
        });

        //---------- acciones tratamiento

        btnNuevoTrat.addActionListener(ev -> {

            JTextField txtNom = new JTextField();
            JTextField txtFecha = new JTextField(LocalDate.now().toString());
            JTextField txtDesc = new JTextField();

            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("Nombre:")); p.add(txtNom);
            p.add(new JLabel("Fecha (YYYY-MM-DD):")); p.add(txtFecha);
            p.add(new JLabel("Descripción:")); p.add(txtDesc);

            if (JOptionPane.showConfirmDialog(dialog, p, "Nuevo tratamiento",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                Tratamiento t = control.crearTratamientoParaDiagnostico(
                        diag, txtNom.getText(),
                        LocalDate.parse(txtFecha.getText()), txtDesc.getText());

                tratamientos.add(t);
                modeloTrat.addRow(new Object[]{ t.getNombre(), t.getFecha(), t.getDescripcion() });
            }
        });

        btnEditarTrat.addActionListener(ev -> {
            int fila = tablaTrat.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(dialog, "Seleccione un tratamiento."); return; }

            Tratamiento t = tratamientos.get(fila);

            JTextField txtNom = new JTextField(t.getNombre());
            JTextField txtFecha = new JTextField(t.getFecha().toString());
            JTextField txtDesc = new JTextField(t.getDescripcion());

            JPanel p = new JPanel(new GridLayout(3, 2, 5, 5));
            p.add(new JLabel("Nombre:")); p.add(txtNom);
            p.add(new JLabel("Fecha:")); p.add(txtFecha);
            p.add(new JLabel("Descripción:")); p.add(txtDesc);

            if (JOptionPane.showConfirmDialog(dialog, p, "Editar tratamiento",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                t.setNombre(txtNom.getText());
                t.setFecha(LocalDate.parse(txtFecha.getText()));
                t.setDescripcion(txtDesc.getText());
                control.actualizarTratamiento(t);

                modeloTrat.setValueAt(t.getNombre(), fila, 0);
                modeloTrat.setValueAt(t.getFecha(), fila, 1);
                modeloTrat.setValueAt(t.getDescripcion(), fila, 2);
            }
        });

        btnGuardar.addActionListener(ev -> {
            diag.setNombre(txtNombre.getText());
            control.actualizarDiagnostico(diag);
            dialog.dispose();
        });

        btnCancelar.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    }
}
