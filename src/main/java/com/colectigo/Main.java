package com.colectigo;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.setProperty("http.agent", "ColectiGo-App-Varela");
        
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}