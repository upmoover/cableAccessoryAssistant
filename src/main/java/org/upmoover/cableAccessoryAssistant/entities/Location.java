package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "Locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //наименование местоположения
    @Column(name = "name")
    private String name;

    @Transient
    private ArrayList<Object> glandsList;

    @Transient
    private Map<CorrugatedPipe, Float> corrugatedPipeList;

    public Map<CorrugatedPipe, Float> getCorrugatedPipeList() {
        return corrugatedPipeList;
    }

    public Location() {
        this.glandsList = new ArrayList<>();
        this.corrugatedPipeList = new HashMap<>();
    }

    public void setGlandsList(ArrayList<Object> glandsList) {
        this.glandsList = glandsList;
    }

    public ArrayList<Object> getGlandsList() {
        return glandsList;
    }

    public Location(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
