package com.zootopia.zootopia.dao.models;


import com.zootopia.zootopia.pojo.Food;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class TotalOrder
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
     private Long totalOrderId;

    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long orderId;
      private String restaurantName;
    @ElementCollection
    private List<Food> foodList=new ArrayList<>();

    private double totalBill;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public Long getTotalOrderId() {
        return totalOrderId;
    }

    public void setTotalOrderId(Long totalOrderId) {
        this.totalOrderId = totalOrderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}