/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sebastian
 */
package com.siar.configuracion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBaseDatos {
    
    private static ConexionBaseDatos instancia;
    private Properties propiedades;

    // Constructor privado para patrón Singleton
    private ConexionBaseDatos() {
        propiedades = new Properties();
        cargarPropiedades();
    }

    private void cargarPropiedades() {
        // Usamos getResourceAsStream con un "/" al inicio para buscar en la raíz del proyecto compilado
        try (InputStream input = ConexionBaseDatos.class.getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Error: No se encontró el archivo database.properties");
                return;
            }
            propiedades.load(input);
        } catch (Exception ex) {
            System.err.println("Error al leer properties: " + ex.getMessage());
        }
    }

    // Método principal para obtener la instancia Singleton
    public static ConexionBaseDatos getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBaseDatos();
        }
        return instancia;
    }

    // Provee una nueva conexión
    public Connection getConexion() throws SQLException {
        try {
            // Fuerza a la Máquina Virtual de Java a cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error crítico: El archivo .jar del Driver de MySQL no se encontró en el Classpath.");
            throw new SQLException("Driver de MySQL no encontrado", e);
        }

        String url = propiedades.getProperty("db.url");
        String user = propiedades.getProperty("db.usuario");
        String pass = propiedades.getProperty("db.password");
        
        if (pass != null) {
            pass = pass.trim();
        } else {
            pass = "";
        }
        
        return DriverManager.getConnection(url, user, pass);
    }

    // Método de utilidad para cerrar recursos de manera segura
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
