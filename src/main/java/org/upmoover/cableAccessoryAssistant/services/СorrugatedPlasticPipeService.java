package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipe;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipePlastic;
import org.upmoover.cableAccessoryAssistant.repositories.CorrugatedPipePlasticRepository;

import java.util.ArrayList;

@Service
public class СorrugatedPlasticPipeService {

    CorrugatedPipePlasticRepository corrugatedPipePlasticRepository;

    //внедрение зависимости для репозитория

    @Autowired
    public void setСorrugatedPipePlasticRepository(CorrugatedPipePlasticRepository corrugatedPipePlasticRepository) {
        this.corrugatedPipePlasticRepository = corrugatedPipePlasticRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CorrugatedPipePlastic> findAllFromBase() {
        ArrayList<CorrugatedPipePlastic> СorrugatedPlasticPipeS = (ArrayList<CorrugatedPipePlastic>) corrugatedPipePlasticRepository.findAll();
        return СorrugatedPlasticPipeS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneСorrugatedPipePlasticToBase(CorrugatedPipePlastic corrugatedPipePlastic) {
        corrugatedPipePlasticRepository.save(corrugatedPipePlastic);
    }

    //метод для удаления кабеля из базы по id
    public void deleteСorrugatedPipePlasticById(Long id) {
        corrugatedPipePlasticRepository.deleteById(id);
    }

}
