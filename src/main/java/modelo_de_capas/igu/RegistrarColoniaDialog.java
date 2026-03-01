package modelo_de_capas.igu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import modelo_de_capas.logica.Administrador;
import modelo_de_capas.logica.Colonia;
import modelo_de_capas.logica.Controladora;
import modelo_de_capas.logica.Usuario;

// JXMapViewer2
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

public class RegistrarColoniaDialog extends JDialog {

    private final Controladora control;

    private JTextField txtNombre;
    private JTextField txtDescripcion;

    private JXMapViewer map;
    private GeoPosition gp1;
    private GeoPosition gp2;

    private RectPainter rectPainter;

    public RegistrarColoniaDialog(java.awt.Frame owner, boolean modal, Controladora control) {
        super(owner, modal);
        this.control = control;
        initUI();
    }

    //---------- interfaz grafica
    private void initUI() {
        setTitle("Registrar nueva colonia");
        setSize(1100, 650);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        //---------- texto de nombre y descripcion
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null); 

        JLabel lblNombre = new JLabel("Nombre de la colonia:");
        lblNombre.setBounds(10, 10, 150, 25);
        topPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(170, 10, 880, 25);
        topPanel.add(txtNombre);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setBounds(10, 45, 150, 25);
        topPanel.add(lblDesc);

        txtDescripcion = new JTextField();
        txtDescripcion.setBounds(170, 45, 880, 25);
        topPanel.add(txtDescripcion);

        topPanel.setPreferredSize(new Dimension(0, 80));
        add(topPanel, BorderLayout.NORTH);

        //---------- mapa
        map = new JXMapViewer();
        initMapa();
        add(map, BorderLayout.CENTER);

        //---------- botones
        JPanel bottomPanel = new JPanel();
        JButton btnGuardar = new JButton("Guardar colonia");
        JButton btnCancelar = new JButton("Cancelar");

        bottomPanel.add(btnGuardar);
        bottomPanel.add(btnCancelar);

        add(bottomPanel, BorderLayout.SOUTH);

        // listeners botones
        btnCancelar.addActionListener(e -> dispose());
        btnGuardar.addActionListener(e -> guardarColonia());
    }

    //---------- mapaaa
    private void initMapa() {

        // ---- TileFactory con HTTPS a OpenStreetMap ----
        TileFactoryInfo info = new TileFactoryInfo(
                1,      // min zoom
                19,     // max zoom
                19,     // total zoom levels
                256,    // tamaño de tile
                true,   // x/y swapped
                true,   // uses TMS coords
                "https://tile.openstreetmap.org", // base URL (sin la última /)
                "x", "y", "z") {

            @Override
            public String getTileUrl(int x, int y, int zoom) {
                // jxmapviewer usa zoom invertido, hacemos la conversion
                int z = 19 - zoom;
                return String.format("https://tile.openstreetmap.org/%d/%d/%d.png", z, x, y);
            }
        };

        TileFactory tileFactory = new DefaultTileFactory(info);
        map.setTileFactory(tileFactory);

        // Zoom y posición inicial (Posadas)
        map.setZoom(4);
        map.setAddressLocation(new GeoPosition(-27.3621, -55.9009));

        // Pan con arrastre
        PanMouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);

        // Zoom con rueda
        map.addMouseWheelListener((MouseWheelEvent e) -> {
            if (e.getWheelRotation() < 0) {
                map.setZoom(map.getZoom() - 1);
            } else {
                map.setZoom(map.getZoom() + 1);
            }
        });

        // Pintor del rectángulo
        rectPainter = new RectPainter();
        map.setOverlayPainter(rectPainter);

        // Clicks para marcar los dos puntos de la colonia
        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }
                GeoPosition gp = map.convertPointToGeoPosition(e.getPoint());
                if (gp1 == null) {
                    gp1 = gp;
                } else if (gp2 == null) {
                    gp2 = gp;
                } else {
                    gp1 = gp;
                    gp2 = null;
                }
                rectPainter.setPositions(gp1, gp2);
                map.repaint();
            }
        });
    }


    // ===================== GUARDAR COLONIA =========================
    private void guardarColonia() {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (nombre.isEmpty() || gp1 == null || gp2 == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre y marcar dos puntos en el mapa.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ordenar coordenadas
        double latMin = Math.min(gp1.getLatitude(), gp2.getLatitude());
        double latMax = Math.max(gp1.getLatitude(), gp2.getLatitude());
        double lonMin = Math.min(gp1.getLongitude(), gp2.getLongitude());
        double lonMax = Math.max(gp1.getLongitude(), gp2.getLongitude());

        // obtener usuario logueado y su administrador
        Usuario u = control.getUsuarioLogueado();
        if (u == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay usuario logueado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Este método lo habíamos comentado antes: adaptalo al nombre real que tengas
        Administrador admin = control.getAdministradorDeUsuario(u);
        if (admin == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Solo un administrador puede crear colonias.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Crear y completar entidad Colonia
        Colonia c = new Colonia();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setLatMin(latMin);
        c.setLatMax(latMax);
        c.setLonMin(lonMin);
        c.setLonMax(lonMax);
        c.setAdministrador(admin);

        // Persistir
        control.crearColonia(c);

        JOptionPane.showMessageDialog(this, "Colonia registrada correctamente.");
        dispose();
    }

    // ===================== PAINTER RECTÁNGULO =========================
    private class RectPainter implements Painter<JXMapViewer> {

        private GeoPosition p1;
        private GeoPosition p2;

        public void setPositions(GeoPosition p1, GeoPosition p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public void paint(Graphics2D g, JXMapViewer viewer, int width, int height) {
            if (p1 == null || p2 == null) {
                return;
            }

            // de coordenadas geográficas a pixeles del mundo
            Point2D pt1 = viewer.getTileFactory().geoToPixel(p1, viewer.getZoom());
            Point2D pt2 = viewer.getTileFactory().geoToPixel(p2, viewer.getZoom());

            // ajustar por el viewport actual
            Rectangle2D viewport = viewer.getViewportBounds();
            double x1 = pt1.getX() - viewport.getX();
            double y1 = pt1.getY() - viewport.getY();
            double x2 = pt2.getX() - viewport.getX();
            double y2 = pt2.getY() - viewport.getY();

            double x = Math.min(x1, x2);
            double y = Math.min(y1, y2);
            double w = Math.abs(x1 - x2);
            double h = Math.abs(y1 - y2);

            Rectangle2D rect = new Rectangle2D.Double(x, y, w, h);

            // relleno semi-transparente
            g.setColor(new Color(255, 0, 0, 80));
            g.fill(rect);

            // borde
            g.setColor(Color.RED);
            g.draw(rect);

            // puntitos en las esquinas
            g.fillOval((int) x1 - 3, (int) y1 - 3, 6, 6);
            g.fillOval((int) x2 - 3, (int) y2 - 3, 6, 6);
        }
    }
}
