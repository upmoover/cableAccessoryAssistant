package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGland;

@Repository
public interface CableGlandRepository extends JpaRepository<CableGland, Long> {
}
