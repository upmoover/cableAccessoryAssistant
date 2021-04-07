package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.StoredValues;

import java.util.ArrayList;

@Repository
public interface StoredValuesRepository extends JpaRepository<StoredValues, Long> {
    StoredValues findByName(String name);
}
