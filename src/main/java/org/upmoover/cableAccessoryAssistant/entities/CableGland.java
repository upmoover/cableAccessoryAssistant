package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cableGland")
//класс для кабельного ввода (гермоввод, сальник)
public class CableGland {
    //id кабельного ввода
    @Id
    @Column(name = "id")
    private Long id;
    //наименование кабельного ввода
    @Column(name = "name")
    private String name;
    //минимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "min_diameter")
    private Long minDiameter;
    //максимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "max_diameter")
    private Long maxDiameter;
    //код производителя (артикул)
    @Column(name = "vendor_code")
    private Long vendorCode;

    public CableGland() {
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

    public Long getMinDiameter() {
        return minDiameter;
    }

    public void setMinDiameter(Long minDiameter) {
        this.minDiameter = minDiameter;
    }

    public Long getMaxDiameter() {
        return maxDiameter;
    }

    public void setMaxDiameter(Long maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public Long getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(Long vendorCode) {
        this.vendorCode = vendorCode;
    }
}
