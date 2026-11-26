package com.Usuario.Usuario_service.controller;

import com.Usuario.Usuario_service.dto.UsuarioDTO;
import com.Usuario.Usuario_service.model.Usuario;
import com.Usuario.Usuario_service.service.UsuarioService;
import com.Usuario.Usuario_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDTO.RegistroRequest request) {
        try {
            Usuario usuario = usuarioService.registrar(request);
            String token = jwtUtil.generarToken(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.AuthResponse.fromUsuario(usuario, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UsuarioDTO.LoginRequest request) {
        try {
            Usuario usuario = usuarioService.login(request);
            String token = jwtUtil.generarToken(usuario);
            return ResponseEntity.ok(UsuarioDTO.AuthResponse.fromUsuario(usuario, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", "Token no proporcionado"));
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.extraerClaims(token);
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "subject", claims.getSubject(),
                    "id", claims.get("id"),
                    "rol", claims.get("rol"),
                    "expiracion", claims.getExpiration()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "error", "Token inválido o expirado"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody UsuarioDTO.UpdateRequest request) {
        try {
            Usuario actualizado = usuarioService.actualizar(id, request);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
