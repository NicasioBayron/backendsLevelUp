--liquibase formatted sql
--changeset Bayron:1
CREATE TABLE torneos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    juego VARCHAR(100) NOT NULL,
    descripcion TEXT,
    modalidad VARCHAR(50) NOT NULL DEFAULT 'LIGUILLA',
    premio VARCHAR(150) DEFAULT 'Premio sorpresa',
    imagen VARCHAR(250) NOT NULL DEFAULT 'valorant.png',
    fecha DATE NOT NULL,
    max_participantes INT NOT NULL CHECK (max_participantes >= 2),
    estado VARCHAR(50) NOT NULL DEFAULT 'Inscripciones abiertas',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--changeset Bayron:2
CREATE TABLE inscripciones_torneo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    torneo_id BIGINT NOT NULL,
    usuario_id BIGINT NULL,
    nombre_jugador VARCHAR(100) NOT NULL,
    correo_jugador VARCHAR(150) NOT NULL,
    fecha_inscripcion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'CONFIRMADA',
    FOREIGN KEY (torneo_id) REFERENCES torneos(id) ON DELETE CASCADE,
    UNIQUE KEY uk_torneo_usuario (torneo_id, usuario_id)
);

--changeset Bayron:3
CREATE TABLE partidas_torneo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    torneo_id BIGINT NOT NULL,
    ronda VARCHAR(50) NOT NULL,
    jugador_uno VARCHAR(100) NOT NULL,
    jugador_dos VARCHAR(100) NOT NULL,
    puntaje_uno INT DEFAULT NULL,
    puntaje_dos INT DEFAULT NULL,
    ganador VARCHAR(100) DEFAULT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    orden INT NOT NULL DEFAULT 1,
    FOREIGN KEY (torneo_id) REFERENCES torneos(id) ON DELETE CASCADE
);

--changeset Bayron:4
INSERT INTO torneos (id, titulo, juego, descripcion, modalidad, premio, imagen, fecha, max_participantes, estado) VALUES
(1, 'Level Up Valorant Cup', 'Valorant', 'Participa en nuestro campeonato, demuestra tus habilidades y compite con otros jugadores de la comunidad Level Up Coffee.', 'ELIMINACION', 'Premio sorpresa', 'valorant.png', '2026-12-15', 16, 'Inscripciones abiertas'),
(2, 'Mario Kart Championship', 'Mario Kart', 'Compite en emocionantes carreras, demuestra tu habilidad al volante y conviértete en el campeón de Level Up Coffee.', 'LIGUILLA', 'Premio sorpresa', 'mariokart.png', '2026-12-20', 12, 'Inscripciones abiertas');
