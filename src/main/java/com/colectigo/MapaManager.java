package com.colectigo;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;

import javax.swing.event.MouseInputListener;
import java.util.ArrayList;
import java.util.List;

public class MapaManager {
    private JXMapViewer mapa;

    // Coordenadas límites: Ajustadas para Coyhaique Urbano
    private final double LAT_MAX = -45.5400; // Norte
    private final double LAT_MIN = -45.6000; // Sur
    private final double LON_MIN = -72.1000; // Oeste
    private final double LON_MAX = -72.0300; // Este

    public MapaManager() {
        mapa = new JXMapViewer();

        // 1. Configuración del Servidor
        OSMTileFactoryInfo info = new OSMTileFactoryInfo("OpenStreetMap", "https://tile.openstreetmap.org");
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);
        mapa.setTileFactory(tileFactory);

        // 2. Configuración Inicial
        GeoPosition coyhaique = new GeoPosition(-45.5712, -72.0685);
        mapa.setAddressLocation(coyhaique);
        
        // En JXMapViewer2, el zoom suele ser: 0 (muy cerca) a 15+ (muy lejos)
        // Fijamos un zoom inicial cómodo para la ciudad
        mapa.setZoom(3); 

        // 3. Interacción
        MouseInputListener mia = new PanMouseInputListener(mapa);
        mapa.addMouseListener(mia);
        mapa.addMouseMotionListener(mia);
        mapa.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapa));

        // 4. BLOQUEO DE FRONTERAS Y ZOOM
        configurarRestricciones();
    }

    private void configurarRestricciones() {
        // --- RESTRICCIÓN DE MOVIMIENTO (Fronteras) ---
        mapa.addPropertyChangeListener("addressLocation", evt -> {
            GeoPosition pos = mapa.getAddressLocation();
            double lat = pos.getLatitude();
            double lon = pos.getLongitude();

            double newLat = Math.max(LAT_MIN, Math.min(LAT_MAX, lat));
            double newLon = Math.max(LON_MIN, Math.min(LON_MAX, lon));

            if (lat != newLat || lon != newLon) {
                // Usamos invokeLater para evitar conflictos con el hilo de dibujo
                javax.swing.SwingUtilities.invokeLater(() -> {
                    mapa.setAddressLocation(new GeoPosition(newLat, newLon));
                });
            }
        });

        // --- RESTRICCIÓN DE ZOOM (Para no ver Sudamérica) ---
        mapa.addPropertyChangeListener("zoom", evt -> {
            int currentZoom = mapa.getZoom();
            // Zoom 5 o 6 ya empieza a mostrar toda la región. 
            // Zoom 1 o 2 es nivel de calle.
            if (currentZoom > 5) { 
                javax.swing.SwingUtilities.invokeLater(() -> mapa.setZoom(5));
            } else if (currentZoom < 1) {
                javax.swing.SwingUtilities.invokeLater(() -> mapa.setZoom(1));
            }
        });
    }

    public void dibujarRuta(List<GeoPosition> puntos) {
        RutaLinea pintorRuta = new RutaLinea(puntos);
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(pintorRuta);
        
        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
        mapa.setOverlayPainter(compoundPainter);
        mapa.repaint();
    }

    public JXMapViewer getComponenteMapa() {
        return mapa;
    }
}