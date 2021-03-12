package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
@Table(name = "CorrugatedPipeMetal")
public class CorrugatedPipeMetal extends CorrugatedPipe {

    @Column(name = "outerDiameter")
    private Float outerDiameter;

    public CorrugatedPipeMetal() {
    }

    @OneToOne
    @JoinColumn(name = "CABLEGLANDMBID")
    CableGlandMB cableGlandMB;

    public Float getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(Float outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public CableGlandMB getCableGlandMB() {
        return cableGlandMB;
    }
}
