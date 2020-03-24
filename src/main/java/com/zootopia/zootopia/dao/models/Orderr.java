package com.zootopia.zootopia.dao.models;


import com.zootopia.zootopia.pojo.Food;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity

public class Orderr {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long orderId;


    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Restaurant restaurant;

     private String status;
    @CreationTimestamp
    @Column(nullable = false,updatable = false)
    private Date orderDate;

    @ElementCollection
    private List<Food> foodItemList=new ArrayList<>();

    public Orderr() {
        this.status = "pending";
    }


    private Double bill;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Food> getFoodItemList() {
        return foodItemList;
    }

    public void setFoodItemList(List<Food> foodItemList) {
        this.foodItemList = foodItemList;
    }



}
