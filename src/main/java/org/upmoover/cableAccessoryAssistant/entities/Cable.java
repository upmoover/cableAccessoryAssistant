package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
@Table(name = "cables")
//класс для кабельной продукции
public class Cable {
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
    @JoinColumn(name = "corrugatedPipeId")
    private CorrugatedPipe corrugatedPipe;

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

    public CorrugatedPipe getCorrugatedPipe() {
        return corrugatedPipe;
    }

    public void setCorrugatedPipe(CorrugatedPipe corrugatedPipe) {
        this.corrugatedPipe = corrugatedPipe;
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
}
