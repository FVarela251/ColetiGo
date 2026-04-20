package com.colectigo;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class RutaLinea implements Painter<JXMapViewer> {
    private List<GeoPosition> track;

    public RutaLinea(List<GeoPosition> track) {
        this.track = track;
    }

@Override
public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
    g = (Graphics2D) g.create();

    // 1. IMPORTANTE: No usamos translate(rect.x, rect.y) con este método
    // porque convertGeoPositionToPoint ya nos da la posición relativa a la pantalla.
    
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Configurar el color Azul Celeste
    g.setColor(new Color(0, 191, 255)); 
    g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    int lastX = 0;
    int lastY = 0;
    boolean first = true;

    for (GeoPosition gp : track) {
        // --- LA SOLUCIÓN AQUÍ ---
        // Este método es interno de JXMapViewer y ya maneja el zoom y la posición
        Point2D pt = map.convertGeoPositionToPoint(gp);

        int x = (int) pt.getX();
        int y = (int) pt.getY();

        if (first) {
            first = false;
        } else {
            // Dibujamos la línea desde el punto anterior al actual
            g.drawLine(lastX, lastY, x, y);
        }
        lastX = x;
        lastY = y;
    }
    g.dispose();
}
}