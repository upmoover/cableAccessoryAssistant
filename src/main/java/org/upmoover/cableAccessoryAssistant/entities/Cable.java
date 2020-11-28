package org.upmoover.cableAccessoryAssistant.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cable")
//класс для кабельной продукции
public class Cable {
    //id кабеля
    @Id
    @Column(name = "id")
    private Long id;
    //наименование кабеля
    @Column(name = "name")
    private String name;
    //наружный диаметр кабеля
    @Column(name = "outer_diameter")
    private Long outerDiameter;
    //код производителя (артикул)
    @Column(name = "vendor_code")
    private Long vendorCode;
    //вес кабеля (кг/м)
    @Column(name = "weight")
    private Long weight;
    //имя кабеля в проекте (например, -W1, -W2 и т. д.)
    @Column(name = "designation")
    private String designation;

    public Cable() {
    }


}
