package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.СorrugatedPipe;
import org.upmoover.cableAccessoryAssistant.repositories.СorrugatedPipeRepository;

import java.util.ArrayList;

@Service
public class СorrugatedPipeService {

    СorrugatedPipeRepository corrugatedPipeRepository;

    //внедрение зависимости для репозитория

    @Autowired
    public void setСorrugatedPipeRepository(СorrugatedPipeRepository сorrugatedPipeRepository) {
        this.corrugatedPipeRepository = сorrugatedPipeRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<СorrugatedPipe> findAllFromBase() {
        ArrayList<СorrugatedPipe> СorrugatedPipeS = (ArrayList<СorrugatedPipe>) corrugatedPipeRepository.findAll();
        return СorrugatedPipeS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneСorrugatedPipeToBase(СorrugatedPipe corrugatedPipe) {
        corrugatedPipeRepository.save(corrugatedPipe);
    }

    //метод для удаления кабеля из базы по id
    public void deleteСorrugatedPipeById(Long id) {
        corrugatedPipeRepository.deleteById(id);
    }

}
