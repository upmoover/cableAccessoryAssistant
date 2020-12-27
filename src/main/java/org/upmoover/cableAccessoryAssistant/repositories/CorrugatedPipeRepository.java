package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipe;

@Repository
public interface CorrugatedPipeRepository extends JpaRepository<CorrugatedPipe, Long> {
    CorrugatedPipe findFirstByInnerDiameterGreaterThan(Float diameter);
}
