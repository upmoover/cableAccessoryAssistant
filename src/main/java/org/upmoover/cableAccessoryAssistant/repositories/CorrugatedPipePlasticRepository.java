package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipe;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipePlastic;

@Repository
public interface CorrugatedPipePlasticRepository extends JpaRepository<CorrugatedPipePlastic, Long> {
    CorrugatedPipePlastic findFirstByInnerDiameterGreaterThan(Float diameter);
}
