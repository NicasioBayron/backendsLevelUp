package com.Pagos.Pagos.controller;

import com.Pagos.Pagos.dto.PagoDTO;
import com.Pagos.Pagos.model.Pago;
import com.Pagos.Pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{numeroOrden}")
    public ResponseEntity<?> obtenerPorNumeroOrden(@PathVariable String numeroOrden) {
        return pagoService.obtenerPorNumeroOrden(numeroOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarPago(@Valid @RequestBody PagoDTO.ProcesarPagoRequest req) {
        try {
            Pago pago = pagoService.procesarPago(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(pago);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
