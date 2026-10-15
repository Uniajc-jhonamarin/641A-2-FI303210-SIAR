CREATE DATABASE IF NOT EXISTS siar_db;
USE siar_db;

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

-- =========================
-- USUARIOS
-- =========================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rol_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_usuario_rol
    FOREIGN KEY (rol_id)
    REFERENCES roles(id)
);

-- =========================
-- CLIENTES
-- =========================
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento VARCHAR(20),
    numero_documento VARCHAR(30) UNIQUE,
    nombre_completo VARCHAR(120) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(150)
);

-- =========================
-- MESAS
-- =========================
CREATE TABLE mesas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    capacidad INT NOT NULL,
    estado ENUM(
        'DISPONIBLE',
        'OCUPADA',
        'RESERVADA'
    ) DEFAULT 'DISPONIBLE'
);

-- =========================
-- RESERVAS
-- =========================
CREATE TABLE reservas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    mesa_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    numero_personas INT NOT NULL,
    estado ENUM(
        'PENDIENTE',
        'CONFIRMADA',
        'CANCELADA'
    ) DEFAULT 'PENDIENTE',
    notas_reserva VARCHAR(255),

    CONSTRAINT fk_reserva_cliente
    FOREIGN KEY (cliente_id)
    REFERENCES clientes(id),

    CONSTRAINT fk_reserva_mesa
    FOREIGN KEY (mesa_id)
    REFERENCES mesas(id)
);

-- =========================
-- CATEGORIAS
-- =========================
CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    activa BOOLEAN DEFAULT TRUE
);

-- =========================
-- PRODUCTOS
-- =========================
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_producto_categoria
    FOREIGN KEY (categoria_id)
    REFERENCES categorias(id)
);

-- =========================
-- INGREDIENTES
-- =========================
CREATE TABLE ingredientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    unidad_medida VARCHAR(20) NOT NULL,
    stock_actual DECIMAL(10,2) NOT NULL,
    stock_minimo DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- =========================
-- PRODUCTO_INGREDIENTE
-- =========================
CREATE TABLE producto_ingrediente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    ingrediente_id INT NOT NULL,
    cantidad DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_pi_producto
    FOREIGN KEY (producto_id)
    REFERENCES productos(id),

    CONSTRAINT fk_pi_ingrediente
    FOREIGN KEY (ingrediente_id)
    REFERENCES ingredientes(id)
);

-- =========================
-- PEDIDOS
-- =========================
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mesa_id INT NOT NULL,
    mesero_id INT NOT NULL,
    cliente_id INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,

    estado ENUM(
        'PENDIENTE',
        'EN_PREPARACION',
        'LISTO',
        'ENTREGADO',
        'FACTURADO'
    ) DEFAULT 'PENDIENTE',

    CONSTRAINT fk_pedido_mesa
    FOREIGN KEY (mesa_id)
    REFERENCES mesas(id),

    CONSTRAINT fk_pedido_mesero
    FOREIGN KEY (mesero_id)
    REFERENCES usuarios(id),

    CONSTRAINT fk_pedido_cliente
    FOREIGN KEY (cliente_id)
    REFERENCES clientes(id)
);

-- =========================
-- DETALLES PEDIDO
-- =========================
CREATE TABLE detalles_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    notas_preparacion VARCHAR(255),

    CONSTRAINT fk_detalle_pedido
    FOREIGN KEY (pedido_id)
    REFERENCES pedidos(id),

    CONSTRAINT fk_detalle_producto
    FOREIGN KEY (producto_id)
    REFERENCES productos(id)
);

-- =========================
-- FACTURAS
-- =========================
CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    cajero_id INT NOT NULL,

    numero_factura VARCHAR(50) UNIQUE,

    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,

    subtotal DECIMAL(10,2) NOT NULL,
    impuestos DECIMAL(10,2) DEFAULT 0,
    descuentos DECIMAL(10,2) DEFAULT 0,
    total_final DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_factura_pedido
    FOREIGN KEY (pedido_id)
    REFERENCES pedidos(id),

    CONSTRAINT fk_factura_cajero
    FOREIGN KEY (cajero_id)
    REFERENCES usuarios(id)
);

-- =========================
-- METODOS PAGO
-- =========================
CREATE TABLE metodos_pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- =========================
-- PAGOS
-- =========================
CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    metodo_pago_id INT NOT NULL,

    monto DECIMAL(10,2) NOT NULL,
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pago_factura
    FOREIGN KEY (factura_id)
    REFERENCES facturas(id),

    CONSTRAINT fk_pago_metodo
    FOREIGN KEY (metodo_pago_id)
    REFERENCES metodos_pago(id)
);