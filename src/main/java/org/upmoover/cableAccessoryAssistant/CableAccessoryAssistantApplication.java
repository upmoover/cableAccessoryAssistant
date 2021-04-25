package org.upmoover.cableAccessoryAssistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.upmoover.cableAccessoryAssistant.entities.Cable;

import java.util.ArrayList;

@SpringBootApplication
public class CableAccessoryAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CableAccessoryAssistantApplication.class, args);
    }

}
