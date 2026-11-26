--liquibase formatted sql
--changeset Bayron:1
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(300),
    comuna VARCHAR(100)
);

--changeset Bayron:2 validCheckSum: ANY
INSERT INTO usuarios (nombre, correo, password, rol, telefono, direccion, comuna)
VALUES ('Eileen Salsas', 'eileensalas890@gmail.com', 'Nose1234', 'ADMIN', NULL, NULL, NULL),
('Admin Demo', 'admin@levelup.test', 'admin123', 'ADMIN', NULL, NULL, NULL);

--changeset Bayron:3
INSERT INTO usuarios (nombre, correo, password, rol, telefono, direccion, comuna)
VALUES
('Ana Lopez', 'ana.lopez@levelup.test', 'cliente123', 'CLIENTE', '56911111101', 'Av. Los Aromos 101', 'Santiago'),
('Benito Perez', 'benito.perez@levelup.test', 'cliente123', 'CLIENTE', '56911111102', 'Calle Norte 202', 'Providencia'),
('Carla Muñoz', 'carla.munoz@levelup.test', 'cliente123', 'CLIENTE', '56911111103', 'Pasaje Central 303', 'Maipu'),
('Diego Rojas', 'diego.rojas@levelup.test', 'cliente123', 'CLIENTE', '56911111104', 'Av. del Parque 404', 'Nunoa'),
('Elena Torres', 'elena.torres@levelup.test', 'cliente123', 'CLIENTE', '56911111105', 'Calle Las Flores 505', 'La Florida'),
('Felipe Castro', 'felipe.castro@levelup.test', 'cliente123', 'CLIENTE', '56911111106', 'Av. Costanera 606', 'Las Condes'),
('Gabriela Silva', 'gabriela.silva@levelup.test', 'cliente123', 'CLIENTE', '56911111107', 'Pasaje El Sol 707', 'Puente Alto'),
('Hector Morales', 'hector.morales@levelup.test', 'cliente123', 'CLIENTE', '56911111108', 'Calle Los Pinos 808', 'San Miguel'),
('Isabel Fuentes', 'isabel.fuentes@levelup.test', 'cliente123', 'CLIENTE', '56911111109', 'Av. Libertad 909', 'Estacion Central'),
('Javier Reyes', 'javier.reyes@levelup.test', 'cliente123', 'CLIENTE', '56911111110', 'Calle Mirador 110', 'Quilicura'),
('Karen Diaz', 'karen.diaz@levelup.test', 'cliente123', 'CLIENTE', '56911111111', 'Av. Los Robles 211', 'Recoleta'),
('Luis Herrera', 'luis.herrera@levelup.test', 'cliente123', 'CLIENTE', '56911111112', 'Pasaje Oriente 312', 'La Reina'),
('Maria Navarro', 'maria.navarro@levelup.test', 'cliente123', 'CLIENTE', '56911111113', 'Calle El Bosque 413', 'Macul'),
('Nicolas Vargas', 'nicolas.vargas@levelup.test', 'cliente123', 'CLIENTE', '56911111114', 'Av. Independencia 514', 'Independencia'),
('Olivia Contreras', 'olivia.contreras@levelup.test', 'cliente123', 'CLIENTE', '56911111115', 'Pasaje Los Olmos 615', 'Peñalolen'),
('Pablo Ramirez', 'pablo.ramirez@levelup.test', 'cliente123', 'CLIENTE', '56911111116', 'Calle Nueva 716', 'Cerrillos'),
('Romina Soto', 'romina.soto@levelup.test', 'cliente123', 'CLIENTE', '56911111117', 'Av. Vicuña 817', 'La Cisterna'),
('Sebastian Molina', 'sebastian.molina@levelup.test', 'cliente123', 'CLIENTE', '56911111118', 'Calle El Alba 918', 'Vitacura'),
('Tamara Castillo', 'tamara.castillo@levelup.test', 'cliente123', 'CLIENTE', '56911111119', 'Pasaje Sur 119', 'Pudahuel'),
('Victor Espinoza', 'victor.espinoza@levelup.test', 'cliente123', 'CLIENTE', '56911111120', 'Av. Los Leones 220', 'Huechuraba');
