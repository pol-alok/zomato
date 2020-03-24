package com.zootopia.zootopia.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private double lat;
    private double lng;
}
