package com.Pagos.Pagos.service;

import com.Pagos.Pagos.dto.PagoDTO;
import com.Pagos.Pagos.model.Pago;
import com.Pagos.Pagos.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> obtenerPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public Optional<Pago> obtenerPorNumeroOrden(String numeroOrden) {
        return pagoRepository.findByNumeroOrden(numeroOrden);
    }

    public Pago procesarPago(PagoDTO.ProcesarPagoRequest req) {
        String authCode = "AUTH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Pago pago = Pago.builder()
                .ordenId(req.getOrdenId())
                .numeroOrden(req.getNumeroOrden())
                .monto(req.getMonto())
                .metodoPago(req.getMetodoPago())
                .estado("APROBADO")
                .codigoAutorizacion(authCode)
                .fechaPago(LocalDateTime.now())
                .build();

        return pagoRepository.save(pago);
    }
}
