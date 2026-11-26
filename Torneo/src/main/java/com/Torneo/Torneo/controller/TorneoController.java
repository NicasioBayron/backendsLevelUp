package com.Torneo.Torneo.controller;

import com.Torneo.Torneo.dto.TorneoDTO;
import com.Torneo.Torneo.model.InscripcionTorneo;
import com.Torneo.Torneo.model.PartidaTorneo;
import com.Torneo.Torneo.model.Torneo;
import com.Torneo.Torneo.service.TorneoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @GetMapping
    public ResponseEntity<List<Torneo>> listarTodos() {
        return ResponseEntity.ok(torneoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return torneoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearTorneo(@Valid @RequestBody TorneoDTO.CrearTorneoRequest req) {
        try {
            Torneo nuevo = torneoService.crearTorneo(req);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarTorneo(@PathVariable Long id) {
        try {
            torneoService.eliminarTorneo(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/inscripciones")
    public ResponseEntity<?> inscribirJugador(
            @PathVariable Long id,
            @Valid @RequestBody TorneoDTO.InscribirJugadorRequest req) {
        try {
            InscripcionTorneo inscripcion = torneoService.inscribirJugador(id, req);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/inscripciones")
    public ResponseEntity<List<InscripcionTorneo>> listarParticipantes(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.listarParticipantes(id));
    }

    @DeleteMapping("/{id}/inscripciones/usuario/{usuarioId}")
    public ResponseEntity<?> retirarJugador(@PathVariable Long id, @PathVariable Long usuarioId) {
        try {
            torneoService.retirarJugador(id, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/partidas")
    public ResponseEntity<List<PartidaTorneo>> listarPartidas(@PathVariable Long id) {
        return ResponseEntity.ok(torneoService.listarPartidas(id));
    }

    @PatchMapping("/partidas/{partidaId}")
    public ResponseEntity<?> actualizarPartida(
            @PathVariable Long partidaId,
            @RequestBody TorneoDTO.ActualizarPartidaRequest req) {
        try {
            PartidaTorneo partida = torneoService.actualizarPartida(partidaId, req);
            return ResponseEntity.ok(partida);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}


