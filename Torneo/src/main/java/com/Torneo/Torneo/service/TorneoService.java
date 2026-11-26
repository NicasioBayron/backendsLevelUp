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

        if (inscripcionTorneoRepository.findByTorneoId(torneoId).size() >= torneo.getMaxParticipantes()) {
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

        InscripcionTorneo saved = inscripcionTorneoRepository.save(inscripcion);
        if ("ELIMINACION".equalsIgnoreCase(torneo.getModalidad())) {
            actualizarFixtureConParticipantes(torneo);
        } else if ("LIGUILLA".equalsIgnoreCase(torneo.getModalidad())) {
            actualizarLiguilla(torneo);
        }
        return saved;
    }

    private void actualizarFixtureConParticipantes(Torneo torneo) {
        List<PartidaTorneo> partidas = partidaTorneoRepository.findByTorneoIdOrderByOrdenAsc(torneo.getId());
        List<String> jugadores = inscripcionTorneoRepository.findByTorneoId(torneo.getId()).stream()
                .map(InscripcionTorneo::getNombreJugador).toList();
        int cuadro = 1;
        while (cuadro < torneo.getMaxParticipantes()) cuadro *= 2;
        int rondas = Integer.numberOfTrailingZeros(cuadro);
        int orden = partidas.stream().mapToInt(PartidaTorneo::getOrden).max().orElse(0) + 1;
        for (int rondaNumero = 1; rondaNumero <= rondas; rondaNumero++) {
            String nombre = nombreRonda(cuadro, rondaNumero);
            int enfrentamientos = cuadro / (int) Math.pow(2, rondaNumero);
            long existentes = partidas.stream().filter(p -> nombre.equals(p.getRonda())).count();
            for (int i = (int) existentes; i < enfrentamientos; i++) {
                partidas.add(PartidaTorneo.builder().torneo(torneo).ronda(nombre)
                        .jugadorUno("Por definir").jugadorDos("Por definir").orden(orden++)
                        .estado("PENDIENTE").build());
            }
        }
        partidaTorneoRepository.saveAll(partidas);
        partidas = partidaTorneoRepository.findByTorneoIdOrderByOrdenAsc(torneo.getId());
        String primeraRonda = nombreRonda(cuadro, 1);
        List<PartidaTorneo> primerasPartidas = partidas.stream().filter(p -> primeraRonda.equals(p.getRonda())).toList();
        int jugadorIndex = 0;
        for (PartidaTorneo partida : primerasPartidas) {
            partida.setJugadorUno(jugadorIndex < jugadores.size() ? jugadores.get(jugadorIndex++) : "Por definir");
            partida.setJugadorDos(jugadorIndex < jugadores.size() ? jugadores.get(jugadorIndex++) : "Por definir");
        }
        partidaTorneoRepository.saveAll(primerasPartidas);
    }

    private String nombreRonda(int cuadro, int ronda) {
        int enfrentamientos = cuadro / (int) Math.pow(2, ronda);
        if (enfrentamientos == 1) return "Final";
        if (enfrentamientos == 2) return "Semifinales";
        if (enfrentamientos == 4) return "Cuartos de final";
        return "Octavos de final";
    }

    private void actualizarLiguilla(Torneo torneo) {
        List<InscripcionTorneo> inscripciones = inscripcionTorneoRepository.findByTorneoId(torneo.getId());
        List<PartidaTorneo> partidas = partidaTorneoRepository.findByTorneoIdOrderByOrdenAsc(torneo.getId());
        int orden = partidas.stream().mapToInt(PartidaTorneo::getOrden).max().orElse(0) + 1;
        for (int i = 0; i < inscripciones.size(); i++) {
            for (int j = i + 1; j < inscripciones.size(); j++) {
                String uno = inscripciones.get(i).getNombreJugador();
                String dos = inscripciones.get(j).getNombreJugador();
                boolean existe = partidas.stream().anyMatch(p ->
                        (uno.equals(p.getJugadorUno()) && dos.equals(p.getJugadorDos())) ||
                        (uno.equals(p.getJugadorDos()) && dos.equals(p.getJugadorUno())));
                if (!existe) {
                    partidas.add(PartidaTorneo.builder().torneo(torneo).ronda("Jornada única")
                            .jugadorUno(uno).jugadorDos(dos).orden(orden++)
                            .estado("PENDIENTE").build());
                }
            }
        }
        partidaTorneoRepository.saveAll(partidas);
    }

    @Transactional
    public void retirarJugador(Long torneoId, Long usuarioId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));
        int eliminadas = inscripcionTorneoRepository.deleteByTorneoIdAndUsuarioId(torneoId, usuarioId);
        if (eliminadas == 0) throw new IllegalArgumentException("El usuario no está inscrito en este torneo.");
        if ("ELIMINACION".equalsIgnoreCase(torneo.getModalidad())) actualizarFixtureConParticipantes(torneo);
    }

    @Transactional
    public void eliminarTorneo(Long torneoId) {
        if (!torneoRepository.existsById(torneoId)) {
            throw new IllegalArgumentException("Torneo no encontrado");
        }
        torneoRepository.deleteById(torneoId);
    }

    public List<InscripcionTorneo> listarParticipantes(Long torneoId) {
        return inscripcionTorneoRepository.findByTorneoId(torneoId);
    }

    public List<PartidaTorneo> listarPartidas(Long torneoId) {
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new IllegalArgumentException("Torneo no encontrado"));
        List<PartidaTorneo> partidas = partidaTorneoRepository.findByTorneoIdOrderByOrdenAsc(torneoId);
        if (partidas.isEmpty() && "ELIMINACION".equalsIgnoreCase(torneo.getModalidad())) {
            actualizarFixtureConParticipantes(torneo);
        } else if (partidas.isEmpty() && "LIGUILLA".equalsIgnoreCase(torneo.getModalidad())) {
            actualizarLiguilla(torneo);
        }
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

