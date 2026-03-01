package modelo_de_capas.igu;

import org.jxmapviewer.viewer.TileFactoryInfo;

public class OSMTileFactoryInfo extends TileFactoryInfo {
    //---------- clase del jxmapviewer que no descarga maven entonces la exporte aca.
    public OSMTileFactoryInfo() {
        super("OpenStreetMap", 
                1, 15, 17, 
                256, true, true,
                "https://tile.openstreetmap.org",
                "x", "y", "z");
    }

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        int z = 17 - zoom;
        return this.baseURL + "/" + z + "/" + x + "/" + y + ".png";
    }
}
