package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.CableGland;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandPG;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.CableGlandPgRepository;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;
import org.upmoover.cableAccessoryAssistant.utils.Shared;

import java.util.*;

@Controller
public class MainController {

    String pathFile = null;
    ArrayList<Cable> listCables = new ArrayList<>();

    ArrayList<Cable> cables = new ArrayList<>();

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
        this.pathFile = pathFile;
        listCables.clear();
        cables.clear();
        listCables = CableFileReader.readFile(this.pathFile);
        return showCableList(listCables);
    }

    //считать список кабеля, вывести страницу со списком и выбором аксессуаров
    public ModelAndView showCableList(ArrayList<Cable> listCables) {
        Cable cable;
        ModelAndView modelAndView = new ModelAndView();

        //поиск кабеля из списка в базе: если все кабели присутствуют в БД - они выводится на страницу, если нет - выводится предупреждение и возможность добавить недостающий кабель в БД
        for (int i = 0; i < listCables.size(); i++) {
            if ((cable = cableService.findCableByName(listCables.get(i).getName())) != null) {
                cables.add(listCables.get(i));
                if (Shared.uniqueNotFoundCables.isEmpty()) {
                    cables.get(i).setId(cable.getId());
                    cables.get(i).setCableGlandRgg(cable.getCableGlandRgg());
                    cables.get(i).setCableGlandPg(cable.getCableGlandPg());
                    cables.get(i).setCableGlandMg(cable.getCableGlandMg());
                }
            }
            //добавление в список кабелей, отсутствующих в базе данных
            else {
                Shared.uniqueNotFoundCables.add(listCables.get(i));
            }
        }

        if (!Shared.uniqueNotFoundCables.isEmpty()) cables.clear();

        Shared.notFoundCables = new ArrayList<>(Shared.uniqueNotFoundCables);
        modelAndView.addObject("cables", cables);
        //получение из базы списка местоположений
        ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
        modelAndView.addObject("locations", locations);
        modelAndView.addObject("notFoundCables", Shared.notFoundCables);
        modelAndView.setViewName("show-cables-for-selection-accessories");
        return modelAndView;
    }

    @PostMapping("/start/get-attributes")
    @ResponseStatus(value = HttpStatus.OK)
    public void getCableAttributes(@RequestParam(value = "startLocation", required = false) String[] startLocation, @RequestParam(value = "cableGlandTypeStart", required = false) String[] cableGlandTypeStart, @RequestParam(value = "corrugatedPipeStart", required = false) String[] corrugatedPipeStart, @RequestParam(value = "endLocation", required = false) String[] endLocation, @RequestParam(value = "corrugatedPipeEnd", required = false) String[] corrugatedPipeEnd, @RequestParam(value = "cableGlandTypeEnd", required = false) String[] cableGlandTypeEnd) {

        cableService.countAccessories(cables, startLocation, cableGlandTypeStart, corrugatedPipeStart, endLocation, corrugatedPipeEnd, cableGlandTypeEnd);

    }

    //вывести страницу занесения кабеля в БД с заполненными полями для добавляемого кабеля (заполняются поля "тип" и "количество жил", т. к. они есть в файле со списком кабелей для подбора аксессуаров)
    @GetMapping("/start/add-notFoundCable")
    public ModelAndView addNotFoundCable() {
        ModelAndView modelAndView = new ModelAndView();
        ArrayList<String> splitCable = new ArrayList<>();
        if (Shared.notFoundCables.size() > 0) {
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

        if (Shared.notFoundCables.size() == 0 & label) {
            splitCable.clear();
            modelAndView.addObject("notFoundCable", null);
            label = false;
            return showCableList(listCables);
        }

        if (Shared.notFoundCables.size() == 0 & !label) {
            modelAndView.setViewName("one-cable-add-form");
            return modelAndView;
        }

        if (Shared.notFoundCables.size() > 0)
            modelAndView.setViewName("one-cable-add-form");
        return modelAndView;
    }

    @GetMapping("/start/add-notFoundCable-show-cableList")
    public ModelAndView saveToBaseShowCables() {
//TODO разобраться
        return showCableList(listCables);
    }
}
