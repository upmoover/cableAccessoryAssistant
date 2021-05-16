package org.upmoover.cableAccessoryAssistant.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.upmoover.cableAccessoryAssistant.controllers.MainController;
import org.upmoover.cableAccessoryAssistant.entities.*;
import org.upmoover.cableAccessoryAssistant.repositories.*;
import org.upmoover.cableAccessoryAssistant.utils.Corrections;
import org.upmoover.cableAccessoryAssistant.utils.SelectedAccessory;
import org.upmoover.cableAccessoryAssistant.utils.Shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class CableService {
    StoredValues correction;
    StoredValues min;
    StoredValues max;

    MainController mainController;

    @Autowired
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

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
        } else if (!inputCorrection.equals("")) {
            correction = storedValuesRepository.findByName("correction");
            correction.setValue(Float.parseFloat(inputCorrection.replace(",", ".")));
            storedValuesRepository.save(correction);
        } else correction = storedValuesRepository.findByName("correction");


        if (storedValuesRepository.findByName("min") == null) {
            min = new StoredValues("min", 0F);
            storedValuesRepository.save(min);
        } else if (!inputMin.equals("")) {
            StoredValues min = new StoredValues();
            min = storedValuesRepository.findByName("min");
            min.setValue(Float.parseFloat(inputMin.replace(",", ".")));
            storedValuesRepository.save(min);
        } else min = storedValuesRepository.findByName("min");


        if (storedValuesRepository.findByName("max") == null) {
            max = new StoredValues("max", 0F);
            storedValuesRepository.save(max);
        } else if (!inputMax.equals("")) {
            StoredValues max = new StoredValues();
            max = storedValuesRepository.findByName("max");
            max.setValue(Float.parseFloat(inputMax.replace(",", ".")));
            storedValuesRepository.save(max);
        } else max = storedValuesRepository.findByName("max");

        ArrayList<Location> locationsList = new ArrayList<>();
        HashMap<String, ArrayList<String>> locationList = new HashMap<>();
        //добавление всех возможных локаций в список
        for (Location location : locationsRepository.findAll()) {
            locationsList.add(location);
        }

        setStartEndCableGLandType(cables, cableGlandTypeStart, cableGlandTypeEnd);
        setStartEndLocation(cables, startLocation, endLocation);
        setCorrugatedPipeStart(cables, corrugatedPipeStart, corrugatedPipeStartLength);
        setCorrugatedPipeEnd(cables, corrugatedPipeEnd, corrugatedPipeEndLength);
        setCorrugatedPipeStartEndLength(cables, corrugatedPipeStartLength, corrugatedPipeEndLength, locationsList, locationList);
        cablesWithDesignatedAccessories.clear();
        for (int i = 0; i < cables.size(); i++) {
            //cablesWithDesignatedAccessories.add(new Cable(cables.get(i).getDesignation(), cables.get(i).getName(), cables.get(i).getLength(), cables.get(i).getStartLocation(), cables.get(i).getEndLocation(), cables.get(i).getCorrugatedPipeStartLength(), cables.get(i).getCorrugatedPipeEndLength(), cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeEnd()));
            cablesWithDesignatedAccessories.add(new Cable(cables.get(i).getName(), cables.get(i).getOuterDiameter(), cables.get(i).getDesignation(), cables.get(i).getLength(), cables.get(i).getStartLocation(), cables.get(i).getCableGlandTypeStart(), cables.get(i).getEndLocation(), cables.get(i).getCableGlandTypeEnd(), cables.get(i).getCableGlandPg(), cables.get(i).getCableGlandMg(), cables.get(i).getCableGlandRgg(), cables.get(i).getCorrugatedPipePlastic(), cables.get(i).getCorrugatedPipeMetal(), cables.get(i).getCorrugatedPipeStart(), cables.get(i).getCorrugatedPipeEnd(), cables.get(i).getOuterDiameterFromBase(), cables.get(i).getCorrugatedPipeStartLength(), cables.get(i).getCorrugatedPipeEndLength()));
        }
//        selectionStartEndAccessories(cables, locationsList);
        selectionStartEndAccessories(cablesWithDesignatedAccessories, locationsList);
        sumCorrugatedPipe(cablesWithDesignatedAccessories, locationsList);


        for (Location location : locationsList
        ) {
            if (!location.getGlandsList().isEmpty()) {
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
    public void setCorrugatedPipeStart(ArrayList<Cable> cables, String[] corrugatedPipeStart, String[] corrugatedPipeStartLength) {
        for (int i = 0; i < cables.size(); i++) {
            if (!corrugatedPipeStart[i].split("=")[1].equals("none")) {
                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipePlastic());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipePlastic());
                }

                if (corrugatedPipeStart[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    if ((cables.get(i).getOuterDiameterFromBase() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameterFromBase(), min.getValue(), max.getValue(), correction.getValue())) == cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue())) {
                        cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    }
                    cables.get(i).setCorrugatedPipeStart(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()).getcorrugatedPipePlastic());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeStart(cables.get(i).getCorrugatedPipeMetal());
                }
                if (corrugatedPipeStart[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    if ((cables.get(i).getOuterDiameterFromBase() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameterFromBase(), min.getValue(), max.getValue(), correction.getValue())) == cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue())) {
                        cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    }
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
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeEnd(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cables.get(i).getOuterDiameter()));
                }

                //todo постоянное прибавление наружного диаметра
                if (corrugatedPipeEnd[i].split("=")[1].equals("plastic") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    if ((cables.get(i).getOuterDiameterFromBase() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameterFromBase(), min.getValue(), max.getValue(), correction.getValue())) == cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue())) {
                        cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    }
                    cables.get(i).setCorrugatedPipeEnd(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cables.get(i).getOuterDiameter()).getcorrugatedPipePlastic());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() == 0) {
                    cables.get(i).setCorrugatedPipeEnd(cables.get(i).getCorrugatedPipeMetal());
                }
                if (corrugatedPipeEnd[i].split("=")[1].equals("metal") && storedValuesRepository.findByName("correction").getValue() != 0) {
                    if ((cables.get(i).getOuterDiameterFromBase() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameterFromBase(), min.getValue(), max.getValue(), correction.getValue())) == cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue())) {
                        cables.get(i).setOuterDiameter(cables.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cables.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                    }
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

    public void selectionStartEndAccessories(ArrayList<Cable> cablesWithAccessories, ArrayList<Location> locationsList) {

//        mainController.setCablesFields(Shared.listCablesFromFile);

        for (int i = 0; i < cablesWithAccessories.size(); i++) {
            String[] startEndAccessories = {cablesWithAccessories.get(i).getCableGlandTypeStart(), cablesWithAccessories.get(i).getCableGlandTypeEnd()};

            CorrugatedPipe corrugatedPipeStart = cablesWithAccessories.get(i).getCorrugatedPipeStart();
            CorrugatedPipe corrugatedPipeEnd = cablesWithAccessories.get(i).getCorrugatedPipeEnd();

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
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandPg() != null)
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        else
                                            cablesWithAccessories.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandPg());
                                    }

                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        //TODO здесь NPE, так как у кабеля, который без имени, идет попытка найти его по имени в базе и не находит
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandPg());
                                    }

                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandPg() != null)
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        else
                                            cablesWithAccessories.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandPg());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            //TODO NULL разобраться
                                            //при повторном запросе подбора аксессуаров outerDiameter у кабеля суммируется с прошлым outerDiameter (который был установлен через коррекцию)
                                            cablesWithAccessories.get(i).setCableGlandPg(cableGlandPgRepository.findFirstByMaxDiameterGreaterThanEqualAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandPg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandPg());
                                    }
                                }
                            }

                            break;

                        case ("MG"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandMg() != null)
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        else
                                            cablesWithAccessories.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandMg());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandMg());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandMg() != null)
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        else
                                            cablesWithAccessories.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandMg());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCableGlandMg(cableGlandMgRepository.findFirstByMaxDiameterGreaterThanAndMinDiameterLessThanEqual(cablesWithAccessories.get(i).getOuterDiameter(), cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandMg());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandMg());
                                    }
                                }
                            }
                            break;

                        case ("RGG"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandRgg() != null) {
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        } else {
                                            cablesWithAccessories.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        }
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandRgg());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            cablesWithAccessories.get(i).setCorrugatedPipePlastic(corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                        cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCableGlandRgg());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCableGlandRgg() != null) {
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                            cablesWithAccessories.get(i).setCorrugatedPipeEnd(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        } else {
                                            cablesWithAccessories.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        }
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandRgg());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCableGlandRgg(cableGlandRggRepository.findFirstByMaxDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            cablesWithAccessories.get(i).setCorrugatedPipePlastic(corrugatedPipePlasticRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCableGlandRgg());
                                        cablesWithAccessories.get(i).setCorrugatedPipeEnd(cablesWithAccessories.get(i).getCableGlandRgg().getcorrugatedPipePlastic());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCableGlandRgg());
                                    }
                                }
                            }
                            break;

                        case ("MB"):
                            for (int j = 0; j < locationsList.size(); j++) {
                                if (m == 0) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (cablesWithAccessories.get(i).getCorrugatedPipeMetal() != null) {
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        } else {
                                            cablesWithAccessories.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        }
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getStartLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        cablesWithAccessories.get(i).setCorrugatedPipeMetal(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                }
                                if (m == 1) {
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() == 0) {
                                        if (!cablesWithAccessories.get(i).getCorrugatedPipeMetal().equals(null)) {
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                            cablesWithAccessories.get(i).setCorrugatedPipeEnd(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        } else {
                                            cablesWithAccessories.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                            locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                            cablesWithAccessories.get(i).setCorrugatedPipeStart(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        }
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                    if (locationsList.get(j).getName().equals(cablesWithAccessories.get(i).getEndLocation()) && storedValuesRepository.findByName("correction").getValue() != 0) {
                                        if (cablesWithAccessories.get(i).getOuterDiameter().equals(cablesWithAccessories.get(i).getOuterDiameterFromBase())) {
                                            cablesWithAccessories.get(i).setOuterDiameter(cablesWithAccessories.get(i).getOuterDiameter() + Corrections.makeCorrectionByStandardSize(cablesWithAccessories.get(i).getOuterDiameter(), min.getValue(), max.getValue(), correction.getValue()));
                                            cablesWithAccessories.get(i).setCorrugatedPipeMetal(corrugatedPipeMetalRepository.findFirstByInnerDiameterGreaterThan(cablesWithAccessories.get(i).getOuterDiameter()));
                                        }
                                        cablesWithAccessories.get(i).setCorrugatedPipeMetal(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        locationsList.get(j).getGlandsList().add(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                        cablesWithAccessories.get(i).setCorrugatedPipeEnd(cablesWithAccessories.get(i).getCorrugatedPipeMetal());
                                        cablesWithDesignatedAccessories.get(i).setSelectedCableGlandEnd(cablesWithAccessories.get(i).getCorrugatedPipeMetal().getCableGlandMB());
                                    }
                                }
                            }
                            break;
                    }
            }
        }

    }

    public void sumCorrugatedPipe(ArrayList<Cable> cablesWithAccessories, ArrayList<Location> locationsList) {
        for (int i = 0; i < cablesWithAccessories.size(); i++) {

            float sum;
            boolean firstAdd = false;

            for (Location location : locationsList
            ) {

                if (location.getName().equals(cablesWithAccessories.get(i).getStartLocation()) && !(cablesWithAccessories.get(i).getCorrugatedPipeStart() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeStart(), cablesWithAccessories.get(i).getCorrugatedPipeStartLength());
                        firstAdd = true;
                    }

                    if (location.getCorrugatedPipeList().containsKey(cablesWithAccessories.get(i).getCorrugatedPipeStart()) & !firstAdd) {
                        float temp = location.getCorrugatedPipeList().get(cablesWithAccessories.get(i).getCorrugatedPipeStart());
                        sum = temp + cablesWithAccessories.get(i).getCorrugatedPipeStartLength();
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeStart(), sum);
                        firstAdd = false;
                    } else {
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeStart(), cablesWithAccessories.get(i).getCorrugatedPipeStartLength());
                        firstAdd = false;
                    }
                }

                if (location.getName().equals(cablesWithAccessories.get(i).getEndLocation()) && !(cablesWithAccessories.get(i).getCorrugatedPipeEnd() == null)) {
                    if (location.getCorrugatedPipeList().size() == 0) {
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeEnd(), cablesWithAccessories.get(i).getCorrugatedPipeEndLength());
                        firstAdd = true;
                    }

                    if (location.getCorrugatedPipeList().containsKey(cablesWithAccessories.get(i).getCorrugatedPipeEnd()) & !firstAdd) {
                        float temp = location.getCorrugatedPipeList().get(cablesWithAccessories.get(i).getCorrugatedPipeEnd());
                        sum = temp + cablesWithAccessories.get(i).getCorrugatedPipeEndLength();
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeEnd(), sum);
                        firstAdd = false;
                    } else {
                        location.getCorrugatedPipeList().put(cablesWithAccessories.get(i).getCorrugatedPipeEnd(), cablesWithAccessories.get(i).getCorrugatedPipeEndLength());
                        firstAdd = false;
                    }
                }
            }
        }
    }
}
