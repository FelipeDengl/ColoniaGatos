package modelo_de_capas.igu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

public class RectPainter implements Painter<JXMapViewer> {

    private final JXMapViewer map;
    private final GeoPosition gp1;
    private final GeoPosition gp2;

    public RectPainter(JXMapViewer map, GeoPosition gp1, GeoPosition gp2) {
        this.map = map;
        this.gp1 = gp1;
        this.gp2 = gp2;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer viewer, int width, int height) {

        if (gp1 != null && gp2 != null) {

            Point p1 = (Point) map.convertGeoPositionToPoint(gp1);
            Point p2 = (Point) map.convertGeoPositionToPoint(gp2);

            int x = Math.min(p1.x, p2.x);
            int y = Math.min(p1.y, p2.y);
            int w = Math.abs(p1.x - p2.x);
            int h = Math.abs(p1.y - p2.y);

            g.setColor(new Color(255, 0, 0, 80));
            g.fillRect(x, y, w, h);

            g.setColor(Color.RED);
            g.drawRect(x, y, w, h);
        }
    }
}
