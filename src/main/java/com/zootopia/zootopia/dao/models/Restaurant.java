package com.zootopia.zootopia.dao.models;

import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.pojo.Location;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Restaurant {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String tagLine;
    private String phoneNo;
    private String city;
     private String email;
    private int minPrice;
    private String payments;

    private Location location;

     private  String address;

    @OneToMany(mappedBy = "restaurant",cascade=CascadeType.REMOVE)
    private List<Orderr> orderrList =new ArrayList<>();

    @OneToOne
    private Customer customerHaveRestaurant;

    @ElementCollection
    private List<Food> foods = new ArrayList<>();

    private  String picture;

}