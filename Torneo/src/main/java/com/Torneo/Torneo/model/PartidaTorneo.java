package com.Torneo.Torneo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "partidas_torneo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    @JsonBackReference(value = "torneo-partidas")
    private Torneo torneo;

    @Column(nullable = false, length = 50)
    private String ronda;

    @Column(name = "jugador_uno", nullable = false, length = 100)
    private String jugadorUno;

    @Column(name = "jugador_dos", nullable = false, length = 100)
    private String jugadorDos;

    @Column(name = "puntaje_uno")
    private Integer puntajeUno;

    @Column(name = "puntaje_dos")
    private Integer puntajeDos;

    @Column(length = 100)
    private String ganador;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String estado = "PENDIENTE";

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 1;
}
