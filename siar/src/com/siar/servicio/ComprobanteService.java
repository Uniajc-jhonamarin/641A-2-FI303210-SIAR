package com.siar.servicio;

import com.siar.modelo.DetallePedido;
import com.siar.modelo.Factura;
import com.siar.modelo.Pedido;
import com.siar.modelo.Usuario;
import com.siar.seguridad.SesionActual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class ComprobanteService {

    private static final DecimalFormat FORMATO_MONEDA = new DecimalFormat("$#,##0");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Construye el objeto Factura aplicando las reglas de negocio vigentes.
     */
    public Factura construirFactura(Pedido pedido) {
        pedido.calcularTotal();
        double subtotal = pedido.getTotal();

        // Regla de negocio sincronizada con VistaFacturacion: 10% de descuento si subtotal >= 50.000
        double descuento = (subtotal >= 50000) ? subtotal * 0.10 : 0.0;

        Factura factura = new Factura();
        factura.setIdPedido(pedido.getId());
        factura.setSubtotal(subtotal);
        factura.setDescuento(descuento);
        return factura;
    }

    /**
     * Genera el contenido HTML compatible tanto con Swing (JEditorPane) como con navegadores web.
     */
    public String generarHtmlComprobante(Pedido pedido, Factura factura) {
        Usuario cajero = SesionActual.getInstancia().getUsuarioActual();
        String nombreCajero = (cajero != null) ? cajero.getNombre() : "Sistema";
        String porcentajeDesc = (factura.getDescuento() > 0) ? "10%" : "0%";

        StringBuilder filasProductos = new StringBuilder();
        for (DetallePedido dp : pedido.getDetalles()) {
            filasProductos.append("<tr>")
                    .append("<td style='padding:4px 2px;'>").append(dp.getCantidad()).append("</td>")
                    .append("<td style='padding:4px 2px;'>").append(escaparHtml(dp.getProducto().getNombre())).append("</td>")
                    .append("<td style='padding:4px 2px; text-align:right;'>").append(FORMATO_MONEDA.format(dp.getProducto().getPrecio())).append("</td>")
                    .append("<td style='padding:4px 2px; text-align:right;'>").append(FORMATO_MONEDA.format(dp.getSubtotal())).append("</td>")
                    .append("</tr>");
        }

        return "<!DOCTYPE html>"
                + "<html lang='es'><head><meta charset='UTF-8'>"
                + "<title>Comprobante Pedido #" + pedido.getId() + "</title>"
                + "</head>"
                + "<body style='font-family: Arial, sans-serif; color: #222; margin: 15px;'>"
                + "<div style='width: 360px; margin: 0 auto; border: 1px solid #ccc; padding: 16px; background: #fff;'>"

                // Encabezado
                + "<div style='text-align: center; border-bottom: 2px solid #2c3e50; padding-bottom: 10px;'>"
                + "<h2 style='margin: 0; color: #2c3e50;'>SIAR RESTAURANTE</h2>"
                + "<p style='margin: 3px 0; font-size: 11px; color: #555;'>Sistema Integral de Administración</p>"
                + "<p style='margin: 2px 0; font-size: 11px; color: #555;'>NIT: 900.123.456-7 | Régimen Común</p>"
                + "<p style='margin: 6px 0 0 0; font-size: 12px; font-weight: bold;'>COMPROBANTE DE PAGO #" + String.format("%05d", pedido.getId()) + "</p>"
                + "</div>"

                // Datos de la orden
                + "<table style='width: 100%; font-size: 11px; margin-top: 10px; border-bottom: 1px dashed #999; padding-bottom: 8px;'>"
                + "<tr><td><b>Fecha:</b></td><td style='text-align:right;'>" + factura.getFecha().format(FORMATO_FECHA) + "</td></tr>"
                + "<tr><td><b>Pedido N°:</b></td><td style='text-align:right;'>" + pedido.getId() + "</td></tr>"
                + "<tr><td><b>Mesa:</b></td><td style='text-align:right;'>Mesa " + pedido.getNumeroMesa() + "</td></tr>"
                + "<tr><td><b>Atendido por:</b></td><td style='text-align:right;'>" + escaparHtml(nombreCajero) + "</td></tr>"
                + "</table>"

                // Tabla de productos
                + "<table style='width: 100%; font-size: 11px; margin-top: 10px; border-collapse: collapse;'>"
                + "<thead>"
                + "<tr style='border-bottom: 1px solid #2c3e50; background-color: #f4f6f8;'>"
                + "<th style='text-align:left; padding:5px 2px;'>Cant</th>"
                + "<th style='text-align:left; padding:5px 2px;'>Producto</th>"
                + "<th style='text-align:right; padding:5px 2px;'>V. Unit</th>"
                + "<th style='text-align:right; padding:5px 2px;'>Subtotal</th>"
                + "</tr>"
                + "</thead>"
                + "<tbody>"
                + filasProductos
                + "</tbody>"
                + "</table>"

                // Totales
                + "<table style='width: 100%; font-size: 12px; margin-top: 12px; border-top: 1px dashed #999; padding-top: 8px;'>"
                + "<tr><td>Subtotal:</td><td style='text-align:right;'>" + FORMATO_MONEDA.format(factura.getSubtotal()) + "</td></tr>"
                + "<tr><td>IVA (19%):</td><td style='text-align:right;'>" + FORMATO_MONEDA.format(factura.getImpuestos()) + "</td></tr>"
                + "<tr><td>Descuento (" + porcentajeDesc + "):</td><td style='text-align:right; color: #27ae60;'>- " + FORMATO_MONEDA.format(factura.getDescuento()) + "</td></tr>"
                + "<tr style='font-size: 15px; font-weight: bold; color: #2c3e50;'>"
                + "<td style='padding-top: 8px; border-top: 2px solid #2c3e50;'>TOTAL A PAGAR:</td>"
                + "<td style='padding-top: 8px; border-top: 2px solid #2c3e50; text-align:right;'>" + FORMATO_MONEDA.format(factura.getTotal()) + "</td>"
                + "</tr>"
                + "</table>"

                // Pie de página
                + "<div style='text-align: center; margin-top: 16px; padding-top: 8px; border-top: 1px solid #eee; font-size: 11px; color: #666;'>"
                + "<p style='margin: 2px 0;'><b>¡Gracias por su preferencia!</b></p>"
                + "<p style='margin: 2px 0;'>Documento generado electrónicamente por SIAR</p>"
                + "</div>"

                + "</div>"
                + "</body></html>";
    }

    /**
     * Guarda el comprobante HTML en una carpeta dedicada "comprobantes/".
     */
    public File guardarComprobanteHtml(int idPedido, String html) throws IOException {
        File carpeta = new File("comprobantes");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, "comprobante_pedido_" + idPedido + ".html");
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8)) {
            writer.write(html);
        }
        return archivo;
    }

    private String escaparHtml(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }
}