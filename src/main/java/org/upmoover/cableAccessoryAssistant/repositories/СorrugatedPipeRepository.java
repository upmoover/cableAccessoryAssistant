package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.СorrugatedPipe;

@Repository
public interface СorrugatedPipeRepository extends JpaRepository<СorrugatedPipe, Long> {
}
