package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
@Table(name = "cable")
//класс для кабельной продукции
public class Cable {
    //id кабеля
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //наименование кабеля
    @Column(name = "name")
    private String name;
    //наружный диаметр кабеля
    @Column(name = "outer_diameter")
    private Float outerDiameter;
    //код производителя (артикул)
    @Column(name = "vendor_code")
    private Long vendorCode;
    //вес кабеля (кг/км)
    @Column(name = "weight")
    private Float weight;
    //имя кабеля в проекте (например, -W1, -W2 и т. д.)
    @Column(name = "designation")
    private String designation;

    public Cable() {
    }

    public Cable(String name, Float outerDiameter, Float weight) {
        this.name = name;
        this.outerDiameter = outerDiameter;
        this.weight = weight;
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

    public Float getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Float outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public Long getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Long vendorCode) {
        this.vendorCode = vendorCode;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
