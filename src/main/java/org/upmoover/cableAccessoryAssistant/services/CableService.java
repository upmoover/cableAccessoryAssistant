package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.entities.*;
import org.upmoover.cableAccessoryAssistant.repositories.*;
import org.upmoover.cableAccessoryAssistant.utils.Corrections;
import org.upmoover.cableAccessoryAssistant.utils.SelectedAccessory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class CableService {
    StoredValues correction;
    StoredValues min;
    StoredValues max;

    CableRepository cableRepository;

    private ArrayList<Cable> cablesWithDesignatedAccessories = new ArrayList<>();

    public ArrayList<Cable> getCablesWithDesignatedAccessories() {
        return cablesWithDesignatedAccessories;
    }

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

    StoredValuesRepository storedValuesRepository;

    @Autowired
    public void setStoredValuesRepository(StoredValuesRepository storedValuesRepository) {
        this.storedValuesRepository = storedValuesRepository;
    }

    CableGlandRggRepository cableGlandRggRepository;

    @Autowired
    public void setCableGlandRggRepository(CableGlandRggRepository cableGlandRggRepository) {
        this.cableGlandRggRepository = cableGlandRggRepository;
    }

    CorrugatedPipeMetalRepository corrugatedPipeMetalRepository;

    @Autowired
    public void setCorrugatedPipeMetalRepository(CorrugatedPipeMetalRepository corrugatedPipeMetalRepository) {
        this.corrugatedPipeMetalRepository = corrugatedPipeMetalRepository;
    }

    CableGlandPgRepository cableGlandPgRepository;

    @Autowired
    public void setCableGlandPgRepository(CableGlandPgRepository cableGlandPgRepository) {
        this.cableGlandPgRepository = cableGlandPgRepository;
    }

    CableGlandMgRepository cableGlandMgRepository;

    @Autowired
    public void setCableGlandMgRepository(CableGlandMgRepository cableGlandMgRepository) {
        this.cableGlandMgRepository = cableGlandMgRepository;
    }

    CorrugatedPipePlasticRepository corrugatedPipePlasticRepository;

    @Autowired
    public void setCorrugatedPipePlasticRepository(CorrugatedPipePlasticRepository corrugatedPipePlasticRepository) {
        this.corrugatedPipePlasticRepository = corrugatedPipePlasticRepository;
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
    public HashMap<String, ArrayList<String>> countAccessories(ArrayList<Cable> cables, String[] startLocation, String[] cableGlandTypeStart, String[] corrugatedPipeStart, String[] endLocation, String[] corrugatedPipeEnd, String[] cableGlandTypeEnd, String[] corrugatedPipeStartLength, String[] corrugatedPipeEndLength, String inputCorrection, String inputMin, String inputMax) {

        if (storedValuesRepository.findByName("correction") == null) {
            correction = new StoredValues("correction", 0F);
            storedValuesRepository.save(correction);
        } else correction = storedValuesRepository.findByName("correction");

        if (storedValuesRepository.findByName("min") == null) {
            min = new StoredValues("min", 0F);
            storedValuesRepository.save(min);
        } else min = storedValuesRepository.findByName("min");

        if (storedValuesRepository.findByName("max") == null) {
            max = new StoredValues("max", 0F);
            storedValuesRepository.save(max);
        } else max = storedValuesRepository.findByName("max");


        if (!inputCorrection.equals("")) {
            correction = storedValuesRepository.findByName("correction");
            correction.setValue(Float.valueOf(inputCorrection.toString().replace(",", ".")));
        }

        if (!inputMin.equals("")) {
            min = storedValuesRepository.findByName("min");
            min.setValue(Float.valueOf(inputMin.toString().replace(",", ".")));
        }

        if (!inputMax.equals("")) {
            max = storedValuesRepository.findByName("max");
            max.setValue(Float.valueOf(inputMax.toString().replace(",", ".")));
        }

        if (!inputCorrection.equals(storedValuesRepository.findByName("correction").getValue())) {
            storedValuesRepository.save(correction);
        }

        if (!inputCorrection.equals(storedValuesRepository.findByName("min").getValue())) {
            storedValuesRepository.save(min);
        }

        if (!inputCorrection.equals(storedValuesRepository.findByName("max").getValue())) {
            storedValuesRepository.save(max);
        }

        ArrayList<Location> locationsList = new ArrayList<>();
        HashMap<String, ArrayList<String>> locationList = new HashMap<>();
        //добавление всех возможных локаций в список
        for (Location location : locationsRepository.findAll()) {
            locationsList.add(location);
        }

        setStartEndCableGLandType(cables, cableGlandTypeStart, cableGlandTypeEnd);
        setStartEndLocation(cables, startLocation, endLocation);
        setCorrugatedPipeStart(cables, corrugatedPipeStart, corrugatedPipeStartLength, locationsList, locationList);
        setCorrugatedPipeEnd(cables, corrugatedPipeEnd, corrugatedPipeEndLength);
        setCorrugatedPipeStartEndLength(cables, corrugatedPipeStartLength, corrugatedPipeEndLength, locationsList, locationList);
        for (int i = 0; i < cables.size(); i++) {
            cablesWithDesignatedAccessories.add(new Cable(cables.get(i).getDesignation(), cables.get(i).getName(), cables.get(i).getLength(), cables.get(i).getStartLocation(), cables.get(i).getEndLocation(), cables.get(i).getCorrugatedPipeStartLength(), cables.get(i).getCorrugatedPipeEndLength(), cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeEnd()));
        }
        selectionStartEndAccessories(cables, locationsList);
        sumCorrugatedPipe(cables, locationsList);


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

            for (CorrugatedPipe cp : location.getCorrugatedPipeList().keySet()
            ) {
                accessoryQuantity.add(cp.getName() + " = " + location.getCorrugatedPipeList().get(cp) + " м.");
            }

            if (location.getGlandsList().size() != 0)
                locationList.put(location.toString(), accessoryQuantity);

        }
        return locationList;
    }


    //method for assign type of cable gland (for start and end locations), filled from form
    public void setStartEndCableGLandType(ArrayList<Cable> cables, String[] cableGlandTypeStart, String[] cableGlandTypeEnd) {
        for (int i = 0; i < cables.size(); i++) {
            cables.get(i).setCableGlandTypeStart(cableGlandTypeStart[i].split("=")[1]);
            cables.get(i).setCableGlandTypeEnd(cableGlandTypeEnd[i].split("=")[1]);
        }
    }

    //method for assign location of cable (for start and end cable location), filled from form
    public void setStartEndLocation(ArrayList<Cable> cables, String[] startLocation, String[] endLocation) {
        for (int i = 0; i < cables.size(); i++) {
            cables.get(i).setStartLocation(startLocation[i].split("=")[1]);
            cables.get(i).setEndLocation(endLocation[i].split("=")[1]);
        }
    }

    //method for assign corrugated pipe (start cable location)
    public void setCorrugatedPipeStart(ArrayList<Cable> cables, String[] corrugatedPipeStart, String[] corrugatedPipeStartLength, ArrayList<Location> locationsList, HashMap<String, ArrayList<String>> locationList) {
        for (int i = 0; i < cables.size(); i++) {
            if (!corrugatedPipeStart[i].split("=")[1].equals("none")) {
                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipePlastic());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipePlastic());
                }

                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    cables.get(i).setCorrugatedPipeStart(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()).getcorrugatedPipePlastic());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipeMetal());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    cables.get(i).setCorrugatedPipeStart(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                }

                if (!corrugatedPipeStartLength[i].equals(""))
                    cables.get(i).setCorrugatedPipeStartLength(Float.parseFloat(corrugatedPipeStartLength[i]));
            }
        }
    }

    //method for assign corrugated pipe (end cable location)
    public void setCorrugatedPipeEnd(ArrayList<Cable> cables, String[] corrugatedPipeEnd, String[] corrugatedPipeEndLength) {
        for (int i = 0; i < cables.size(); i++) {
            if (!corrugatedPipeEnd[i].split("=")[1].equals("none")) {
                if (corrugatedPipeEnd[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipePlastic());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipePlastic());
                }

                if (corrugatedPipeEnd[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    cables.get(i).setCorrugatedPipeEnd(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()).getcorrugatedPipePlastic());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipeMetal());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    cables.get(i).setCorrugatedPipeEnd(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                }

                if (!corrugatedPipeEndLength[i].equals(""))
                    cables.get(i).setCorrugatedPipeEndLength(Float.parseFloat(corrugatedPipeEndLength[i]));
            }
        }
    }

    public void setCorrugatedPipeStartEndLength(ArrayList<Cable> cables, String[] corrugatedPipeStartLength, String[] corrugatedPipeEndLength, ArrayList<Location> locationsList, HashMap<String, ArrayList<String>> locationList) {
        Float startLength;
        Float endLength;

        for (int i = 0; i < cables.size(); i++) {
            if (!corrugatedPipeStartLength[i].isEmpty()) {
                if ((startLength = Float.parseFloat(corrugatedPipeStartLength[i].replace(',', '.'))) > 0)
                    cables.get(i).setCorrugatedPipeStartLength(startLength);
            }

            if (!corrugatedPipeEndLength[i].isEmpty()) {
                if ((endLength = Float.parseFloat(corrugatedPipeEndLength[i].replace(',', '.'))) > 0)
                    cables.get(i).setCorrugatedPipeEndLength(endLength);
            }
        }
    }

    public void selectionStartEndAccessories(ArrayList<Cable> cables, ArrayList<Location> locationsList) {
        for (int i = 0; i < cables.size(); i++) {
            String[] startEndAccessories = {cables.get(i).getCableGlandTypeStart(), cables.get(i).getCableGlandTypeEnd()};

            CorrugatedPipe corrugatedPipeStart = cables.get(i).getCorrugatedPipeStart();
            CorrugatedPipe corrugatedPipeEnd = cables.get(i).getCorrugatedPipeEnd();

            if (corrugatedPipeStart instanceof CorrugatedPipeMetal) {
                startEndAccessories[0] = "MB";
            }

            if (corrugatedPipeEnd instanceof CorrugatedPipeMetal) {
                startEndAccessories[1] = "MB";
            }

            for (int m = 0; m < startEndAccessories.length; m++) {

                if (!(startEndAccessories[m] == null))

                    switch (startEndAccessories[m]) {
                        case ("PG"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandPg());
                                    }

                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandPg());
                                    }

                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandPg());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandPg());
                                    }
                                }
                            }

                            break;

                        case ("MG"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandMg());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandPg());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandMg());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cables.get(i).getOuterDiameter(), cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandPg());
                                    }
                                }
                            }
                            break;

                        case ("RGG"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                                        cables.get(i).setCorrugatedPipeStart(cables.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandRgg());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                            cables.get(i).setCorrugatedPipePlastic(corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                                        cables.get(i).setCorrugatedPipeStart(cables.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCableGlandRgg());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                                        cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandRgg());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                            cables.get(i).setCorrugatedPipePlastic(corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCableGlandRgg());
                                        cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCableGlandRgg());
                                    }
                                }
                            }
                            break;

                        case ("MB"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                        }
                                        cables.get(i).setCorrugatedPipeMetal(cables.get(i).getCorrugatedPipeMetal());
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                    if (locationsList.get(j).getName().equals(cables.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cables.get(i).getOuterDiameter().equals(cableRepository.findCableByName(cables.get(i).getName()).getOuterDiameter())) {
                                            cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cables.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                                        }
                                        cables.get(i).setCorrugatedPipeMetal(cables.get(i).getCorrugatedPipeMetal());
                                        locationsList.get(j).getGlandsList().add(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cables.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                }
                            }
                    }
            }
        }

    }

    /*public void countSum(ArrayList<Cable> cables, ArrayList<Location> locationsList, HashMap<String, ArrayList<String>> locationList) {
        for (int i = 0; i < cables.size(); i++) {
            for (int j = 0; j < locationsList.size(); j++) {
                if (locationsList.get(j).getName().equals(cables.get(i).getName())) {
                    if (locationList.containsKey(cables.get(i).getCorrugatedPipeStart().getName())) {
                        Float sum = locationsList.get(j).getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeStart().getName());
                        locationsList.get(j).getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), sum + cables.get(i).getCorrugatedPipeStart().getLength());
                    }
                }
            }
        }
    }*/

    public void sumCorrugatedPipe(ArrayList<Cable> cables, ArrayList<Location> locationsList) {
        for (int i = 0; i < cables.size(); i++) {

            float sum;
            boolean firstAdd = false;

            for (Location location : locationsList
            ) {

                if (location.getName().equals(cables.get(i).getStartLocation()) & !(cables.get(i).getCorrugatedPipeStart() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeStartLength());
                        firstAdd = true;
                    }

                    if (location.getCorrugatedPipeList().containsKey(cables.get(i).getCorrugatedPipeStart()) & !firstAdd) {
                        float temp = location.getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeStart());
                        sum = temp + cables.get(i).getCorrugatedPipeStartLength();
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), sum);
                        firstAdd = false;
                    } else {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeStartLength());
                        firstAdd = false;
                    }
                }

                if (location.getName().equals(cables.get(i).getEndLocation()) & !(cables.get(i).getCorrugatedPipeEnd() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), cables.get(i).getCorrugatedPipeEndLength());
                        firstAdd = true;
                    }

                    if (location.getCorrugatedPipeList().containsKey(cables.get(i).getCorrugatedPipeEnd()) & !firstAdd) {
                        float temp = location.getCorrugatedPipeList().get(cables.get(i).getCorrugatedPipeEnd());
                        sum = temp + cables.get(i).getCorrugatedPipeEndLength();
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), sum);
                        firstAdd = false;
                    } else {
                        location.getCorrugatedPipeList().put(cables.get(i).getCorrugatedPipeEnd(), cables.get(i).getCorrugatedPipeEndLength());
                        firstAdd = false;
                    }
                }
            }
        }
    }
}
