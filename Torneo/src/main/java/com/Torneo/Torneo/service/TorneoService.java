package com.Torneo.Torneo.service;

import com.Torneo.Torneo.dto.TorneoDTO;
import com.Torneo.Torneo.model.InscripcionTorneo;
import com.Torneo.Torneo.model.PartidaTorneo;
import com.Torneo.Torneo.model.Torneo;
import com.Torneo.Torneo.repository.InscripcionTorneoRepository;
import com.Torneo.Torneo.repository.PartidaTorneoRepository;
import com.Torneo.Torneo.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private InscripcionTorneoRepository inscripcionTorneoRepository;

    @Autowired
    private PartidaTorneoRepository partidaTorneoRepository;

    public List<Torneo> listarTodos() {
        return torneoRepository.findAllByOrderByFechaAsc();
    }

    public Optional<Torneo> obtenerPorId(Long id) {
        return torneoRepository.findById(id);
    }

    @Transactional
    public Torneo crearTorneo(TorneoDTO.CrearTorneoRequest req) {
        Torneo torneo = Torneo.builder()
                .titulo(req.getTitle())
                .juego(req.getGame())
                .descripcion(req.getDescription() != null ? req.getDescription() : "")
                .modalidad(req.getMode() != null ? req.getMode() : "LIGUILLA")
                .premio(req.getPrize() != null ? req.getPrize() : "Premio sorpresa")
                .imagen(req.getImage() != null ? req.getImage() : "valorant.png")
                .fecha(req.getDate())
                .maxParticipantes(req.getMaxParticipants())
                .estado("Inscripciones abiertas")
                .fechaCreacion(LocalDateTime.now())
                .build();

        return torneoRepository.save(torneo);
    }

    @Transactional
    public InscripcionTorneo inscribirJugador(Long torneoId, TorneoDTO.InscribirJugadorRequest req) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));

        if (torneo.getInscripciones().size() >= torneo.getMaxParticipantes()) {
            throw new IllegalArgumentException("El torneo ya alcanzó su cupo máximo de participantes.");
        }

        if (req.getUsuarioId() != null && inscripcionTorneoRepository.existsByTorneoIdAndUsuarioId(torneoId, req.getUsuarioId())) {
            throw new IllegalArgumentException("El usuario ya está inscrito en este torneo.");
        }

        InscripcionTorneo inscripcion = InscripcionTorneo.builder()
                .torneo(torneo)
                .usuarioId(req.getUsuarioId())
                .nombreJugador(req.getNombreJugador())
                .correoJugador(req.getCorreoJugador())
                .fechaInscripcion(LocalDateTime.now())
                .estado("CONFIRMADA")
                .build();

        return inscripcionTorneoRepository.save(inscripcion);
    }

    public List<InscripcionTorneo> listarParticipantes(Long torneoId) {
        return inscripcionTorneoRepository.findByTorneoId(torneoId);
    }

    public List<PartidaTorneo> listarPartidas(Long torneoId) {
        return partidaTorneoRepository.findByTorneoIdOrderByOrdenAsc(torneoId);
    }

    @Transactional
    public PartidaTorneo actualizarPartida(Long partidaId, TorneoDTO.ActualizarPartidaRequest req) {
        PartidaTorneo partida = partidaTorneoRepository.findById(partidaId)
                .orElseThrow(() -> new IllegalArgumentException("Partida no encontrada"));

        if (req.getPuntajeUno() != null) partida.setPuntajeUno(req.getPuntajeUno());
        if (req.getPuntajeDos() != null) partida.setPuntajeDos(req.getPuntajeDos());
        if (req.getGanador() != null) partida.setGanador(req.getGanador());
        if (req.getEstado() != null) partida.setEstado(req.getEstado());

        return partidaTorneoRepository.save(partida);
    }
}
