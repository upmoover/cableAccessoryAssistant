package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandRgg;

@Repository
public interface CableGlandRggRepository extends JpaRepository<CableGlandRgg, Long> {
    CableGlandRgg findFirstByMaxDiameterGreaterThan(Float diameter);
}
