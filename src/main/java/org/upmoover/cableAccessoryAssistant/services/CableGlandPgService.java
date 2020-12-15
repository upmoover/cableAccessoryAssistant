package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandPgRepository;
import org.upmoover.cableAccessoryAssistant.repositories.CableRepository;

import java.util.ArrayList;

@Service
public class CableGlandPgService {

    CableGlandPgRepository cableGlandPgRepository;

    //внедрение зависимости для репозитория
    @Autowired
    public void setCableRepository(CableGlandPgRepository cableGlandPgRepository) {

        this.cableGlandPgRepository = cableGlandPgRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<CableGlandPG> findAllFromBase() {
        ArrayList<CableGlandPG> cableGlandPGS = (ArrayList<CableGlandPG>) cableGlandPgRepository.findAll();
        return cableGlandPGS;
    }

    //метод для сохранения экземпляра кабеля в базу
    public void saveOneCableGlandPgToBase(CableGlandPG cableGlandPG) {
        cableGlandPgRepository.save(cableGlandPG);
    }
    
    //метод для удаления кабеля из базы по id
    public void deleteCableGlandPgById(Long id) {
        cableGlandPgRepository.deleteById(id);
    }
/*
    //метод для сохранения кабеля в базу данных (с проверкой на уникальность, чтобы избежать дублирования в БД)
    public void saveCableToBase(ArrayList<Cable> cables) {
        for (Cable cable :
                cables
        ) {
            cableRepository.save(cable);
        }

    }*/

}
