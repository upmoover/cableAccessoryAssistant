package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

//класс-родитель для трубы гофрированной
@MappedSuperclass
public class CorrugatedPipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "innerDiameter")
    private Float innerDiameter;
    @Column(name = "vendorCode")
    private String vendorCode;
    @Column(name = "name")
    private String name;

    @Transient
    private Float length;

    public CorrugatedPipe() {
    }

    public CorrugatedPipe(String name, Float innerDiameter, String vendorCode) {
        this.name = name;
        this.innerDiameter = innerDiameter;
        this.vendorCode = vendorCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getInnerDiameter() {
        return innerDiameter;
    }

    public void setInnerDiameter(Float innerDiameter) {
        this.innerDiameter = innerDiameter;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }
}
