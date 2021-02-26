package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public HashMap<String, ArrayList<String>> countAccessories(ArrayList<Cable> cables, String[] startLocation, String[] cableGlandTypeStart, String[] corrugatedPipeStart, String[] endLocation, String[] corrugatedPipeEnd, String[] cableGlandTypeEnd, String[] corrugatedPipeStartLength, String[] corrugatedPipeEndLength) {

        ArrayList<Location> locationsList = new ArrayList<>();
        HashMap<String, ArrayList<String>> locationList = new HashMap<>();

        for (Location location : locationsRepository.findAll()) {
            locationsList.add(location);
        }

        for (int i = 0; i < cables.size(); i++) {
            cables.get(i).setStartLocation(startLocation[i].split("=")[1]);
            cables.get(i).setCableGlandTypeStart(cableGlandTypeStart[i].split("=")[1]);

            if (!corrugatedPipeStart[i].split("=")[1].equals("none")) {
                if (corrugatedPipeStart[i].split("=")[1].equals("plastic")) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipePlastic());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("metal")) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipeMetal());
                }

                if (!corrugatedPipeStartLength[i].equals(""))
                    cables.get(i).setCorrugatedPipeStartLength(Float.parseFloat(corrugatedPipeStartLength[i]));

                for (int j = 0; j < locationsList.size(); j++) {
                    if (locationsList.get(j).getName().equals(cables.get(i).getName())) {
                        if (locationList.containsKey(cables.get(i).getCorrugatedPipeStart().getName())) {
                            Float sum = locationsList.get(j).getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeStart().getName());
                            locationsList.get(j).getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), sum + cables.get(i).getCorrugatedPipeStart().getLength());
                        }
                    }
                }
//TODO изменил тут код и выше то же самое
// эта строчка была за скобками
                cables.get(i).setEndLocation(endLocation[i].split("=")[1]);
            }

//            cables.get(i).setCorrugatedPipeStart(corrugatedPipeStart[i].split("=")[1]);

            if (!corrugatedPipeEnd[i].split("=")[1].equals("none")) {
                if (corrugatedPipeEnd[i].split("=")[1].equals("plastic")) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipePlastic());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal")) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipeMetal());
                }

                if (!corrugatedPipeEndLength[i].equals(""))
                    cables.get(i).setCorrugatedPipeEndLength(Float.parseFloat(corrugatedPipeStartLength[i]));

//                for (int j = 0; j < locationsList.size(); j++) {
//                    if (locationsList.get(j).getName().equals(cables.get(i).getName())) {
//                        if (locationList.containsKey(cables.get(i).getCorrugatedPipeEnd().getName())) {
//                            Float sum = locationsList.get(j).getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeEnd().getName());
//                            locationsList.get(j).getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), sum + cables.get(i).getCorrugatedPipeEnd().getLength());
//                        }
//                    }
//                }
//TODO изменил тут код и выше то же самое
// эта строчка была за скобками
                cables.get(i).setCableGlandTypeEnd(cableGlandTypeEnd[i].split("=")[1]);
            }

            Float startLength = null;
            Float endLength = null;
            if (!corrugatedPipeStartLength[i].isEmpty()) {
                if ((startLength = Float.parseFloat(corrugatedPipeStartLength[i].replace(',', '.'))) > 0)
                    cables.get(i).setCorrugatedPipeStartLength(startLength);
            }

            if (!corrugatedPipeEndLength[i].isEmpty()) {
                if ((endLength = Float.parseFloat(corrugatedPipeEndLength[i].replace(',', '.'))) > 0)
                    cables.get(i).setCorrugatedPipeEndLength(endLength);
            }
//TODO удалить

            //-----DELETE

            //-----DELETE

            String[] startEndAccessories = {cables.get(i).getCableGlandTypeStart(), cables.get(i).getCableGlandTypeEnd()};

            for (int m = 0; m < startEndAccessories.length; m++) {

                if (!(startEndAccessories[m] == null))

                    switch (startEndAccessories[m]) {
                        case ("PG"):
                            for (int j = 0; j < locationsList.size(); j++) {

//                            if (!locationsList.get(j).getCorrugatedPipeList().isEmpty() && locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && locationsList.get(j).getCorrugatedPipeList().containsKey(cables.get(i).getCorrugatedPipeStart().getName())) {
//                                Float sum = locationsList.get(j).getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeStart().getName());
//                                locationsList.get(j).getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart().getName(), sum + cables.get(i).getCorrugatedPipeStart().getLength());
//                            }


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

            float sum;

            for (Location location : locationsList
            ) {

                if (location.getName().equals(cables.get(i).getStartLocation()) & !(cables.get(i).getCorrugatedPipeStart() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeStartLength());
                    }

                    if (location.getCorrugatedPipeList().containsKey(cables.get(i).getCorrugatedPipeStart())) {
                        sum = location.getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeStart());
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), sum + cables.get(i).getCorrugatedPipeStartLength());
                    } else {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeStartLength());
                    }
                }

                if (location.getName().equals(cables.get(i).getEndLocation()) & !(cables.get(i).getCorrugatedPipeEnd() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), cables.get(i).getCorrugatedPipeEndLength());
                    }

                    if (location.getCorrugatedPipeList().containsKey(cables.get(i).getCorrugatedPipeEnd())) {
                        sum = location.getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeEnd());
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), sum + cables.get(i).getCorrugatedPipeEndLength());
                    } else {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeStartLength());
                    }
                }
            }


        }

        for (Location location : locationsList
        ) {
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

            //TODO дописать
            System.out.println(location.getCorrugatedPipeList().keySet().stream().map(key -> key + "=" + location.getCorrugatedPipeList().get(key)));

        }
        return locationList;
    }

}
