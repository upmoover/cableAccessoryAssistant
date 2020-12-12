package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
public class СorrugatedPipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "inner_diameter")
    private Float innerDiameter;
    @Column(name = "vendor_code")
    private String vendorCode;
}
