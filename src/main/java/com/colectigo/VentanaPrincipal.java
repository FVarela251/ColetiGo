package com.colectigo;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jxmapviewer.viewer.GeoPosition;

public class VentanaPrincipal extends JFrame {
    private MapaManager mapaMgr;
    private JComboBox<String> cbLineas;
    private JRadioButton rbIda;

    public VentanaPrincipal() {
        super("ColectiGo - Sistema de Rutas Coyhaique");
        mapaMgr = new MapaManager();
        
        initUI();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        // Panel Izquierdo (Control)
        JPanel panelIzquierdo = crearPanelControl();
        
        // Divisor
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                            panelIzquierdo, 
                                            mapaMgr.getComponenteMapa());
        splitPane.setDividerLocation(320);
        add(splitPane);
    }

    private JPanel crearPanelControl() {
    JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(250, 250, 250));

        // --- SECCIÓN TÍTULO ---
        JLabel lblTitulo = new JLabel("ColectiGo");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        JLabel lblSub = new JLabel("Región de Aysén");
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

    // --- SECCIÓN LÍNEAS ---
        JLabel lblLineas = new JLabel("Explorar Líneas:");
        lblLineas.setFont(new Font("Arial", Font.BOLD, 14));
        lblLineas.setBorder(BorderFactory.createEmptyBorder(25, 0, 5, 0));
        lblLineas.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        cbLineas = new JComboBox<>(new String[]{"Seleccione...", "Línea 5"});
        cbLineas.setMaximumSize(new Dimension(280, 35));
        cbLineas.setAlignmentX(Component.LEFT_ALIGNMENT);

    // Panel para RadioButtons (Sentido)
        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRadio.setOpaque(false);
        panelRadio.setMaximumSize(new Dimension(280, 40));
        panelRadio.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        rbIda = new JRadioButton("Ida", true);
        JRadioButton rbVuelta = new JRadioButton("Vuelta");
        ButtonGroup grupoSentido = new ButtonGroup();
        grupoSentido.add(rbIda);
        grupoSentido.add(rbVuelta);
        panelRadio.add(rbIda);
        panelRadio.add(rbVuelta);

        JButton btnVer = new JButton("Ver Recorrido");
        btnVer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVer.setMaximumSize(new Dimension(280, 40));
        btnVer.setBackground(new Color(33, 150, 243));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFocusPainted(false);
        btnVer.addActionListener(e -> accionCargarRuta());

    // --- SECCIÓN PLANIFICAR VIAJE (RESTAURADA) ---
        JLabel lblPlanificar = new JLabel("Planificar Viaje:");
        lblPlanificar.setFont(new Font("Arial", Font.BOLD, 14));
        lblPlanificar.setBorder(BorderFactory.createEmptyBorder(35, 0, 10, 0));
        lblPlanificar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesde = new JLabel("Desde (Origen):");
        lblDesde.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField txtOrigen = new JTextField("Mi ubicación actual");
        txtOrigen.setEnabled(false); // Inhabilitado como pediste
        txtOrigen.setMaximumSize(new Dimension(280, 30));
        txtOrigen.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHasta = new JLabel("Hasta (Destino):");
        lblHasta.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblHasta.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JTextField txtDestino = new JTextField("Ingrese destino...");
        txtDestino.setEnabled(false); // Inhabilitado como pediste
        txtDestino.setMaximumSize(new Dimension(280, 30));
        txtDestino.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnBuscar = new JButton("Calcular Ruta");
        btnBuscar.setEnabled(false);
        btnBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBuscar.setMaximumSize(new Dimension(280, 40));

    // --- AGREGAR TODO AL PANEL EN ORDEN ---
        panel.add(lblTitulo);
        panel.add(lblSub);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JSeparator());
        panel.add(lblLineas);
        panel.add(cbLineas);
        panel.add(panelRadio);
        panel.add(btnVer);
    
        panel.add(lblPlanificar);
        panel.add(lblDesde);
        panel.add(txtOrigen);
        panel.add(lblHasta);
        panel.add(txtDestino);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(btnBuscar);
    
    // Pegamento al final para que todo se empuje hacia arriba
        panel.add(Box.createVerticalGlue());

    return panel;
}

    private void accionCargarRuta() {
        String seleccion = (String) cbLineas.getSelectedItem();
        if (seleccion.equals("Seleccione...")) return;

        String numLinea = seleccion.replace("Línea ", "");
        String sentido = rbIda.isSelected() ? "Ida" : "Vuelta";

        try (Connection conn = ConexionBD.getConexion()) {
            String sql = "SELECT puntos_json FROM trazados_geo t " +
                         "JOIN lineas l ON t.linea_id = l.id " +
                         "WHERE l.numero = ? AND t.sentido = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, numLinea);
            ps.setString(2, sentido);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String json = rs.getString("puntos_json");
                List<GeoPosition> track = parsearJson(json);
                mapaMgr.dibujarRuta(track);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private List<GeoPosition> parsearJson(String json) {
        Gson gson = new Gson();
        java.lang.reflect.Type listType = new TypeToken<ArrayList<Map<String, Double>>>(){}.getType();
        ArrayList<Map<String, Double>> puntos = gson.fromJson(json, listType);
        List<GeoPosition> track = new ArrayList<>();
        for (Map<String, Double> p : puntos) {
            track.add(new GeoPosition(p.get("lat"), p.get("lng")));
        }
        return track;
    }
}