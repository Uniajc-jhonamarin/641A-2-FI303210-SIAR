package com.siar;

import com.siar.configuracion.ConexionBaseDatos;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando prueba de conexión SIAR...");

        ConexionBaseDatos gestorConexion = ConexionBaseDatos.getInstancia();
        
        try {
            Connection conexion = gestorConexion.getConexion();
            if (conexion != null && !conexion.isClosed()) {
                System.out.println(" ¡Éxito! Conexión a MySQL establecida correctamente.");
                
                gestorConexion.cerrarConexion(conexion);
                System.out.println("Conexión cerrada de forma segura.");
            }
        } catch (Exception e) {
            System.err.println(" Error al conectar a la base de datos:");
            e.printStackTrace();
        }
    }
}