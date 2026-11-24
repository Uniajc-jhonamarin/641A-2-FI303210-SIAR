# SIAR — Sistema Integral de Administración para Restaurantes

**SIAR (Sistema Integral de Administración para Restaurantes)** es una aplicación de escritorio desarrollada en **Java Swing** con persistencia relacional en **MySQL (JDBC)**. Está diseñada para centralizar y automatizar la operación integral de un restaurante: gestión de usuarios y control de acceso por roles, administración del menú, toma y seguimiento de pedidos, monitor de cocina (KDS), facturación con generación de comprobantes profesionales en HTML/PDF y registro de auditoría de eventos críticos.

## Tabla de Contenidos

 1. [Arquitectura del Sistema y Patrones de Diseño](#1-arquitectura-del-sistema-y-patrones-de-diseño)

 2. [Estructura del Proyecto y Paquetes](#2-estructura-del-proyecto-y-paquetes)

 3. [Stack Tecnológico y Dependencias](#3-stack-tecnológico-y-dependencias)

 4. [Módulos Funcionales y Reglas de Negocio](#4-módulos-funcionales-y-reglas-de-negocio)

 5. [Matriz de Cumplimiento de Requerimientos](#5-matriz-de-cumplimiento-de-requerimientos)

 6. [Modelo de Base de Datos (`siar_db`)](#6-modelo-de-base-de-datos-siar_db)

 7. [Seguridad, Roles y Auditoría](#7-seguridad-roles-y-auditoría)

 8. [Instalación, Configuración y Ejecución](#8-instalación-configuración-y-ejecución)

 9. [Pruebas Unitarias y Cobertura de Código (JUnit 5 + JaCoCo)](#9-pruebas-unitarias-y-cobertura-de-código-junit-5--jacoco)

10. [Notas Técnicas y Solución de Problemas](#10-notas-técnicas-y-solución-de-problemas)

## 1. Arquitectura del Sistema y Patrones de Diseño

SIAR implementa una **Arquitectura en N-Capas combinada con el patrón MVC (Modelo-Vista-Controlador)**, diseñada bajo principios **SOLID** (especialmente Responsabilidad Única e Inversión de Dependencias) para desacoplar la interfaz gráfica, la lógica de negocio y el acceso a datos.

### Capas de la Arquitectura

1. **Capa de Presentación (`vista` y `controlador`):** Interfaces gráficas en Java Swing totalmente desacopladas de la lógica de negocio. Los controladores actúan como mediadores capturando eventos de usuario y delegando operaciones a los servicios.

2. **Capa de Negocio (`servicio`):** Concentra las validaciones, reglas comerciales (cálculo de impuestos, descuentos, restricciones de estado, seguridad de contraseñas) y orquestación de auditoría.

3. **Capa de Dominio (`modelo` y `patrones.estado`):** Entidades del negocio y máquina de estados que gobierna el ciclo de vida de los pedidos.

4. **Capa de Acceso a Datos (`repositorio` y `persistencia`):** Contratos (interfaces) en `repositorio` e implementaciones JDBC en `persistencia` utilizando consultas parametrizadas (`PreparedStatement`) para prevenir inyección SQL.

5. **Capa Transversal (`configuracion` y `seguridad`):** Gestión centralizada de conexión a base de datos y control de sesión activa.

### Patrones de Diseño Implementados

| Patrón | Clases Involucradas | Propósito en SIAR | 
| ----- | ----- | ----- | 
| **Singleton** | `ConexionBaseDatos`, `SesionActual` | Garantiza una única instancia centralizada para gestionar la conexión JDBC y el estado global del usuario autenticado durante toda la sesión. | 
| **Repository** | `UsuarioRepository`, `RolRepository`, `ProductoRepository`, `CategoriaRepository`, `PedidoRepository` y sus `*Impl` | Desacopla la lógica de negocio del motor MySQL/JDBC, permitiendo inyectar repositorios simulados en memoria (*fakes*) durante las pruebas unitarias. | 
| **State** | `EstadoPedido`, `EstadoPendiente`, `EstadoPreparacion`, `EstadoListo`, `EstadoEntregado`, `EstadoFacturado`, `EstadoCancelado` | Encapsula las transiciones válidas del ciclo de vida de un pedido, eliminando condicionales rígidos y bloqueando cambios en estados terminales (`FACTURADO`, `CANCELADO`). | 
| **MVC** | `Vista*`, `Controlador*`, `Modelo*` | Separa la representación visual Swing del manejo de eventos y de los datos del dominio. | 
| **Transacciones ACID** | `PedidoRepositoryImpl` | Inserción atómica de encabezados de pedido y múltiples ítems (`detalles_pedido`) usando `setAutoCommit(false)`, `executeBatch()`, `commit()` y `rollback()` ante fallos. | 

## 2. Estructura del Proyecto y Paquetes

```
SIAR/
├── README.md                                  # Documentación técnica completa
├── siar_db.sql                                # Script DDL y DML de la base de datos
├── siar_db.png                                # Diagrama Entidad-Relación (DER)
└── siar/                                      # Proyecto Java Ant (NetBeans)
    ├── build.xml                              # Script de construcción Ant
    ├── lib/                                   # Dependencias externas y herramientas de QA
    │   ├── jbcrypt-0.4.jar
    │   ├── mysql-connector-j-9.3.0.jar
    │   ├── junit-platform-console-standalone-1.10.3.jar
    │   ├── jacocoagent.jar
    │   └── jacococli.jar
    ├── comprobantes/                          # Directorio de salida de comprobantes HTML generados
    ├── src/
    │   └── com/siar/
    │       ├── Main.java                      # Punto de entrada de la aplicación
    │       ├── configuracion/
    │       │   ├── ConexionBaseDatos.java     # Singleton de conexión JDBC
    │       │   └── database.properties        # Parámetros de conexión MySQL
    │       ├── modelo/
    │       │   ├── Categoria.java
    │       │   ├── DetallePedido.java
    │       │   ├── Factura.java
    │       │   ├── Pedido.java
    │       │   ├── Producto.java
    │       │   ├── Rol.java
    │       │   └── Usuario.java
    │       ├── patrones/
    │       │   └── estado/
    │       │       ├── EstadoPedido.java
    │       │       ├── EstadoPendiente.java
    │       │       ├── EstadoPreparacion.java
    │       │       ├── EstadoListo.java
    │       │       ├── EstadoEntregado.java
    │       │       ├── EstadoFacturado.java
    │       │       └── EstadoCancelado.java
    │       ├── repositorio/
    │       │   ├── CategoriaRepository.java
    │       │   ├── PedidoRepository.java
    │       │   ├── ProductoRepository.java
    │       │   ├── RolRepository.java
    │       │   └── UsuarioRepository.java
    │       ├── persistencia/
    │       │   ├── AuditoriaRepositoryImpl.java
    │       │   ├── CategoriaRepositoryImpl.java
    │       │   ├── PedidoRepositoryImpl.java
    │       │   ├── ProductoRepositoryImpl.java
    │       │   ├── RolRepositoryImpl.java
    │       │   └── UsuarioRepositoryImpl.java
    │       ├── seguridad/
    │       │   └── SesionActual.java          # Singleton de sesión y RBAC
    │       ├── servicio/
    │       │   ├── AuditoriaService.java
    │       │   ├── AuthService.java
    │       │   ├── ComprobanteService.java
    │       │   ├── PedidoService.java
    │       │   ├── ProductoService.java
    │       │   └── UsuarioService.java
    │       ├── controlador/
    │       │   ├── ControladorCocina.java
    │       │   ├── ControladorDashboard.java
    │       │   ├── ControladorFacturacion.java
    │       │   ├── ControladorLogin.java
    │       │   ├── ControladorPedido.java
    │       │   ├── ControladorProducto.java
    │       │   ├── ControladorSeguimientoPedidos.java
    │       │   └── ControladorUsuario.java
    │       └── vista/
    │           ├── VistaCocina.java
    │           ├── VistaDashboard.java
    │           ├── VistaFacturacion.java
    │           ├── VistaLogin.java
    │           ├── VistaPedido.java
    │           ├── VistaPreviaComprobante.java
    │           ├── VistaProducto.java
    │           ├── VistaSeguimientoPedidos.java
    │           └── VistaUsuario.java
    └── test/
        └── com/siar/
            ├── modelo/
            │   ├── PedidoYEstadosTest.java
            │   └── ModelosYComprobanteTest.java
            └── servicio/
                ├── AuthServiceTest.java
                ├── FacturaYComprobanteTest.java
                ├── PedidoServiceTest.java
                ├── ProductoServiceTest.java
                └── UsuarioServiceTest.java

```

## 3. Stack Tecnológico y Dependencias

* **Lenguaje:** Java (JDK 22 / JDK 25).

* **Interfaz Gráfica:** Java Swing (`javax.swing`, `java.awt`).

* **Base de Datos:** MySQL / MariaDB (gestionado mediante XAMPP / phpMyAdmin).

* **Conector de Base de Datos:** `mysql-connector-j-9.3.0.jar` (JDBC).

* **Seguridad / Cifrado:** `jbcrypt-0.4.jar` (algoritmo BCrypt con *salt* aleatorio por usuario).

* **Pruebas Unitarias:** `junit-platform-console-standalone-1.10.3.jar` (JUnit 5 Jupiter).

* **Cobertura de Código:** JaCoCo (`jacocoagent.jar` y `jacococli.jar`).

* **Construcción y Entorno:** Apache Ant / Apache NetBeans IDE + automatización vía PowerShell.

* **Control de Versiones:** Git.

## 4. Módulos Funcionales y Reglas de Negocio

### Módulo 1: Gestión de Usuarios y Autenticación

* **Autenticación (`VistaLogin`, `ControladorLogin`, `AuthService`):** Valida credenciales verificando que el usuario esté activo y comparando el texto plano contra el hash almacenado mediante `BCrypt.checkpw()`. Al autenticarse, carga el usuario en `SesionActual`.

* **Recuperación de Contraseña (RF06):** Flujo directo desde la pantalla de inicio de sesión (`¿Olvidaste tu contraseña?`) que valida el correo registrado, exige una contraseña mínima de 4 caracteres, genera un nuevo hash BCrypt y registra el evento en auditoría.

* **CRUD de Usuarios y Roles (`VistaUsuario`, `ControladorUsuario`, `UsuarioService`):** Permite registrar, listar, editar (con actualización opcional de contraseña) y eliminar usuarios asignándoles uno de los 4 roles del sistema. Incluye una regla de seguridad que **impide la auto-eliminación** del usuario con sesión activa.

### Módulo 2: Gestión de Menú

* **Administración de Productos (`VistaProducto`, `ControladorProducto`, `ProductoService`):** Permite crear, modificar, eliminar y categorizar productos (`Entradas`, `Platos Fuertes`, `Bebidas`, `Postres`).

* **Control de Disponibilidad:** Cada producto cuenta con una bandera booleana `disponible`. Los productos marcados como no disponibles se ocultan automáticamente en el módulo de toma de pedidos (`ControladorPedido`).

* **Validaciones:** Rechaza nombres vacíos, precios $\le 0$ o productos sin categoría asignada.

### Módulo 4: Pedidos, Cocina (KDS) y Seguimiento

* **Creación de Pedidos (`VistaPedido`, `ControladorPedido`, `PedidoService`):** Permite seleccionar mesa, agregar/quitar productos disponibles en un carrito dinámico con cálculo automático de subtotales y guardar la orden en BD de forma atómica (transacción ACID).

* **Máquina de Estados (`com.siar.patrones.estado`):**
  

  $$
  \text{PENDIENTE} \rightarrow \text{EN\_PREPARACION} \rightarrow \text{LISTO} \rightarrow \text{ENTREGADO} \rightarrow \text{FACTURADO}
  $$

  
  Adicionalmente soporta transición a estado terminal **`CANCELADO`** únicamente desde `PENDIENTE`.

* **Monitor de Cocina KDS (`VistaCocina`, `ControladorCocina`):** Muestra las comandas activas para que cocina avance los pedidos de `PENDIENTE` a `EN_PREPARACION` y `LISTO`.

* **Seguimiento, Modificación y Cancelación (`VistaSeguimientoPedidos`, `ControladorSeguimientoPedidos`):** Permite consultar el estado en tiempo real de todos los pedidos, modificar la mesa asignada (RF19) o cancelar un pedido (RF20) bajo la regla de negocio de que **solo se permiten modificaciones o cancelaciones mientras el pedido permanezca en estado `PENDIENTE`**.

### Módulo 6: Facturación y Comprobantes Profesionales

* **Cálculo Financiero (`Factura`, `ComprobanteService`, `ControladorFacturacion`):**

  * **Subtotal (**$S$**):** Suma de los subtotales de cada `DetallePedido` ($\text{precio} \times \text{cantidad}$).

  * **Descuento (**$D$**):** Si $S \ge \$50.000$, se aplica automáticamente un descuento del $10\%$ ($D = 0.10 \times S$); en caso contrario, $D = 0$.

  * **Impuesto IVA (**$I$**):** $19\%$ calculado sobre el subtotal ($I = 0.19 \times S$).

  * **Total Final (**$T$**):** $T = S + I - D$.

* **Emisión de Comprobantes (`VistaPreviaComprobante`, `ComprobanteService`):** Genera un comprobante estructurado en **HTML profesional** con escape seguro de caracteres especiales (`&`, `<`, `>`), lo guarda en disco (`comprobantes/comprobante_pedido_<id>.html`) y ofrece vista previa integrada, impresión directa y exportación a PDF.

## 5. Matriz de Cumplimiento de Requerimientos

### Requerimientos Funcionales (RF)

| Código | Módulo | Descripción | Clases Principales | Estado | 
| ----- | ----- | ----- | ----- | ----- | 
| **RF01** | Usuarios | Registrar usuarios | `VistaUsuario`, `ControladorUsuario`, `UsuarioService`, `UsuarioRepositoryImpl` | ✅ | 
| **RF02** | Usuarios | Editar usuarios | `VistaUsuario`, `ControladorUsuario`, `UsuarioService`, `UsuarioRepositoryImpl` | ✅ | 
| **RF03** | Usuarios | Eliminar usuarios | `VistaUsuario`, `ControladorUsuario`, `UsuarioService`, `UsuarioRepositoryImpl` | ✅ | 
| **RF04** | Usuarios | Gestionar roles (Admin, Cajero, Mesero, Cocinero) | `Rol`, `RolRepositoryImpl`, `VistaUsuario` | ✅ | 
| **RF05** | Usuarios | Iniciar sesión | `VistaLogin`, `ControladorLogin`, `AuthService`, `SesionActual` | ✅ | 
| **RF06** | Usuarios | Recuperar contraseña | `VistaLogin`, `ControladorLogin`, `AuthService`, `UsuarioRepositoryImpl` | ✅ | 
| **RF07** | Menú | Crear productos | `VistaProducto`, `ControladorProducto`, `ProductoService`, `ProductoRepositoryImpl` | ✅ | 
| **RF08** | Menú | Modificar productos | `VistaProducto`, `ControladorProducto`, `ProductoService` | ✅ | 
| **RF09** | Menú | Eliminar productos | `VistaProducto`, `ControladorProducto`, `ProductoService` | ✅ | 
| **RF10** | Menú | Categorizar productos | `Categoria`, `CategoriaRepositoryImpl`, `VistaProducto` | ✅ | 
| **RF11** | Menú | Gestionar disponibilidad | `VistaProducto`, `ControladorProducto`, `ControladorPedido` | ✅ | 
| **RF17** | Pedidos | Crear pedido | `VistaPedido`, `ControladorPedido`, `PedidoService`, `PedidoRepositoryImpl` | ✅ | 
| **RF18** | Pedidos | Agregar productos al pedido | `VistaPedido`, `ControladorPedido`, `DetallePedido` | ✅ | 
| **RF19** | Pedidos | Modificar pedido (en estado `PENDIENTE`) | `VistaSeguimientoPedidos`, `ControladorSeguimientoPedidos`, `PedidoService` | ✅ | 
| **RF20** | Pedidos | Cancelar pedido (estado `CANCELADO`) | `EstadoCancelado`, `PedidoService`, `ControladorSeguimientoPedidos` | ✅ | 
| **RF21** | Pedidos | Enviar pedido a cocina (KDS) | `VistaCocina`, `ControladorCocina`, `EstadoPendiente` | ✅ | 
| **RF22** | Pedidos | Consultar estado (5 estados operativos + cancelado) | `VistaSeguimientoPedidos`, `com.siar.patrones.estado.*` | ✅ | 
| **RF28** | Facturación | Generar factura | `Factura`, `ComprobanteService`, `ControladorFacturacion` | ✅ | 
| **RF29** | Facturación | Calcular impuestos (IVA $19\%$) | `Factura.calcularImpuestos()`, `ComprobanteService` | ✅ | 
| **RF30** | Facturación | Aplicar descuentos ($10\%$ si subtotal $\ge \$50.000$) | `VistaFacturacion`, `ComprobanteService` | ✅ | 
| **RF31** | Facturación | Registrar pago / cierre de factura | `ControladorFacturacion`, `PedidoService`, `EstadoFacturado` | ✅ | 
| **RF32** | Facturación | Emitir comprobante profesional (HTML / Impresión / PDF) | `ComprobanteService`, `VistaPreviaComprobante` | ✅ | 

### Requerimientos No Funcionales (RNF)

| Código | Descripción | Implementación Técnica en SIAR | Estado | 
| ----- | ----- | ----- | ----- | 
| **RNF02** | Disponibilidad mínima del $95\%$ | Gestión controlada de conexión con Singleton (`ConexionBaseDatos`) y manejo defensivo de excepciones SQL sin cierre abrupto de la aplicación. | ✅ | 
| **RNF03** | Tiempo de respuesta $< 3$ segundos | Ejecución local en JVM, consultas SQL parametrizadas con índices y inserciones por lotes (`executeBatch()`). | ✅ | 
| **RNF04** | Contraseñas cifradas | Cifrado unidireccional con `jBCrypt` (`BCrypt.hashpw` / `BCrypt.checkpw`) en registro, edición y recuperación. | ✅ | 
| **RNF05** | Control de acceso por roles (RBAC) | `ControladorDashboard.aplicarPermisosPorRol()` habilita o restringe módulos según el rol en `SesionActual`. | ✅ | 
| **RNF06** | Registro de auditoría | Tabla `auditoria` en MySQL + `AuditoriaService` y `AuditoriaRepositoryImpl` registrando usuario, acción, detalle y marca de tiempo. | ✅ | 
| **RNF07** | Arquitectura en capas | Separación estricta en paquetes `modelo`, `repositorio`, `persistencia`, `servicio`, `controlador`, `vista`, `seguridad` y `patrones`. | ✅ | 
| **RNF08** | Uso obligatorio de Git | Repositorio versionado con `.gitignore` e historial de commits por funcionalidad. | ✅ | 
| **RNF09** | Cobertura mínima de pruebas del $70\%$ | **84% de cobertura de instrucciones** y **82.6% en líneas** verificadas con JUnit 5 y JaCoCo. | ✅ | 
| **RNF10** | Documentación técnica completa | `README.md`, script `siar_db.sql`, diagrama `siar_db.png` y reportes HTML de cobertura JaCoCo. | ✅ | 

## 6. Modelo de Base de Datos (`siar_db`)

El esquema relacional está diseñado en **InnoDB** con codificación `utf8mb4_unicode_ci` e integridad referencial estricta mediante claves foráneas (`FOREIGN KEY`).

### Tablas Principales

| Tabla | Descripción | Claves / Restricciones Destacadas | 
| ----- | ----- | ----- | 
| `roles` | Catálogo de roles del sistema (`Administrador`, `Cajero`, `Mesero`, `Cocinero`). | `UNIQUE(nombre)` | 
| `usuarios` | Cuentas de acceso con hash BCrypt y estado `activo`. | `FK(rol_id) -> roles(id)`, `UNIQUE(email)` | 
| `categorias` | Clasificación del menú (`Entradas`, `Platos Fuertes`, `Bebidas`, `Postres`). | `UNIQUE(nombre)` | 
| `productos` | Catálogo de platos y bebidas con precio y disponibilidad. | `FK(categoria_id) -> categorias(id)` | 
| `mesas` | Mesas físicas del restaurante y su disponibilidad. | `UNIQUE(numero)`, `ENUM('DISPONIBLE','OCUPADA','RESERVADA')` | 
| `clientes` | Registro de clientes para facturación. | `UNIQUE(numero_documento)` | 
| `pedidos` | Encabezado de órdenes con trazabilidad de mesa, mesero y estado. | `FK(mesa_id)`, `FK(mesero_id)`, `ENUM('PENDIENTE','EN_PREPARACION','LISTO','ENTREGADO','FACTURADO','CANCELADO')` | 
| `detalles_pedido` | Líneas de productos por pedido, cantidad, precio unitario y subtotal. | `FK(pedido_id)`, `FK(producto_id)` | 
| `facturas` | Registro contable de facturas emitidas (subtotal, IVA, descuentos, total). | `UNIQUE(pedido_id)`, `UNIQUE(numero_factura)`, `FK(cajero_id)` | 
| `metodos_pago` | Catálogo de medios de pago activos. | `UNIQUE(nombre)` | 
| `pagos` | Pagos asociados a cada factura. | `FK(factura_id)`, `FK(metodo_pago_id)` | 
| `auditoria` | Bitácora inmutable de eventos críticos (usuario, acción, detalle, fecha). | `fecha DATETIME DEFAULT CURRENT_TIMESTAMP` | 

## 7. Seguridad, Roles y Auditoría

### Matriz de Control de Acceso por Roles (RBAC)

| Módulo / Funcionalidad | Administrador | Cajero | Mesero | Cocinero | 
| ----- | ----- | ----- | ----- | ----- | 
| Gestión de Usuarios y Roles | ✅ | ❌ | ❌ | ❌ | 
| Gestión de Menú (Productos) | ✅ | ❌ | ❌ | ❌ | 
| Toma de Pedidos | ✅ | ❌ | ✅ | ❌ | 
| Seguimiento, Modificación y Cancelación | ✅ | ✅ | ✅ | ✅ | 
| Monitor de Cocina (KDS) | ✅ | ❌ | ❌ | ✅ | 
| Facturación y Comprobantes | ✅ | ✅ | ❌ | ❌ | 

### Auditoría de Eventos (RNF06)

Mediante `AuditoriaService.registrarEvento(accion, detalle)` se almacenan automáticamente en la tabla `auditoria` el correo del usuario en `SesionActual` (o `SISTEMA/ANONIMO` si ocurre previo al login), el tipo de acción (ej. `LOGIN`, `RECUPERAR_PASSWORD`, `MODIFICAR_PEDIDO`, `CANCELAR_PEDIDO`), la descripción y la fecha/hora exacta del servidor MySQL.

## 8. Instalación, Configuración y Ejecución

### Prerrequisitos

1. **Java Development Kit (JDK):** Versión 22 o 25 instalada (ej. `C:\Program Files\Java\jdk-25`).

2. **Servidor MySQL / MariaDB:** XAMPP o instalación local activa en el puerto `3306`.

3. **IDE (opcional):** Apache NetBeans (proyecto basado en Ant).

### Paso 1: Configurar la Base de Datos

1. Inicia el servicio de **MySQL** en XAMPP.

2. Abre **phpMyAdmin** y crea una base de datos llamada `siar_db`.

3. Importa el archivo `siar_db.sql` ubicado en la raíz del repositorio.

4. *(Si importaste una versión anterior de `siar_db.sql`)*, ejecuta adicionalmente en la pestaña SQL:

   ```
   ALTER TABLE pedidos 
   MODIFY COLUMN estado ENUM('PENDIENTE','EN_PREPARACION','LISTO','ENTREGADO','FACTURADO','CANCELADO') 
   DEFAULT 'PENDIENTE';
   
   CREATE TABLE IF NOT EXISTS auditoria (
       id INT AUTO_INCREMENT PRIMARY KEY,
       usuario_email VARCHAR(100) NOT NULL,
       accion VARCHAR(100) NOT NULL,
       detalle VARCHAR(255) NOT NULL,
       fecha DATETIME DEFAULT CURRENT_TIMESTAMP
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
   
   ```

### Paso 2: Verificar Credenciales de Conexión

Revisa el archivo `siar/src/com/siar/configuracion/database.properties`:

```
db.url=jdbc:mysql://localhost:3306/siar_db?useSSL=false&serverTimezone=UTC
db.user=root
db.password=

```

### Paso 3: Ejecutar la Aplicación

* **Desde NetBeans:** Abre la carpeta `siar/`, verifica que en **Libraries** estén `jbcrypt-0.4.jar` y `mysql-connector-j-9.3.0.jar`, haz **Clean and Build** y ejecuta `com.siar.Main`.

* **Usuario administrador inicial:**

  * **Correo:** `admin@siar.com`

  * **Rol:** `Administrador`

## 9. Pruebas Unitarias y Cobertura de Código (JUnit 5 + JaCoCo)

La suite de pruebas unitarias valida de forma aislada toda la lógica de negocio, modelos, patrones de estado y seguridad sin depender de una conexión activa a MySQL (gracias a la inyección de repositorios simulados en memoria).

### Suite de Pruebas Implementada

1. `PedidoYEstadosTest`: Verifica el cálculo de totales, recálculo de subtotales en `DetallePedido` y todas las transiciones del patrón **State**.

2. `ModelosYComprobanteTest`: Valida entidades (`Usuario`, `Rol`, `Factura`), escape HTML en `ComprobanteService`, reglas sin descuento y escritura/limpieza de archivos en disco.

3. `AuthServiceTest`: Prueba inicio de sesión exitoso con BCrypt, rechazo por contraseña inválida, usuario inexistente y cierre de sesión en `SesionActual`.

4. `FacturaYComprobanteTest`: Verifica cálculo de IVA ($19\%$) y aplicación automática de descuento del $10\%$ en pedidos $\ge \$50.000$.

5. `PedidoServiceTest`: Valida restricciones al crear pedidos (carrito vacío, mesa faltante, guardado exitoso y avance de estado).

6. `ProductoServiceTest`: Valida el CRUD completo de productos, listado de categorías y rechazo de precios inválidos o campos obligatorios vacíos.

7. `UsuarioServiceTest`: Verifica registro de usuarios con cifrado BCrypt, validaciones de entrada, actualización con/sin cambio de contraseña y bloqueo de auto-eliminación.

### Resultados Verificados de Cobertura (JaCoCo)

| Paquete | Cobertura de Instrucciones | Clases Cubiertas | 
| ----- | ----- | ----- | 
| `com.siar.patrones.estado` | **100%** | 5 / 5 | 
| `com.siar.servicio` | **88%** | 5 / 5 | 
| `com.siar.modelo` | **78%** | 7 / 7 | 
| `com.siar.seguridad` | **73%** | 1 / 1 | 
| **TOTAL GLOBAL** | **84%** *(82.6% en líneas)* | **18 / 18 (0 sin cubrir)** | 

> Cumple holgadamente el requerimiento **RNF09** (cobertura mínima del $70\%$).

### Ejecución Automatizada desde PowerShell

Desde la carpeta `siar/` (`D:\aplicacioDesa\SIAR\siar`), ejecuta los siguientes comandos:

```
# 1. Asegurar JDK 25 en la sesión actual
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# 2. Compilar las pruebas unitarias
New-Item -ItemType Directory -Force -Path build\test\classes | Out-Null
javac -encoding UTF-8 -cp "build\classes;lib\*" -d build\test\classes `
  test\com\siar\modelo\*.java `
  test\com\siar\servicio\*.java

# 3. Ejecutar JUnit 5 instrumentado con el agente de JaCoCo
java -javaagent:lib\jacocoagent.jar=destfile=build\jacoco.exec `
  -jar lib\junit-platform-console-standalone-1.10.3.jar execute `
  --class-path "build\classes;build\test\classes;lib\jbcrypt-0.4.jar;lib\mysql-connector-j-9.3.0.jar" `
  --scan-class-path

# 4. Generar el reporte HTML de cobertura
java -jar lib\jacococli.jar report build\jacoco.exec `
  --classfiles build\classes\com\siar\modelo `
  --classfiles build\classes\com\siar\patrones `
  --classfiles build\classes\com\siar\seguridad `
  --classfiles build\classes\com\siar\servicio `
  --sourcefiles src `
  --html build\reporte-cobertura

# 5. Abrir el reporte en el navegador
start build\reporte-cobertura\index.html

```

## 10. Notas Técnicas y Solución de Problemas

* **Desajuste de versión de bytecode (`wrong version 66.0, should be 55.0`):** Ocurre si NetBeans compila con JDK 22/25 pero PowerShell tiene un JDK antiguo en el `PATH` global. Se resuelve estableciendo `$env:JAVA_HOME` hacia `jdk-25` antes de ejecutar `javac` y `java`.

* **Ejecución de JUnit 5 en proyectos Ant de NetBeans:** El ejecutor clásico de Ant puede reportar `No runnable methods` con el JAR `junit-platform-console-standalone`. Utilizar la ejecución directa por línea de comandos con `junit-platform-console-standalone-1.10.3.jar` garantiza la ejecución completa del motor **JUnit Jupiter** junto con la instrumentación de **JaCoCo**.

* **Actualización de pruebas tras extender repositorios:** Si se agregan nuevos métodos a interfaces en `com.siar.repositorio` (como `actualizarPasswordPorEmail` o `actualizarMesaPedido`), asegúrate de implementar dichos métodos en las clases anónimas (*fakes*) dentro de `test/` antes de recompilar.