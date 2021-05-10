package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
@Table(name = "cables")
//класс для кабельной продукции
public class Cable implements Cloneable {
    //id кабеля
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //наименование кабеля
    @Column(name = "name")
    private String name;
    //наружный диаметр кабеля
    @Column(name = "outerDiameter")
    private Float outerDiameter;
    //код производителя (артикул)
    @Column(name = "vendorCode")
    private String vendorCode;
    //вес кабеля (кг/км)
    @Column(name = "weight")
    private Float weight;
    //имя кабеля в проекте (например, -W1, -W2 и т. д.)
    @Column(name = "designation")
    private String designation;
    @Transient
    private Float length;
    @Transient
    private boolean isSelectedForDelete;
    @Transient
    private String startLocation;
    @Transient
    private String cableGlandTypeStart;
    @Transient
    private CorrugatedPipe corrugatedPipeStart;
    @Transient
    private String endLocation;
    @Transient
    private CorrugatedPipe corrugatedPipeEnd;
    @Transient
    private String cableGlandTypeEnd;
    @Transient
    private Float corrugatedPipeStartLength;
    @Transient
    private Float corrugatedPipeEndLength;
    @Transient
    private CableGland selectedCableGlandStart;
    @Transient
    private CableGland selectedCableGlandEnd;
    @Transient
    private Float outerDiameterFromBase;

    //--------

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getCableGlandTypeStart() {
        return cableGlandTypeStart;
    }

    public void setCableGlandTypeStart(String cableGlandTypeStart) {
        this.cableGlandTypeStart = cableGlandTypeStart;
    }

    public CorrugatedPipe getCorrugatedPipeStart() {
        return corrugatedPipeStart;
    }

    public void setCorrugatedPipeStart(CorrugatedPipe corrugatedPipeStart) {
        this.corrugatedPipeStart = corrugatedPipeStart;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public CorrugatedPipe getCorrugatedPipeEnd() {
        return corrugatedPipeEnd;
    }

    public void setCorrugatedPipeEnd(CorrugatedPipe corrugatedPipeEnd) {
        this.corrugatedPipeEnd = corrugatedPipeEnd;
    }

    public String getCableGlandTypeEnd() {
        return cableGlandTypeEnd;
    }

    public void setCableGlandTypeEnd(String cableGlandTypeEnd) {
        this.cableGlandTypeEnd = cableGlandTypeEnd;
    }

    public Float getCorrugatedPipeStartLength() {
        return corrugatedPipeStartLength;
    }

    public void setCorrugatedPipeStartLength(Float corrugatedPipeStartLength) {
        this.corrugatedPipeStartLength = corrugatedPipeStartLength;
    }

    public Float getCorrugatedPipeEndLength() {
        return corrugatedPipeEndLength;
    }

    public void setCorrugatedPipeEndLength(Float corrugatedPipeEndLength) {
        this.corrugatedPipeEndLength = corrugatedPipeEndLength;
    }

    public CableGland getSelectedCableGlandStart() {
        return selectedCableGlandStart;
    }

    public void setSelectedCableGlandStart(CableGland selectedCableGlandStart) {
        this.selectedCableGlandStart = selectedCableGlandStart;
    }

    public CableGland getSelectedCableGlandEnd() {
        return selectedCableGlandEnd;
    }

    public void setSelectedCableGlandEnd(CableGland selectedCableGlandEnd) {
        this.selectedCableGlandEnd = selectedCableGlandEnd;
    }

    //--------

    public boolean isSelectedForDelete() {
        return isSelectedForDelete;
    }

    public void setSelectedForDelete(boolean selectedForDelete) {
        isSelectedForDelete = selectedForDelete;
    }

    //поле для связи с таблицей кабельных вводов (cableGland)
    @ManyToOne()
    @JoinColumn(name = "cableGlandPgId")
    private CableGlandPG cableGlandPg;

    @ManyToOne()
    @JoinColumn(name = "cableGlandMgId")
    private CableGlandMG cableGlandMg;

    @ManyToOne()
    @JoinColumn(name = "cableGlandRggId")
    private CableGlandRgg cableGlandRgg;

    @ManyToOne()
    @JoinColumn(name = "corrugatedPipePlasticId")
    private CorrugatedPipePlastic corrugatedPipePlastic;

    @ManyToOne()
    @JoinColumn(name = "corrugatedPipeMetalId")
    private CorrugatedPipeMetal corrugatedPipeMetal;

    public CableGlandRgg getCableGlandRgg() {
        return cableGlandRgg;
    }

    public void setCableGlandRgg(CableGlandRgg cableGlandRgg) {
        this.cableGlandRgg = cableGlandRgg;
    }

    public CableGlandMG getCableGlandMg() {
        return cableGlandMg;
    }

    public void setCableGlandMg(CableGlandMG cableGlandMg) {
        this.cableGlandMg = cableGlandMg;
    }

    public CableGlandPG getCableGlandPg() {
        return cableGlandPg;
    }

    public void setCableGlandPg(CableGlandPG cableGland) {
        this.cableGlandPg = cableGland;
    }

    public CorrugatedPipePlastic getCorrugatedPipePlastic() {
        return corrugatedPipePlastic;
    }

    public void setCorrugatedPipePlastic(CorrugatedPipePlastic corrugatedPipePlastic) {
        this.corrugatedPipePlastic = corrugatedPipePlastic;
    }

    public CorrugatedPipeMetal getCorrugatedPipeMetal() {
        return corrugatedPipeMetal;
    }

    public void setCorrugatedPipeMetal(CorrugatedPipeMetal corrugatedPipeMetal) {
        this.corrugatedPipeMetal = corrugatedPipeMetal;
    }

    public Float getOuterDiameterFromBase() {
        return outerDiameterFromBase;
    }

    public void setOuterDiameterFromBase(Float outerDiameterFromBase) {
        this.outerDiameterFromBase = outerDiameterFromBase;
    }

    public Cable() {
    }

    public Cable(String name, Float outerDiameter, Float weight) {
        this.name = name;
        this.outerDiameter = outerDiameter;
        this.weight = weight;
    }

    public Cable(String designation, String name, Float length) {
        this.designation = designation;
        this.name = name;
        this.length = length;
    }

    public Cable(String designation, String name, Float length, String startLocation, String endLocation) {
        this.name = name;
        this.designation = designation;
        this.length = length;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public Cable(String designation, String name, Float length, String startLocation, String endLocation, Float corrugatedPipeStartLength, Float corrugatedPipeEndLength, CorrugatedPipe corrugatedPipeStart, CorrugatedPipe corrugatedPipeEnd) {
        this.name = name;
        this.designation = designation;
        this.length = length;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.corrugatedPipeStartLength = corrugatedPipeStartLength;
        this.corrugatedPipeEndLength = corrugatedPipeEndLength;
        this.corrugatedPipeStart = corrugatedPipeStart;
        this.corrugatedPipeEnd = corrugatedPipeEnd;
    }

    public Cable(String name, Float outerDiameter, String designation, Float length, String startLocation, String cableGlandTypeStart, String endLocation, String cableGlandTypeEnd, CableGlandPG cableGlandPg, CableGlandMG cableGlandMg, CableGlandRgg cableGlandRgg, CorrugatedPipePlastic corrugatedPipePlastic, CorrugatedPipeMetal corrugatedPipeMetal, CorrugatedPipe corrugatedPipeStart, CorrugatedPipe corrugatedPipeEnd, Float outerDiameterFromBase) {
        this.name = name;
        this.outerDiameter = outerDiameter;
        this.designation = designation;
        this.length = length;
        this.startLocation = startLocation;
        this.cableGlandTypeStart = cableGlandTypeStart;
        this.endLocation = endLocation;
        this.cableGlandTypeEnd = cableGlandTypeEnd;
        this.cableGlandPg = cableGlandPg;
        this.cableGlandMg = cableGlandMg;
        this.cableGlandRgg = cableGlandRgg;
        this.corrugatedPipePlastic = corrugatedPipePlastic;
        this.corrugatedPipeMetal = corrugatedPipeMetal;
        this.corrugatedPipeStart = corrugatedPipeStart;
        this.corrugatedPipeEnd = corrugatedPipeEnd;
        this.outerDiameterFromBase = outerDiameterFromBase;
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

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
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

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "Cable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", outerDiameter=" + outerDiameter +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int hashCode() {
        char[] charCableName = this.getName().toCharArray();
        int sum = 0;
        for (int i = 0; i < charCableName.length; i++) {
            sum = sum + charCableName[i] + i;
        }
        return sum;
    }

    @Override
    public boolean equals(Object obj) {
        Cable cable = (Cable) obj;
        return cable.getName().equalsIgnoreCase(name);
    }
}
