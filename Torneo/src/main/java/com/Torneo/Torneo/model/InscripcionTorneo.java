package com.Torneo.Torneo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripciones_torneo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscripcionTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    @JsonBackReference(value = "torneo-inscripciones")
    private Torneo torneo;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre_jugador", nullable = false, length = 100)
    private String nombreJugador;

    @Column(name = "correo_jugador", nullable = false, length = 150)
    private String correoJugador;

    @Column(name = "fecha_inscripcion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaInscripcion = LocalDateTime.now();

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String estado = "CONFIRMADA";
}
