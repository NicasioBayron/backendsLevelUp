package com.Carrito.Carrito.repository;

import com.Carrito.Carrito.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioIdAndActivo(Long usuarioId, Boolean activo);
    Optional<Carrito> findBySessionTokenAndActivo(String sessionToken, Boolean activo);
}
