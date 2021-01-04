package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.upmoover.cableAccessoryAssistant.entities.Cable;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;
import org.upmoover.cableAccessoryAssistant.services.CableService;
import org.upmoover.cableAccessoryAssistant.utils.CableFileReader;

import javax.xml.transform.sax.SAXResult;
import java.util.ArrayList;

@Controller
public class MainController {

    ArrayList<Cable> cables = new ArrayList<>();
    ArrayList<Cable> notFoundCables = new ArrayList<>();

    CableService cableService;

    @Autowired
    public void setCableService(CableService cableService) {
        this.cableService = cableService;
    }

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
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

    //считать список кабеля, вывести страницу со списком и выбором аксессуаров
    @GetMapping("/start/show-cables")
    public String showCableList(@RequestParam String pathFile, Model model) {
        //получение списка кабелей для подбора аксессуаров из файла

        ArrayList<Cable> listCables = new ArrayList<>();
        listCables.clear();
        notFoundCables.clear();
        Cable cable = null;
        cables.clear();
        listCables = CableFileReader.readFile(pathFile);
        //поиск кабеля из списка в базе: если кабель присутствует в БД - он выводится на страницу, если нет - выводится предупреждение и возможность добавить недостающий кабель в БД
        for (int i = 0; i < listCables.size(); i++) {
            if ((cable = cableService.findCableByName(listCables.get(i).getName())) != null) {
                cables.add(cable);
                System.out.println(cable.getId() + " " + cable.getName());
            }
            //добавление в список кабелей, отсутствующих в базе данных
            else notFoundCables.add(listCables.get(i));
        }

        System.out.println("Не найденные в базе кабели ===============");
        for (int i = 0; i < notFoundCables.size(); i++) {
            System.out.println(notFoundCables.get(i).getName());
        }

        model.addAttribute("cables", cables);
        //получение из базы списка местоположений
        ArrayList<Location> locations = (ArrayList<Location>) locationsRepository.findAll();
        model.addAttribute("locations", locations);
        model.addAttribute("notFoundCables", notFoundCables);
        return "show-cables-for-selection-accessories";
    }

    @PostMapping("/start/get-attributes")
    @ResponseStatus(value = HttpStatus.OK)
    public void getCableAttributes(@RequestParam(value = "cableGlandType", required = false) String[] cableGlandType, @RequestParam(value = "startLocation", required = false) String[] startLocations) {
        for (int i = 0; i < cableGlandType.length; i++) {
            System.out.println("name: " + cableGlandType[i].split("=")[0] + ", cableGland: " + cableGlandType[i].split("=")[1] + "location: " + startLocations[i].split("=")[0]);
        }
    }

    //вывести страницу занесения кабеля в БД с заполненными полями для добавляемого кабеля (заполняются поля "тип" и "количество жил", т. к. они есть в файле со списком кабелей для подбора аксессуаров)
    @GetMapping("/start/add-notFoundCable")
    public String addNotFoundCable(Model model) {
        ArrayList<String> splitCable = new ArrayList<>();
        if (!notFoundCables.isEmpty()) {
            model.addAttribute("notFoundCable", notFoundCables.get(0));
            String s1 = notFoundCables.get(0).getName().split("\\s")[0];
            String s2 = notFoundCables.get(0).getName().split("\\s")[1].split("х")[0];
            String s3 = notFoundCables.get(0).getName().split("\\s")[1].split("х")[1];
            model.addAttribute("notFoundCableType", s1);
            model.addAttribute("notFoundCableNumberOfWires", s2);
            model.addAttribute("notFoundCableSectionOfWire", s3);
            notFoundCables.remove(0);
        } else {
            splitCable.clear();
            model.addAttribute("notFoundCable", null);
            return "show-cables-for-selection-accessories";
        }
        return "one-cable-add-form";
    }
}
