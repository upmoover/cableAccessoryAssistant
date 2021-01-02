package org.upmoover.cableAccessoryAssistant.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.upmoover.cableAccessoryAssistant.entities.CableGlandMG;
import org.upmoover.cableAccessoryAssistant.entities.Location;
import org.upmoover.cableAccessoryAssistant.repositories.LocationsRepository;

import java.util.ArrayList;

@Controller
@RequestMapping("/database/locations")
public class LocationsController {

    LocationsRepository locationsRepository;

    @Autowired
    public void setLocationsRepository(LocationsRepository locationsRepository) {
        this.locationsRepository = locationsRepository;
    }

    //отобразить страницу редактирования местоположений
    @RequestMapping("")
    public String showFormEditLocation() {
        return "edit-locations-database";
    }

    //отобразить страницу добавления в БД местоположений
    @RequestMapping("/add-form")
    public String showAddLocation() {
        return "one-location-add-form";
    }

    //добавить в БД местоположение, отобразить страницу добавления в БД местоположения
    @RequestMapping("/add")
    public String addCableLocation(@RequestParam String name) {
        locationsRepository.save(new Location(name));
        return "redirect:/database/locations/show-all-from-base";
    }

    //отобразить страницу с полным списком местоположений из БД
    @RequestMapping("/show-all-from-base")
    public String editCableLocation(Model model) {
        ArrayList<Location> locations;
        locations = (ArrayList<Location>) locationsRepository.findAll();
        model.addAttribute("locations", locations);
        return "show-all-locations-from-base";
    }

    //удалить из базы выбранное месторасположение и вернуть обратно на страницу с месторасположениями
    @RequestMapping("/delete-location-by-id/{id}")
    public String deleteCableLocation(@PathVariable("id") Long id) {
        locationsRepository.deleteById(id);
        return "redirect:/database/locations/show-all-from-base";
    }
}
