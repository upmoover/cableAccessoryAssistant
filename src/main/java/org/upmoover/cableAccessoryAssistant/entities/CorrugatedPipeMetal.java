package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CorrugatedPipeMetal")
public class CorrugatedPipeMetal extends CorrugatedPipe {

    @Column(name = "outerDiameter")
    private Float outerDiameter;

    public CorrugatedPipeMetal() {
    }

    public Float getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Float outerDiameter) {
        this.outerDiameter = outerDiameter;
    }
}
