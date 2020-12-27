package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CableGlandRgg")
//класс для кабельного ввода (гермоввод, сальник)
public class CableGlandRgg extends CableGland {

    @OneToMany(mappedBy = "cableGlandRgg")
    List<Cable> cables;

    @OneToOne()
    @JoinColumn(name = "corrugatedPipeId")
    CorrugatedPipe corrugatedPipe;

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

    public CorrugatedPipe getCorrugatedPipe() {
        return corrugatedPipe;
    }

    public void setCorrugatedPipe(CorrugatedPipe corrugatedPipe) {
        this.corrugatedPipe = corrugatedPipe;
    }
}
