package com.Torneo.Torneo.repository;

import com.Torneo.Torneo.model.PartidaTorneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaTorneoRepository extends JpaRepository<PartidaTorneo, Long> {
    List<PartidaTorneo> findByTorneoIdOrderByOrdenAsc(Long torneoId);
}
