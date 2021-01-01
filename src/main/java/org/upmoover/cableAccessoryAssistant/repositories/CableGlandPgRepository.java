package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;

@Repository
public interface CableGlandPgRepository extends JpaRepository<CableGlandPG, Long> {
    CableGlandPG findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(Float minDiameter, Float maxDiameter);
}
