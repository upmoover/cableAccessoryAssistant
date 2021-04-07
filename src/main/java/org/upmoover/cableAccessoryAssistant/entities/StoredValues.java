package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
@Table(name = "STOREDVALUES")
public class StoredValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column
    private Float value;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public StoredValues() {
    }

    public StoredValues(String name) {
        this.name = name;
    }

    public StoredValues(String name, Float value) {
        this.name = name;
        this.value = value;
    }
}
