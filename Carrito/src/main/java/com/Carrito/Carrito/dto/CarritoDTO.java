package com.Carrito.Carrito.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class CarritoDTO {

    @Data
    public static class AgregarItemRequest {
        @NotNull(message = "productoId es obligatorio")
        private Long productoId;

        @NotNull(message = "cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;
    }

    @Data
    public static class ActualizarCantidadRequest {
        @NotNull(message = "cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;
    }
}
