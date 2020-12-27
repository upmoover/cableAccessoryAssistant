package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.CorrugatedPipe;
import org.upmoover.cableAccessoryAssistant.repositories.CorrugatedPipeRepository;

import java.util.ArrayList;

@Service
public class СorrugatedPipeService {

    CorrugatedPipeRepository corrugatedPipeRepository;

    //внедрение зависимости для репозитория

    @Autowired
    public void setСorrugatedPipeRepository(CorrugatedPipeRepository corrugatedPipeRepository) {
        this.corrugatedPipeRepository = corrugatedPipeRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CorrugatedPipe> findAllFromBase() {
        ArrayList<CorrugatedPipe> СorrugatedPipeS = (ArrayList<CorrugatedPipe>) corrugatedPipeRepository.findAll();
        return СorrugatedPipeS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneСorrugatedPipeToBase(CorrugatedPipe corrugatedPipe) {
        corrugatedPipeRepository.save(corrugatedPipe);
    }

    //метод для удаления кабеля из базы по id
    public void deleteСorrugatedPipeById(Long id) {
        corrugatedPipeRepository.deleteById(id);
    }

}
