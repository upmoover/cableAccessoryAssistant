package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.СorrugatedPipe;

@Repository
public interface CableGlandMgRepository extends JpaRepository<CableGlandMG, Long> {
    CableGlandMG findFirstByMaxDiameterGreaterThanAndMinDiameterLessThan(Float diameterStart, Float diameterEnd);
}
