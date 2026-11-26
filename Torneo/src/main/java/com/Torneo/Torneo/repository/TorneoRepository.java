package com.Torneo.Torneo.repository;

import com.Torneo.Torneo.model.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    List<Torneo> findAllByOrderByFechaAsc();
}
