package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.repositories.CableRepository;

import java.util.ArrayList;

@Service
public class CableService {

    CableRepository cableRepository;

    //внедрение зависимости для репозитория
    @Autowired
    public void setCableRepository(CableRepository cableRepository) {

        this.cableRepository = cableRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<Cable> findAllFromBase() {
        ArrayList<Cable> cables = (ArrayList<Cable>) cableRepository.findAll();
        return cables;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneCableToBase(Cable cable) {
        cableRepository.save(cable);
    }

    //метод для удаления кабеля из базы по id
    public void deleteCableById(Long id) {
        cableRepository.deleteById(id);
    }

}
