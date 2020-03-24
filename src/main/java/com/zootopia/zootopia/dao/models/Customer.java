package com.zootopia.zootopia.dao.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cid;
    private String name;
    private String email;
    private String password;
    private String address;
    private long phoneNo;
    private String role;

    private String sub;

    private String picture;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
    private List<Orderr> orderrList = new ArrayList<>();

    @OneToOne(mappedBy = "customerHaveRestaurant", cascade = CascadeType.ALL)
    private Restaurant restaurants;
}
