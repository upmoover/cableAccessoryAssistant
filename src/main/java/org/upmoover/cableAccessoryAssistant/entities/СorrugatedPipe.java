package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.*;

@Entity
public class СorrugatedPipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "innerDiameter")
    private Float innerDiameter;
    @Column(name = "vendorCode")
    private String vendorCode;
}
