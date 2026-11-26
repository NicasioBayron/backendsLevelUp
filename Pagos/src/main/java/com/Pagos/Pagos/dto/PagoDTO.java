package com.Pagos.Pagos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public class PagoDTO {

    @Data
    public static class ProcesarPagoRequest {
        @NotNull(message = "ordenId es obligatorio")
        private Long ordenId;

        @NotBlank(message = "numeroOrden es obligatorio")
        private String numeroOrden;

        @NotNull(message = "monto es obligatorio")
        @DecimalMin(value = "0.0")
        private BigDecimal monto;

        @NotBlank(message = "metodoPago es obligatorio")
        private String metodoPago; // 'WEBPAY', 'DEBITO', 'CREDITO'
    }
}
