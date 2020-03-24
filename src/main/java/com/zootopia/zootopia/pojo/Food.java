package com.zootopia.zootopia.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Food {
    private Integer foodId;
    private String foodName;
    private double foodPrice;
    private Boolean veg;
    private int quantity;
}
