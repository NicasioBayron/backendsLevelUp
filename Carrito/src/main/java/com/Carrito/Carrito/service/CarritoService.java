package com.Carrito.Carrito.service;

import com.Carrito.Carrito.dto.CarritoDTO;
import com.Carrito.Carrito.model.Carrito;
import com.Carrito.Carrito.model.ItemCarrito;
import com.Carrito.Carrito.repository.CarritoRepository;
import com.Carrito.Carrito.repository.ItemCarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    public Carrito obtenerOCrearCarrito(Long usuarioId, String sessionToken) {
        if (usuarioId != null) {
            return carritoRepository.findByUsuarioIdAndActivo(usuarioId, true)
                    .orElseGet(() -> carritoRepository.save(Carrito.builder()
                            .usuarioId(usuarioId)
                            .sessionToken(sessionToken)
                            .fechaCreacion(LocalDateTime.now())
                            .fechaActualizacion(LocalDateTime.now())
                            .activo(true)
                            .build()));
        } else if (sessionToken != null && !sessionToken.isBlank()) {
            return carritoRepository.findBySessionTokenAndActivo(sessionToken, true)
                    .orElseGet(() -> carritoRepository.save(Carrito.builder()
                            .sessionToken(sessionToken)
                            .fechaCreacion(LocalDateTime.now())
                            .fechaActualizacion(LocalDateTime.now())
                            .activo(true)
                            .build()));
        } else {
            throw new IllegalArgumentException("Debe proporcionar usuarioId o sessionToken para el carrito.");
        }
    }

    public Optional<Carrito> obtenerPorId(Long id) {
        return carritoRepository.findById(id);
    }

    @Transactional
    public Carrito agregarItem(Long carritoId, CarritoDTO.AgregarItemRequest request) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        Optional<ItemCarrito> itemExistente = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, request.getProductoId());

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
            itemCarritoRepository.save(item);
        } else {
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .productoId(request.getProductoId())
                    .cantidad(request.getCantidad())
                    .build();
            carrito.getItems().add(nuevoItem);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidad(Long carritoId, Long productoId, int nuevaCantidad) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        ItemCarrito item = itemCarritoRepository.findByCarritoIdAndProductoId(carritoId, productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no está en el carrito"));

        item.setCantidad(nuevaCantidad);
        itemCarritoRepository.save(item);

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarItem(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.getItems().removeIf(item -> item.getProductoId().equals(productoId));
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void vaciarCarrito(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado"));

        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }
}
