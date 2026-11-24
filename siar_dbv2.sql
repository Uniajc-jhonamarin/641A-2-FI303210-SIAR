-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-11-2026 a las 04:11:20
-- Versión del servidor: 10.11.19-MariaDB
-- Versión de PHP: 8.3.37

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `siar_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auditoria`
--

CREATE TABLE `auditoria` (
  `id` int(11) NOT NULL,
  `usuario_email` varchar(100) NOT NULL,
  `accion` varchar(100) NOT NULL,
  `detalle` varchar(255) NOT NULL,
  `fecha` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `auditoria`
--

INSERT INTO `auditoria` (`id`, `usuario_email`, `accion`, `detalle`, `fecha`) VALUES
(1, 'SISTEMA/ANONIMO', 'RECUPERAR_PASSWORD', 'Restablecimiento para: admin@siar.com', '2026-11-23 19:57:51'),
(2, 'admin@siar.com', 'LOGIN', 'Inicio de sesión exitoso', '2026-11-23 21:28:19'),
(3, 'admin@siar.com', 'ACTUALIZAR_USUARIO', 'Usuario actualizado ID: 4 (luz@gmail.com)', '2026-11-23 21:28:42'),
(4, 'admin@siar.com', 'LOGIN', 'Inicio de sesión exitoso', '2026-11-23 22:04:23'),
(5, 'admin@siar.com', 'CREAR_PRODUCTO', 'Catálogo inicial de menú cargado', '2026-11-23 22:04:23'),
(6, 'mesero@siar.com', 'CREAR_PEDIDO', 'Pedido #10 registrado para Mesa 1', '2026-11-23 22:04:23'),
(7, 'cajero@siar.com', 'FACTURAR_PEDIDO', 'Factura FAC-2026-0001 generada por $35700.00', '2026-11-23 22:04:23'),
(8, 'admin@siar.com', 'LOGIN', 'Inicio de sesión exitoso', '2026-11-23 22:08:18');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `activa` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id`, `nombre`, `descripcion`, `activa`) VALUES
(1, 'Entradas', 'Aperitivos y snacks', 1),
(2, 'Platos Fuertes', 'Platos principales', 1),
(3, 'Bebidas', 'Jugos, gaseosas y licores', 1),
(4, 'Postres', 'Dulces y helados', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `tipo_documento` varchar(20) DEFAULT NULL,
  `numero_documento` varchar(50) DEFAULT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `tipo_documento`, `numero_documento`, `nombre_completo`, `telefono`, `email`, `direccion`) VALUES
(1, 'CC', '1144050123', 'Sofía Martínez', '3154201122', 'sofia.martinez@gmail.com', 'Calle 5 # 45-20, Cali'),
(2, 'CC', '1006123456', 'Mateo López', '3008903344', 'mateo.lopez@hotmail.com', 'Av. 6N # 24-15, Cali'),
(3, 'CC', '66987452', 'Valentina Ríos', '3187654321', 'valen.rios@gmail.com', 'Carrera 100 # 11-60, Cali'),
(4, 'NIT', '900123456-1', 'Distribuidora del Valle S.A.S', '6023304050', 'compras@distvalle.com', 'Zona Franca Palmaseca'),
(5, 'CC', '16789234', 'Diego Fernando Castro', '3105556677', 'diego.castro@yahoo.com', 'Calle 9 # 32-10, Cali');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalles_pedido`
--

CREATE TABLE `detalles_pedido` (
  `id` int(11) NOT NULL,
  `pedido_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `notas_preparacion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `detalles_pedido`
--

INSERT INTO `detalles_pedido` (`id`, `pedido_id`, `producto_id`, `cantidad`, `precio_unitario`, `subtotal`, `notas_preparacion`) VALUES
(1, 3, 1, 1, 30000.00, 30000.00, NULL),
(2, 4, 1, 1, 30000.00, 30000.00, NULL),
(3, 5, 1, 1, 30000.00, 30000.00, NULL),
(10, 10, 5, 2, 38000.00, 76000.00, 'Sin chicharrón uno de ellos'),
(11, 10, 9, 2, 11000.00, 22000.00, 'Poco hielo'),
(12, 11, 6, 1, 45000.00, 45000.00, 'Término medio'),
(13, 11, 2, 1, 12000.00, 12000.00, NULL),
(14, 12, 7, 2, 32000.00, 64000.00, NULL),
(15, 12, 10, 2, 8000.00, 16000.00, 'En agua, sin azúcar'),
(16, 13, 8, 1, 48000.00, 48000.00, NULL),
(17, 13, 13, 1, 14000.00, 14000.00, NULL),
(18, 14, 11, 2, 6000.00, 12000.00, 'Cliente canceló la mesa');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `facturas`
--

CREATE TABLE `facturas` (
  `id` int(11) NOT NULL,
  `pedido_id` int(11) NOT NULL,
  `cajero_id` int(11) NOT NULL,
  `numero_factura` varchar(50) NOT NULL,
  `fecha_emision` datetime DEFAULT current_timestamp(),
  `subtotal` decimal(10,2) NOT NULL,
  `impuestos` decimal(10,2) NOT NULL,
  `descuentos` decimal(10,2) DEFAULT 0.00,
  `total_final` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `facturas`
--

INSERT INTO `facturas` (`id`, `pedido_id`, `cajero_id`, `numero_factura`, `fecha_emision`, `subtotal`, `impuestos`, `descuentos`, `total_final`) VALUES
(1, 3, 2, 'FAC-2026-0001', '2026-11-02 19:45:00', 30000.00, 5700.00, 0.00, 35700.00),
(2, 4, 2, 'FAC-2026-0002', '2026-11-03 22:30:00', 30000.00, 5700.00, 0.00, 35700.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mesas`
--

CREATE TABLE `mesas` (
  `id` int(11) NOT NULL,
  `numero` int(11) NOT NULL,
  `capacidad` int(11) NOT NULL,
  `estado` enum('DISPONIBLE','OCUPADA','RESERVADA') DEFAULT 'DISPONIBLE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `mesas`
--

INSERT INTO `mesas` (`id`, `numero`, `capacidad`, `estado`) VALUES
(1, 0, 1, 'DISPONIBLE'),
(2, 1, 4, 'DISPONIBLE'),
(3, 2, 4, 'OCUPADA'),
(4, 3, 2, 'OCUPADA'),
(5, 4, 6, 'DISPONIBLE'),
(6, 5, 4, 'RESERVADA'),
(7, 6, 2, 'DISPONIBLE'),
(8, 7, 8, 'DISPONIBLE'),
(9, 8, 4, 'DISPONIBLE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodos_pago`
--

CREATE TABLE `metodos_pago` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `metodos_pago`
--

INSERT INTO `metodos_pago` (`id`, `nombre`, `activo`) VALUES
(1, 'Efectivo', 1),
(2, 'Tarjeta Débito', 1),
(3, 'Tarjeta Crédito', 1),
(4, 'Nequi / Daviplata', 1),
(5, 'Transferencia Bancaria', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pagos`
--

CREATE TABLE `pagos` (
  `id` int(11) NOT NULL,
  `factura_id` int(11) NOT NULL,
  `metodo_pago_id` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_pago` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `pagos`
--

INSERT INTO `pagos` (`id`, `factura_id`, `metodo_pago_id`, `monto`, `fecha_pago`) VALUES
(1, 1, 1, 35700.00, '2026-11-02 19:46:00'),
(2, 2, 4, 35700.00, '2026-11-03 22:31:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `id` int(11) NOT NULL,
  `mesa_id` int(11) NOT NULL,
  `mesero_id` int(11) NOT NULL,
  `cliente_id` int(11) DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT current_timestamp(),
  `estado` enum('PENDIENTE','EN_PREPARACION','LISTO','ENTREGADO','FACTURADO','CANCELADO') DEFAULT 'PENDIENTE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`id`, `mesa_id`, `mesero_id`, `cliente_id`, `fecha_creacion`, `estado`) VALUES
(3, 1, 1, NULL, '2026-11-02 19:06:29', 'FACTURADO'),
(4, 1, 1, NULL, '2026-11-03 22:02:46', 'FACTURADO'),
(5, 1, 1, NULL, '2026-11-23 17:20:17', 'FACTURADO'),
(10, 2, 3, 1, '2026-11-23 22:04:23', 'PENDIENTE'),
(11, 3, 3, 2, '2026-11-23 22:04:23', 'EN_PREPARACION'),
(12, 4, 3, 3, '2026-11-23 22:04:23', 'LISTO'),
(13, 5, 3, 4, '2026-11-23 22:04:23', 'FACTURADO'),
(14, 6, 3, 5, '2026-11-23 22:04:23', 'CANCELADO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `categoria_id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `disponible` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `categoria_id`, `nombre`, `descripcion`, `precio`, `disponible`) VALUES
(1, 2, 'Arroz chino', 'Arroz chino', 30000.00, 1),
(2, 1, 'Empanadas Vallunas (x4)', 'Empanadas crujientes de carne con ají casero', 12000.00, 1),
(3, 1, 'Patacones con Hogao', 'Patacones de plátano verde con hogao y guacamole', 15000.00, 1),
(4, 1, 'Aros de Calamar', 'Calamares apanados con salsa tártara', 22000.00, 1),
(5, 2, 'Bandeja Paisa', 'Frijoles, arroz, chicharrón, carne molida, huevo y aguacate', 38000.00, 1),
(6, 2, 'Churrasco 300g', 'Corte a la parrilla con papa francesa y ensalada', 45000.00, 1),
(7, 2, 'Pechuga Gratinada', 'Pechuga a la plancha con salsa de champiñones y queso', 32000.00, 1),
(8, 2, 'Salmón al Ajillo', 'Filete de salmón con puré de papa y vegetales', 48000.00, 1),
(9, 3, 'Limonada de Coco', 'Limonada natural frappé con crema de coco', 11000.00, 1),
(10, 3, 'Jugo Natural en Agua', 'Lulo, maracuyá, mora o mango', 8000.00, 1),
(11, 3, 'Gaseosa 400ml', 'Bebida gaseosa personal', 6000.00, 1),
(12, 3, 'Cerveza Artesanal', 'Botella 330ml', 14000.00, 1),
(13, 4, 'Cheesecake de Frutos Rojos', 'Porción individual con salsa de mora', 14000.00, 1),
(14, 4, 'Brownie con Helado', 'Brownie tibio con bola de helado de vainilla', 15000.00, 1),
(15, 4, 'Flan de Caramelo', 'Flan casero tradicional', 10000.00, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`, `descripcion`) VALUES
(1, 'Administrador', 'Control total del sistema'),
(2, 'Cajero', 'Gestión de caja y facturación'),
(3, 'Mesero', 'Toma de pedidos y atención en mesas'),
(4, 'Cocinero', 'Gestión de comandas y preparación');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `rol_id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `rol_id`, `nombre`, `email`, `password_hash`, `activo`, `fecha_creacion`) VALUES
(1, 1, 'Admin Sistema', 'admin@siar.com', '$2a$10$HFYJPjGqPh0FGTrhuDjIvOy7PfMMRZ0M.O9GR.xXHNfn2RBgqOcH.', 1, '2026-11-02 17:19:59'),
(2, 2, 'Juan Sebastian Largo', 'jslargo61@gmail.com', '$2a$10$ADxKknX2n.fiYAwqJ/IJB.OD52wNw6kQfWLwHlGOK1T5yFIT0/F02', 1, '2026-11-23 17:08:28'),
(3, 3, 'Jhon Marin', 'jhon@gmail.com', '$2a$10$ms2I2luotGJ1Ojs1ByRgJusBzx7I9zn9QAUOPjktFhhM39HqL0bUK', 1, '2026-11-23 17:08:52'),
(4, 4, 'Luz', 'luz@gmail.com', '$2a$10$6ytqGaDmydOmDvnT6DWR4ueAhLJIIKXwW8HpIzamoDHu7k02XFTbe', 1, '2026-11-23 17:09:06');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_documento` (`numero_documento`);

--
-- Indices de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD PRIMARY KEY (`id`),
  ADD KEY `pedido_id` (`pedido_id`),
  ADD KEY `producto_id` (`producto_id`);

--
-- Indices de la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pedido_id` (`pedido_id`),
  ADD UNIQUE KEY `numero_factura` (`numero_factura`),
  ADD KEY `cajero_id` (`cajero_id`);

--
-- Indices de la tabla `mesas`
--
ALTER TABLE `mesas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero` (`numero`);

--
-- Indices de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `factura_id` (`factura_id`),
  ADD KEY `metodo_pago_id` (`metodo_pago_id`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `mesa_id` (`mesa_id`),
  ADD KEY `mesero_id` (`mesero_id`),
  ADD KEY `cliente_id` (`cliente_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `categoria_id` (`categoria_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `rol_id` (`rol_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `auditoria`
--
ALTER TABLE `auditoria`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de la tabla `facturas`
--
ALTER TABLE `facturas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `mesas`
--
ALTER TABLE `mesas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `metodos_pago`
--
ALTER TABLE `metodos_pago`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `pagos`
--
ALTER TABLE `pagos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalles_pedido`
--
ALTER TABLE `detalles_pedido`
  ADD CONSTRAINT `detalles_pedido_ibfk_1` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `detalles_pedido_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `facturas`
--
ALTER TABLE `facturas`
  ADD CONSTRAINT `facturas_ibfk_1` FOREIGN KEY (`pedido_id`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `facturas_ibfk_2` FOREIGN KEY (`cajero_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `pagos`
--
ALTER TABLE `pagos`
  ADD CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`factura_id`) REFERENCES `facturas` (`id`),
  ADD CONSTRAINT `pagos_ibfk_2` FOREIGN KEY (`metodo_pago_id`) REFERENCES `metodos_pago` (`id`);

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`mesa_id`) REFERENCES `mesas` (`id`),
  ADD CONSTRAINT `pedidos_ibfk_2` FOREIGN KEY (`mesero_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `pedidos_ibfk_3` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
