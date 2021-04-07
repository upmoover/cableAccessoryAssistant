package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandRgg;

import java.util.ArrayList;

@Repository
public interface CableGlandRggRepository extends JpaRepository<CableGlandRgg, Long> {
    CableGlandRgg findFirstByMaxDiameterGreaterThan(Float diameter);

    @Query("select id from CableGlandRgg")
    ArrayList<Long> getAllIds();
}
