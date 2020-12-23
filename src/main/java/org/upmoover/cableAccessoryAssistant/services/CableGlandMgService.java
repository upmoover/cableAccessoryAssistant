package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandMgRepository;

import java.util.ArrayList;

@Service
public class CableGlandMgService {

    CableGlandMgRepository cableGlandMgRepository;

    //внедрение зависимости для репозитория

    @Autowired
    public void setCableGlandMgRepository(CableGlandMgRepository cableGlandMgRepository) {
        this.cableGlandMgRepository = cableGlandMgRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CableGlandMG> findAllFromBase() {
        ArrayList<CableGlandMG> cableGlandMGS = (ArrayList<CableGlandMG>) cableGlandMgRepository.findAll();
        return cableGlandMGS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneCableGlandMgToBase(CableGlandMG cableGlandMG) {
        cableGlandMgRepository.save(cableGlandMG);
    }

    //метод для удаления кабеля из базы по id
    public void deleteCableGlandMgById(Long id) {
        cableGlandMgRepository.deleteById(id);
    }

}
