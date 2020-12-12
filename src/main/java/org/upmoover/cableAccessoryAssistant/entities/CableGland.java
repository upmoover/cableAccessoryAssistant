package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cableGlands")
//класс для кабельного ввода (гермоввод, сальник)
public class CableGland {
    //id кабельного ввода
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //наименование кабельного ввода
    @Column(name = "name")
    private String name;
    //минимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "minDiameter")
    private Long minDiameter;
    //максимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "maxDiameter")
    private Long maxDiameter;
    //код производителя (артикул)
    @Column(name = "vendorCode")
    private String vendorCode;
    @OneToMany()
    List<Cable> cables;

    public List<Cable> getCables() {
        return cables;
    }

    public void setCables(List<Cable> cables) {
        this.cables = cables;
    }

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

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
