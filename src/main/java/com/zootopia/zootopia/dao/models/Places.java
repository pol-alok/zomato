package com.zootopia.zootopia.dao.models;

import com.zootopia.zootopia.pojo.Location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Setter
@Getter
public class Places {

    @Id
    private String name;
    private Location location;
}
