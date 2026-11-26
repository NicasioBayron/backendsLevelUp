package com.Pagos.Pagos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private Long ordenId;

    @Column(name = "numero_orden", nullable = false, length = 30)
    private String numeroOrden;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago; // 'DEBITO', 'CREDITO', 'WEBPAY'

    @Column(nullable = false, length = 30)
    private String estado; // 'APROBADO', 'RECHAZADO', 'PENDIENTE'

    @Column(name = "codigo_autorizacion", length = 50)
    private String codigoAutorizacion;

    @Column(name = "fecha_pago", nullable = false)
    @Builder.Default
    private LocalDateTime fechaPago = LocalDateTime.now();
}
