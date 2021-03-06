package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

//класс-родитель для кабельного ввода (гермоввод, сальник)
@MappedSuperclass
public class CableGland {
    //id кабельного ввода
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //наименование кабельного ввода
    @Column(name = "name")
    private String name;
    //максимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "maxDiameter")
    private Float maxDiameter;
    //код производителя (артикул)
    @Column(name = "vendorCode")
    private String vendorCode;

    public CableGland() {
    }

    public CableGland(String name, Float maxDiameter, String vendorCode) {
        this.name = name;
        this.maxDiameter = maxDiameter;
        this.vendorCode = vendorCode;
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

    public Float getMaxDiameter() {
        return maxDiameter;
    }

    public void setMaxDiameter(Float maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
