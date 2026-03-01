package modelo_de_capas.igu;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.AbstractPainter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.Rectangle;

public class RectPainterPunto extends AbstractPainter<JXMapViewer> {

    private GeoPosition punto;

    public RectPainterPunto() {
        // se pinta siempre por encima
        setAntialiasing(true);
    }

    public void setPunto(GeoPosition punto) {
        this.punto = punto;
        // avisamos que cambió para que el mapa repinte
        setDirty(true);
    }

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        if (punto == null) {
            return;
        }

        // coordenadas de la vista actual
        Rectangle viewport = map.getViewportBounds();

        // posición del punto en coordenadas "globales" de pixel
        Point2D pt = map.getTileFactory().geoToPixel(punto, map.getZoom());

        // convertir a coords relativas al viewport
        int x = (int) (pt.getX() - viewport.getX());
        int y = (int) (pt.getY() - viewport.getY());

        int size = 10;
        int half = size / 2;

        // dibujar círculo rojo
        g.setColor(Color.RED);
        g.fillOval(x - half, y - half, size, size);

        g.setColor(Color.BLACK);
        g.drawOval(x - half, y - half, size, size);
    }
}
