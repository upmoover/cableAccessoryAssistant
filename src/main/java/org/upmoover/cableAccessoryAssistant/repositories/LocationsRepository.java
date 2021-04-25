package org.upmoover.cableAccessoryAssistant.repositories;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.upmoover.cableAccessoryAssistant.entities.Location;

@Repository
public interface LocationsRepository extends JpaRepository<Location, Long> {
}
