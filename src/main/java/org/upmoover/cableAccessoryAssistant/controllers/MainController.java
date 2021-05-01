package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandPgRepository;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;
import org.upmoover.cableAccessoryAssistant.repositories.StoredValuesRepository;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;
import org.upmoover.cableAccessoryAssistant.utils.ExcelUtil;
import org.upmoover.cableAccessoryAssistant.utils.Shared;

import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    CableFileReader cableFileReader;

    @Autowired
    public void setCableFileReader(CableFileReader cableFileReader) {
        this.cableFileReader = cableFileReader;
    }

    HashMap<String, ArrayList<String>> locationList = new HashMap<>();

    String pathFile = null;
    //list of cables, read from file
    ArrayList<Cable> listCables = new ArrayList<>();
    //list of cables added from the base and read from the file
    ArrayList<Cable> cables = new ArrayList<>();
    //list of cables, found in base

    ArrayList<String> cablesWithLength = new ArrayList<>();

    CableService cableService;

    boolean label = false;

    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    CableGlandPgRepository cableGlandPgRepository;

    @Autowired
    public void setCableGlandPgRepository(CableGlandPgRepository cableGlandPgRepository) {
        this.cableGlandPgRepository = cableGlandPgRepository;
    }

    StoredValuesRepository storedValuesRepository;

    @Autowired
    public void setStoredValuesRepository(StoredValuesRepository storedValuesRepository) {
        this.storedValuesRepository = storedValuesRepository;
    }

    //вернуть стартовую страницу
    @GetMapping("/")
    public String homepage() {
        return "index";
    }

    //вернуть страницу справки
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    //вернуть страницу добавления списка кабелей для подбора аксессуаров
    @GetMapping("/start")
    public String start() {
        return "start";
    }

    //получение списка кабелей для подбора аксессуаров из файла
    @GetMapping("/start/read-cables-list")
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView readCablesList(@RequestParam String pathFile) {
        Shared.isSkipCablesSelected = false;
        Shared.isUnknown = false;
        this.pathFile = pathFile;
        listCables.clear();
        cables.clear();
        listCables = cableFileReader.readFile(this.pathFile);
        return showCableList(listCables);
    }

    //считать список кабеля, вывести страницу со списком и выбором аксессуаров
    public ModelAndView showCableList(ArrayList<Cable> listCables) {
        ModelAndView modelAndView = new ModelAndView();
        Shared.uniqueNotFoundCables.clear();
        //поиск кабеля из списка в базе: если все кабели присутствуют в БД - они выводится на страницу, если нет - выводится предупреждение и возможность добавить недостающий кабель в БД или пропустить этот шаг

        setCablesFields(listCables);

        Shared.notFoundCables = new ArrayList<>(Shared.uniqueNotFoundCables);
        modelAndView.addObject("cables", cables);
        //получение из базы списка местоположений
        ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("notFoundCables", Shared.notFoundCables);
        modelAndView.addObject("unknownCables", Shared.unknownCables);
        modelAndView.addObject("correction", storedValuesRepository.findByName("correction").getValue());
        modelAndView.addObject("min", storedValuesRepository.findByName("min").getValue());
        modelAndView.addObject("max", storedValuesRepository.findByName("max").getValue());
        modelAndView.setViewName("show-cables-for-selection-accessories");
        return modelAndView;
    }

    //TODO разбить метод установки аксессуаров

    public void setCablesFields(ArrayList<Cable> listCables) {
        Cable cable;
        Shared.cablesFoundInBase.clear();
        Shared.uniqueNotFoundCables.clear();
//        cableService.getCablesWithDesignatedAccessories().clear();
        //поиск кабеля из списка в базе: если все кабели присутствуют в БД - они выводится на страницу, если нет - выводится предупреждение и возможность добавить недостающий кабель в БД или пропустить этот шаг
        for (int i = 0; i < listCables.size(); i++) {
            if ((cable = cableService.findCableByName(listCables.get(i).getName())) != null) {
                cables.add(listCables.get(i));
                cables.get(cables.size() - 1).setId(cable.getId());
                cables.get(cables.size() - 1).setCableGlandRgg(cable.getCableGlandRgg());
                cables.get(cables.size() - 1).setCableGlandPg(cable.getCableGlandPg());
                cables.get(cables.size() - 1).setCableGlandMg(cable.getCableGlandMg());
                cables.get(cables.size() - 1).setCorrugatedPipePlastic(cable.getCorrugatedPipePlastic());
                cables.get(cables.size() - 1).setCorrugatedPipeMetal(cable.getCorrugatedPipeMetal());
                cables.get(cables.size() - 1).setCorrugatedPipeStartLength(cable.getCorrugatedPipeStartLength());
                cables.get(cables.size() - 1).setCorrugatedPipeEndLength(cable.getCorrugatedPipeEndLength());
                cables.get(cables.size() - 1).setOuterDiameter(cable.getOuterDiameter());
                Shared.cablesFoundInBase.add(cables.get(cables.size() - 1));
            }
            //add cables, that was not found in a base
            else {
                Shared.uniqueNotFoundCables.add(listCables.get(i));
            }
        }

        //if there are no cables added to the list - clear the cable list to add missing cables to the base
        if (!Shared.uniqueNotFoundCables.isEmpty()) cables.clear();
    }

    @PostMapping("/start/get-attributes")
    @ResponseStatus(value = HttpStatus.OK)
    public String getCableAttributes(Model model, @RequestParam(value = "startLocation", required = false) String[] startLocation, @RequestParam(value = "cableGlandTypeStart", required = false) String[] cableGlandTypeStart, @RequestParam(value = "corrugatedPipeStart", required = false) String[] corrugatedPipeStart, @RequestParam(value = "endLocation", required = false) String[] endLocation, @RequestParam(value = "corrugatedPipeEnd", required = false) String[] corrugatedPipeEnd, @RequestParam(value = "cableGlandTypeEnd", required = false) String[] cableGlandTypeEnd, @RequestParam(value = "corrugatedPipeStartLength", required = false, defaultValue = "0") String[] corrugatedPipeStartLength, @RequestParam(value = "corrugatedPipeEndLength", required = false, defaultValue = "0") String[] corrugatedPipeEndLength, @RequestParam(value = "correction", required = false) String correction, @RequestParam(value = "min", required = false) String min, @RequestParam(value = "max", required = false) String max) {
        if (!Shared.isSkipCablesSelected) {
            locationList.clear();
            locationList = cableService.countAccessories(cables, startLocation, cableGlandTypeStart, corrugatedPipeStart, endLocation, corrugatedPipeEnd, cableGlandTypeEnd, corrugatedPipeStartLength, corrugatedPipeEndLength, correction, min, max);
            model.addAttribute("locationList", locationList);
            model.addAttribute("cablesWithLength", sumCables(cables));
        }
        if (Shared.isSkipCablesSelected) {
            locationList.clear();
            locationList = cableService.countAccessories(Shared.cablesFoundInBase, startLocation, cableGlandTypeStart, corrugatedPipeStart, endLocation, corrugatedPipeEnd, cableGlandTypeEnd, corrugatedPipeStartLength, corrugatedPipeEndLength, correction, min, max);
            model.addAttribute("locationList", locationList);
            model.addAttribute("cablesWithLength", sumCables(Shared.cablesFoundInBase));
        }
//        isSkipCablesSelected = false;
        model.addAttribute("cablesWithDesignatedAccessories", cableService.getCablesWithDesignatedAccessories());
        return "show-results";
    }

    //вывести страницу занесения кабеля в БД с заполненными полями для добавляемого кабеля (заполняются поля "тип" и "количество жил", т. к. они есть в файле со списком кабелей для подбора аксессуаров)
    @GetMapping("/start/add-notFoundCable")
    public ModelAndView addNotFoundCable() {
        ModelAndView modelAndView = new ModelAndView();
        ArrayList<String> splitCable = new ArrayList<>();
        if (Shared.notFoundCables != null && Shared.notFoundCables.size() > 0) {
            boolean secondSection = false;
            label = true;//если пользователь попал на страницу добавления кабеля из этого контроллера, метка равна 1
            modelAndView.addObject("notFoundCable", Shared.notFoundCables.size());
            modelAndView.addObject("notFoundCableType", Shared.notFoundCables.get(0).getName().split("\\s")[0]);
            if (!Shared.notFoundCables.get(0).getName().contains("+")) {
                modelAndView.addObject("notFoundCableNumberOfWires", Shared.notFoundCables.get(0).getName().split("\\s")[1].split("х")[0]);
                modelAndView.addObject("notFoundCableSectionOfWire", Shared.notFoundCables.get(0).getName().split("\\s")[1].split("х")[1]);
            } else {
                secondSection = true;
                modelAndView.addObject("secondSection", secondSection);
                modelAndView.addObject("notFoundCableNumberOfWires", Shared.notFoundCables.get(0).getName().split("\\+")[0].split("\\s")[1].split("х")[0]);
                modelAndView.addObject("notFoundCableSectionOfWire", Shared.notFoundCables.get(0).getName().split("\\+")[0].split("\\s")[1].split("х")[1]);
                modelAndView.addObject("notFoundCableNumberOfWiresSecond", Shared.notFoundCables.get(0).getName().split("\\+")[1].split("х")[0]);
                modelAndView.addObject("notFoundCableSectionOfWireSecond", Shared.notFoundCables.get(0).getName().split("\\+")[1].split("х")[1]);
            }
            Shared.uniqueNotFoundCables.remove(Shared.notFoundCables.get(0));
        }

        if (Shared.notFoundCables != null && Shared.notFoundCables.size() == 0 & label) {
            splitCable.clear();
            modelAndView.addObject("notFoundCable", null);
            label = false;
            return showCableList(listCables);
        }

        if (Shared.notFoundCables != null && Shared.notFoundCables.size() == 0 & !label) {
            modelAndView.setViewName("one-cable-add-form");
            return modelAndView;
        }

        if (Shared.notFoundCables != null && Shared.notFoundCables.size() > 0)
            modelAndView.setViewName("one-cable-add-form");

        if (Shared.notFoundCables != null && Shared.notFoundCables.isEmpty() && !Shared.unknownCables.isEmpty()) {
            modelAndView.setViewName("unknown-cable-add-form");
            return modelAndView;
        }

        modelAndView.setViewName("one-cable-add-form");
        return modelAndView;
    }

    @GetMapping("/start/skip-notFoundCable")
    public ModelAndView skipNotFoundCable() {
        ModelAndView modelAndView = new ModelAndView();
        Shared.notFoundCables.clear();
        Shared.isSkipCablesSelected = true;
        if (!Shared.isUnknown) {
            modelAndView.addObject("cables", Shared.cablesFoundInBase);
            ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
            modelAndView.addObject("locations", locations);
            modelAndView.addObject("correction", storedValuesRepository.findByName("correction").getValue());
            modelAndView.addObject("min", storedValuesRepository.findByName("min").getValue());
            modelAndView.addObject("max", storedValuesRepository.findByName("max").getValue());
            modelAndView.addObject("unknownCables", Shared.unknownCables);
            modelAndView.setViewName("show-cables-for-selection-accessories");
        }
        if (!Shared.unknownCables.isEmpty() && Shared.isUnknown) {
            modelAndView.addObject("cableDesignation", Shared.unknownCables.get(0).getDesignation());
            modelAndView.addObject("correction", storedValuesRepository.findByName("correction").getValue());
            modelAndView.addObject("min", storedValuesRepository.findByName("min").getValue());
            modelAndView.addObject("max", storedValuesRepository.findByName("max").getValue());
            modelAndView.addObject("max", storedValuesRepository.findByName("max").getValue());
            modelAndView.setViewName("unknown-cable-add-form");
        }

        return modelAndView;
    }

    public ArrayList<String> sumCables(ArrayList<Cable> cablesList) {

        List<Cable> cablesListCopy = new ArrayList<>();
        cablesWithLength.clear();

        for (int i = 0; i < cablesList.size(); i++) {
            cablesListCopy.add(new Cable(cablesList.get(i).getDesignation(), cablesList.get(i).getName(), cablesList.get(i).getLength()));
        }

        //add list of cables in Set to remove duplicates
        HashSet<Cable> uniqueCables = new HashSet<>(cablesListCopy);

        for (Cable cable : uniqueCables
        ) {
            cable.setLength(0F);
        }

        Float sum;
        for (Cable cable : uniqueCables
        ) {
            for (int i = 0; i < cablesList.size(); i++) {
                if (cablesList.get(i).equals(cable)) {
                    sum = cable.getLength() + cablesList.get(i).getLength();
                    cable.setLength(sum);
                }
            }
        }

        for (Cable cable : uniqueCables
        ) {
            cablesWithLength.add("Кабель " + cable.getName() + " = " + cable.getLength() + " м.");
        }

        return cablesWithLength;
    }

    @GetMapping("/start/add-notFoundCable-show-cableList")
    public ModelAndView saveToBaseShowCables() {
        return showCableList(listCables);
    }

    @GetMapping("/start/path-to-Excel")
    public String pathToExcel() {
        return "export-to-excel";
    }

    @RequestMapping("/excel/file-path")
    @ResponseStatus(value = HttpStatus.OK)
    public String exportToExcel(@RequestParam String pathFile) throws IOException {
        ExcelUtil.writeExcelFile(pathFile, locationList, cablesWithLength, cableService.getCablesWithDesignatedAccessories());
        return "success-excel-export";
    }

    @GetMapping("/start/skip-unknownCable")
    public ModelAndView skipUnknownCable() {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("cables", Shared.cablesFoundInBase);
        ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("correction", storedValuesRepository.findByName("correction").getValue());
        modelAndView.addObject("min", storedValuesRepository.findByName("min").getValue());
        modelAndView.addObject("max", storedValuesRepository.findByName("max").getValue());
        modelAndView.setViewName("show-cables-for-selection-accessories");

        return modelAndView;
    }

}
