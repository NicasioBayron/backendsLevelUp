--liquibase formatted sql
--changeset Bayron:1
CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    numero_orden VARCHAR(30) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    codigo_autorizacion VARCHAR(50),
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
