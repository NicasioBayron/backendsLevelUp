package com.Carrito.Carrito.controller;

import com.Carrito.Carrito.dto.CarritoDTO;
import com.Carrito.Carrito.model.Carrito;
import com.Carrito.Carrito.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<Carrito> obtenerCarrito(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) String sessionToken) {
        return ResponseEntity.ok(carritoService.obtenerOCrearCarrito(usuarioId, sessionToken));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return carritoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<?> agregarItem(
            @PathVariable Long id,
            @Valid @RequestBody CarritoDTO.AgregarItemRequest request) {
        try {
            Carrito carrito = carritoService.agregarItem(id, request);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/items/{productoId}")
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable Long id,
            @PathVariable Long productoId,
            @Valid @RequestBody CarritoDTO.ActualizarCantidadRequest request) {
        try {
            Carrito carrito = carritoService.actualizarCantidad(id, productoId, request.getCantidad());
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/items/{productoId}")
    public ResponseEntity<?> eliminarItem(
            @PathVariable Long id,
            @PathVariable Long productoId) {
        try {
            Carrito carrito = carritoService.eliminarItem(id, productoId);
            return ResponseEntity.ok(carrito);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long id) {
        try {
            carritoService.vaciarCarrito(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

