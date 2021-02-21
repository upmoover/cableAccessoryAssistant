package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipeMetal;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipePlastic;

@Repository
public interface CorrugatedPipeMetalRepository extends JpaRepository<CorrugatedPipeMetal, Long> {
    CorrugatedPipeMetal findFirstByInnerDiameterGreaterThan(Float diameter);
}
