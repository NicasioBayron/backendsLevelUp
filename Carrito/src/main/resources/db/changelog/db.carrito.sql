--liquibase formatted sql
--changeset Bayron:1
CREATE TABLE carritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NULL,
    session_token VARCHAR(100) NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo TINYINT(1) NOT NULL DEFAULT 1
);

--changeset Bayron:2
CREATE TABLE items_carrito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    FOREIGN KEY (carrito_id) REFERENCES carritos(id) ON DELETE CASCADE,
    UNIQUE KEY uk_carrito_producto (carrito_id, producto_id)
);
