package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandRgg;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandRggRepository;

import java.util.ArrayList;

@Service
public class CableGlandRggService {

    CableGlandRggRepository CableGlandRggRepository, с;

    //внедрение зависимости для репозитория

    @Autowired
    public void setCableGlandRggRepository(CableGlandRggRepository CableGlandRggRepository) {
        this.CableGlandRggRepository = CableGlandRggRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CableGlandRgg> findAllFromBase() {
        ArrayList<CableGlandRgg> CableGlandRggS = (ArrayList<CableGlandRgg>) CableGlandRggRepository.findAll();
        return CableGlandRggS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneCableGlandRggToBase(CableGlandRgg CableGlandRgg) {
        CableGlandRggRepository.save(CableGlandRgg);
    }

    //метод для удаления кабеля из базы по id
    public void deleteCableGlandRggById(Long id) {
        CableGlandRggRepository.deleteById(id);
    }

}
