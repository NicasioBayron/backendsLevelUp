package com.Torneo.Torneo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "torneos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 100)
    private String juego;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String modalidad = "LIGUILLA";

    @Column(length = 150)
    @Builder.Default
    private String premio = "Premio sorpresa";

    @Column(nullable = false, length = 250)
    @Builder.Default
    private String imagen = "valorant.png";

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "max_participantes", nullable = false)
    private Integer maxParticipantes;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String estado = "Inscripciones abiertas";

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "torneo-inscripciones")
    @Builder.Default
    private List<InscripcionTorneo> inscripciones = new ArrayList<>();

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "torneo-partidas")
    @Builder.Default
    private List<PartidaTorneo> partidas = new ArrayList<>();
}
