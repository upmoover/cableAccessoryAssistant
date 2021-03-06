package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;

import java.util.ArrayList;

@Repository
public interface CableGlandMgRepository extends JpaRepository<CableGlandMG, Long> {
    CableGlandMG findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(Float diameterStart, Float diameterEnd);

    @Query("select id from CableGlandMG")
    ArrayList<Long> getAllIds();
}
