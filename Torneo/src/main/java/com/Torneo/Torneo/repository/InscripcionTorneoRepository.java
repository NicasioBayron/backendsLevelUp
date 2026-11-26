package com.Torneo.Torneo.repository;

import com.Torneo.Torneo.model.InscripcionTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionTorneoRepository extends JpaRepository<InscripcionTorneo, Long> {
    List<InscripcionTorneo> findByTorneoId(Long torneoId);
    Optional<InscripcionTorneo> findByTorneoIdAndUsuarioId(Long torneoId, Long usuarioId);
    boolean existsByTorneoIdAndUsuarioId(Long torneoId, Long usuarioId);

    @Modifying
    @Query("delete from InscripcionTorneo i where i.torneo.id = :torneoId and i.usuarioId = :usuarioId")
    int deleteByTorneoIdAndUsuarioId(@Param("torneoId") Long torneoId, @Param("usuarioId") Long usuarioId);
}
