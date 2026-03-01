package modelo_de_capas.igu;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class MapaSeleccionDialog extends JDialog {

    private JXMapViewer mapViewer;
    private JLabel lblInfo;
    private String coordenaSeleccionada;
    private boolean seleccionValida = false;

    public MapaSeleccionDialog(Frame parent, boolean modal) {
        super(parent, modal);
        setTitle("Seleccionar punto en el mapa");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initMapa();
        initBottom();
    }

    private void initMapa() {
        mapViewer = new JXMapViewer();

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        //---------- para que se ponga posadas de una
        GeoPosition posadas = new GeoPosition(-27.362137, -55.900875);
        mapViewer.setZoom(5);
        mapViewer.setAddressLocation(posadas);

        //---------- guardado de la cordenada con click
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                GeoPosition gp = mapViewer.convertPointToGeoPosition(p);
                coordenaSeleccionada = String.format(Locale.US, "%.6f,%.6f",
                        gp.getLatitude(), gp.getLongitude());
                lblInfo.setText("Punto seleccionado: " + coordenaSeleccionada);
            }
        });

        add(new JScrollPane(mapViewer), BorderLayout.CENTER);
    }

    private void initBottom() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        lblInfo = new JLabel("Hacé clic en el mapa para seleccionar un punto.");
        bottom.add(lblInfo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAceptar = new JButton("Aceptar");

        btnCancelar.addActionListener(e -> {
            seleccionValida = false;
            dispose();
        });

        btnAceptar.addActionListener(e -> {
            if (coordenaSeleccionada == null || coordenaSeleccionada.isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Primero hacé clic en el mapa.",
                        "Coordenada faltante",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            seleccionValida = true;
            dispose();
        });

        botones.add(btnCancelar);
        botones.add(btnAceptar);
        bottom.add(botones, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    public boolean isSeleccionValida() {
        return seleccionValida;
    }

    public String getCoordenaSeleccionada() {
        return coordenaSeleccionada;
    }
}
