package modelo_de_capas.igu;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import modelo_de_capas.igu.OSMTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;

public class MapaPanel extends JPanel {

    private JXMapViewer map;
    private ClickListener listener;

    private RectPainterPunto painterPunto = new RectPainterPunto();
    private CompoundPainter<JXMapViewer> compoundPainter;

    public interface ClickListener {
        void onClick(double lat, double lon);
    }

    public MapaPanel() {
        setLayout(new BorderLayout());

        //----------  tileFactory exactamente igual que en registrar colonia
        TileFactory tileFactory = new DefaultTileFactory(new OSMTileFactoryInfo());

        map = new JXMapViewer();
        map.setTileFactory(tileFactory);

        map.setZoom(5);
        map.setAddressLocation(new GeoPosition(-27.3621, -55.9009));

        //----------  PAN + ZOOM (estos NO tocar porque ser rompe
        PanMouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));

        //----------  CLICK EN EL MAPA
        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GeoPosition gp = map.convertPointToGeoPosition(e.getPoint());

                // Actualizar punto visible
                painterPunto.setPunto(gp);
                map.repaint();

                if (listener != null) {
                    listener.onClick(gp.getLatitude(), gp.getLongitude());
                }
            }
        });

        //----------  crea el cuadrado de los dos puntos (NO ROMPE NADA)
        compoundPainter = new CompoundPainter<>();
        compoundPainter.setPainters(painterPunto);

        map.setOverlayPainter(compoundPainter);

        add(map, BorderLayout.CENTER);
    }

    public void setListenerClick(ClickListener l) {
        this.listener = l;
    }
}
