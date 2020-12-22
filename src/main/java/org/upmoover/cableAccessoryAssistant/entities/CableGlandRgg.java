package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CableGlandRgg")
//класс для кабельного ввода (гермоввод, сальник)
public class CableGlandRgg extends CableGland {

    @OneToMany(mappedBy = "cableGlandRgg")
    List<Cable> cables;

    public List<Cable> getCables() {
        return cables;
    }

    public void setCables(List<Cable> cables) {
        this.cables = cables;
    }

    public CableGlandRgg() {
    }

    public CableGlandRgg(String name, Float maxDiameter, String vendorCode) {
        super(name, maxDiameter, vendorCode);
    }

}
