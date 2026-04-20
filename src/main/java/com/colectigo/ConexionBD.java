package com.colectigo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    // Datos de tu base de datos local
    private static final String URL = "jdbc:mysql://localhost:3306/colectigo";
    private static final String USER = "root"; //
    private static final String PASSWORD = "fernando16"; //

    private static Connection conexion = null;


    public static Connection getConexion() {
        try {
            // Si la conexión no existe o se cerró, la creamos
            if (conexion == null || conexion.isClosed()) {
                // Registro del driver (opcional en versiones nuevas, pero buena práctica)
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión exitosa a ColectiGo");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}