package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cableGlandPG")
//класс для кабельного ввода (гермоввод, сальник)
public class CableGlandPG extends CableGland {
    //минимальный диаметр кабеля, который можно поместить в кабельный ввод
    @Column(name = "minDiameter")
    private Float minDiameter;
    //максимальный диаметр кабеля, который можно поместить в кабельный ввод
    @OneToMany(mappedBy = "cableGlandPg")
    List<Cable> cables;

    public List<Cable> getCables() {
        return cables;
    }

    public void setCables(List<Cable> cables) {
        this.cables = cables;
    }

    public CableGlandPG() {
    }

    public CableGlandPG(String name, Float maxDiameter, String vendorCode, Float minDiameter) {
        super(name, maxDiameter, vendorCode);
        this.minDiameter = minDiameter;
    }

    public Float getMinDiameter() {
        return minDiameter;
    }

    public void setMinDiameter(Float minDiameter) {
        this.minDiameter = minDiameter;
    }
}
