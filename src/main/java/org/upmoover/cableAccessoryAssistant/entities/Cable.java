package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cable")
//класс для кабельной продукции
public class Cable {
    //id кабеля
    @Id
    @Column(name = "id")
    private Long id;
    //наименование кабеля
    @Column(name = "name")
    private String name;
    //наружный диаметр кабеля
    @Column(name = "outer_diameter")
    private Long outerDiameter;
    //код производителя (артикул)
    @Column(name = "vendor_code")
    private Long vendorCode;
    //вес кабеля (кг/м)
    @Column(name = "weight")
    private Long weight;
    //имя кабеля в проекте (например, -W1, -W2 и т. д.)
    @Column(name = "designation")
    private String designation;

    public Cable() {
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

    public Long getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Long outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public Long getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Long vendorCode) {
        this.vendorCode = vendorCode;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
