/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Sebastian
 */
package main.java.com.siar.configuracion;

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
        // Carga el archivo database.properties ubicado en src/main/resources
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
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
        String url = propiedades.getProperty("db.url");
        String user = propiedades.getProperty("db.usuario");
        String pass = propiedades.getProperty("db.password");
        
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
