package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.CableGland;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.CableRepository;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class CableService {

    CableRepository cableRepository;

    //внедрение зависимости для репозитория
    @Autowired
    public void setCableRepository(CableRepository cableRepository) {

        this.cableRepository = cableRepository;
    }

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    //метод для поиска всех элеметов базы (аналог SELECT * FROM cable)
    public ArrayList<Cable> findAllFromBase() {
        ArrayList<Cable> cables = (ArrayList<Cable>) cableRepository.findAll();
        return cables;
    }

    //метод для поиска кабеля по имени
    public Cable findCableByName(String name) {
        return cableRepository.findCableByName(name);
    }

    public boolean isCableInTheDatabase(String name) {
        //если кабель не найден
        if (cableRepository.findCableByName(name) == null) return false;
            //если кабель найден
        else return true;
    }

    //метод для сохранения одиночного экземпляра кабеля в базу
    public void saveOneCableToBase(Cable cable) {
        cableRepository.save(cable);
    }

    //метод для удаления кабеля из базы по id
    public void deleteCableById(Long id) {
        cableRepository.deleteById(id);
    }

    //метод для сохранения кабеля в базу данных списком (например, из файла)
    public void saveCablesToBase(ArrayList<Cable> cables) {
        for (Cable cable :
                cables
        ) {
            cableRepository.save(cable);
        }

    }

    //метод для подсчета аксессуаров для разных местоположений
    public HashMap<String, ArrayList<String>> countAccessories(ArrayList<Cable> cables, @RequestParam(value = "startLocation", required = false) String[] startLocation, @RequestParam(value = "cableGlandTypeStart", required = false) String[] cableGlandTypeStart, @RequestParam(value = "corrugatedPipeStart", required = false) String[] corrugatedPipeStart, @RequestParam(value = "endLocation", required = false) String[] endLocation, @RequestParam(value = "corrugatedPipeEnd", required = false) String[] corrugatedPipeEnd, @RequestParam(value = "cableGlandTypeEnd", required = false) String[] cableGlandTypeEnd) {

        ArrayList<Location> locationsList = new ArrayList<>();
        HashMap<String, ArrayList<String>> locationList = new HashMap<>();

        for (Location location : locationsRepository.findAll()) {
            locationsList.add(location);
        }

        for (int i = 0; i < cables.size(); i++) {
            cables.get(i).setStartLocation(startLocation[i].split("=")[1]);
            cables.get(i).setCableGlandTypeStart(cableGlandTypeStart[i].split("=")[1]);
            cables.get(i).setCorrugatedPipeStart(corrugatedPipeStart[i].split("=")[1]);
            cables.get(i).setEndLocation(endLocation[i].split("=")[1]);
            cables.get(i).setCorrugatedPipeEnd(corrugatedPipeEnd[i].split("=")[1]);
            cables.get(i).setCableGlandTypeEnd(cableGlandTypeEnd[i].split("=")[1]);

            String[] startEndAccessories = {cables.get(i).getCableGlandTypeStart(), cables.get(i).getCableGlandTypeEnd()};

            for (int m = 0; m < startEndAccessories.length; m++) {

                switch (startEndAccessories[m]) {
                    case ("PG"):
                        for (int j = 0; j < locationsList.size(); j++) {
                            if (m == 0) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                            }
                            if (m == 1) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                            }
                        }

                        break;

                    case ("MG"):
                        for (int j = 0; j < locationsList.size(); j++) {
                            if (m == 0) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                            }
                            if (m == 1) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                            }
                        }
                        break;

                    case ("RGG"):
                        for (int j = 0; j < locationsList.size(); j++) {
                            if (m == 0) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                            }
                            if (m == 1) {
                                if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()))
                                    locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                            }
                        }
                        break;
                }
            }

        }

        for (Location location : locationsList
        ) {
            System.out.println(location.getName() + ":");

            HashSet<Object> uniqueCableGlands = new HashSet(location.getGlandsList());
            ArrayList<String> accessoryQuantity = new ArrayList<>();
            accessoryQuantity.clear();

            for (Object cableGland : uniqueCableGlands
            ) {
                CableGland cg = (CableGland) cableGland;
                accessoryQuantity.add(cg.getName() + " = " + Collections.frequency(location.getGlandsList(), cableGland) + " шт.");
            }
            if (location.getGlandsList().size() != 0)
                locationList.put(location.toString(), accessoryQuantity);
        }
        return locationList;
    }

}
