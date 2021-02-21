package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipeMetal;
import org.upmoover.cableAccessoryAssistant.repositories.CorrugatedPipeMetalRepository;

import java.util.ArrayList;

@Service
public class СorrugatedMetalPipeService {

    CorrugatedPipeMetalRepository corrugatedPipeMetalRepository;

    //внедрение зависимости для репозитория

    @Autowired
    public void setСorrugatedPipeMetalRepository(CorrugatedPipeMetalRepository corrugatedPipeMetalRepository) {
        this.corrugatedPipeMetalRepository = corrugatedPipeMetalRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CorrugatedPipeMetal> findAllFromBase() {
        ArrayList<CorrugatedPipeMetal> СorrugatedMetalPipeS = (ArrayList<CorrugatedPipeMetal>) corrugatedPipeMetalRepository.findAll();
        return СorrugatedMetalPipeS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneСorrugatedPipeMetalToBase(CorrugatedPipeMetal corrugatedPipeMetal) {
        corrugatedPipeMetalRepository.save(corrugatedPipeMetal);
    }

    //метод для удаления кабеля из базы по id
    public void deleteСorrugatedPipeMetalById(Long id) {
        corrugatedPipeMetalRepository.deleteById(id);
    }

}
