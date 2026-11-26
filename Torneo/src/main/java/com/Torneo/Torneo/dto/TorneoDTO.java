package com.Torneo.Torneo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public class TorneoDTO {

    @Data
    public static class CrearTorneoRequest {
        @NotBlank(message = "El título es obligatorio")
        private String title;

        @NotBlank(message = "El juego es obligatorio")
        private String game;

        private String description;

        private String mode; // 'LIGUILLA' o 'ELIMINACION'

        private String prize;

        private String image;

        @NotNull(message = "La fecha es obligatoria")
        private LocalDate date;

        @NotNull(message = "El máximo de participantes es obligatorio")
        @Min(value = 2, message = "Debe permitir al menos 2 participantes")
        private Integer maxParticipants;
    }

    @Data
    public static class InscribirJugadorRequest {
        private Long usuarioId;

        @NotBlank(message = "El nombre del jugador es obligatorio")
        private String nombreJugador;

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Correo no válido")
        private String correoJugador;
    }

    @Data
    public static class ActualizarPartidaRequest {
        private Integer puntajeUno;
        private Integer puntajeDos;
        private String ganador;
        private String estado;
    }
}
