package com.zootopia.zootopia.controller;


import com.zootopia.zootopia.dao.models.Orderr;
import com.zootopia.zootopia.dao.models.Places;
import com.zootopia.zootopia.dao.models.Restaurant;
import com.zootopia.zootopia.dao.models.TotalOrder;
import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.pojo.Location;
import com.zootopia.zootopia.services.OrderService;
import com.zootopia.zootopia.services.PlacesServices;
import com.zootopia.zootopia.services.RestaurantService;
import com.zootopia.zootopia.services.TotalOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private TotalOrderService totalOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlacesServices placesServices;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/totalOrder")
    public void totalOrder() {
        double Total = 0;
        for (TotalOrder totalOrder : totalOrderService.getAll()) {
            System.out.print(totalOrder.getOrderId() + "  " + totalOrder.getCustomerId() + " " + totalOrder.getCustomerName() + " " + totalOrder.getCustomerEmail() + "  ");
            for (Food food : totalOrder.getFoodList()) {

                System.out.print(food.getFoodId() + " " + food.getFoodName() + " " + food.getFoodPrice() + " " + food.getVeg() + " ");
            }
            System.out.println(totalOrder.getOrderId());
            System.out.println();
        }

    }

    @GetMapping("/insertPlaces")
    public String setPlaces(@RequestParam("name") String name, @RequestParam("lat") Double lat,@RequestParam("lng") Double lng) {
        Places places = new Places();
        places.setName(name);
        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        places.setLocation(location);
        placesServices.save(places);
        return "redirect:/showRestaurant";
    }

    @GetMapping("/dummyResto")
    public String setRest(@RequestParam("address") String address,
                          @RequestParam("city") String city,
                          @RequestParam("email") String email,
                          @RequestParam("lat") Double lat,
                          @RequestParam("lng") Double lng,
                          @RequestParam("min_price") Integer min_price,
                          @RequestParam("name") String name,
                          @RequestParam("payments") String payments,
                          @RequestParam("phoneNo") String phoneNo,
                          @RequestParam("tagLine") String tagLine,
                          @RequestParam("pic") String pic){

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setCity(city);
        restaurant.setEmail(email);
        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        restaurant.setLocation(location);
        restaurant.setMinPrice(min_price);
        restaurant.setName(name);
        restaurant.setPayments(payments);
        restaurant.setPhoneNo(phoneNo);
        restaurant.setTagLine(tagLine);
        restaurant.setPicture(pic);

        restaurantService.save(restaurant);
        return "redirect:/showRestaurant";
    }

    @GetMapping("/totalBill")
    void totalBill() {
        double bill = 0;

        List<TotalOrder> totalOrder1 = totalOrderService.getAll();

        System.out.println(totalOrder1.get(1).getCustomerName());

        for (TotalOrder totalOrder : totalOrderService.getAll()) {
            bill = bill + totalOrder.getTotalBill();
        }

        System.out.println("GRAND TOTAL : " + bill);

    }

    @GetMapping("/orderConfirm/{id}")
    public String orderConfirm(@PathVariable("id") long orderId) {
        System.out.println("JKJKJK");
        Orderr order = orderService.get(orderId);
        order.setStatus("accepted");
        TotalOrder totalOrder = new TotalOrder();
        totalOrder.setRestaurantName(order.getRestaurant().getName());
        totalOrder.setOrderId(order.getOrderId());
        List<Food> foodList = new ArrayList<>();
        for (Food food : order.getFoodItemList()) {
            foodList.add(food);
        }
        totalOrder.setCustomerId(order.getCustomer().getCid());
        totalOrder.setCustomerName(order.getCustomer().getName());
        totalOrder.setCustomerEmail(order.getCustomer().getEmail());
        totalOrder.setFoodList(foodList);
        System.out.println(order.getBill());
        totalOrder.setTotalBill(order.getBill());
        totalOrderService.save(totalOrder);
        return "redirect:/restaurantDashBoard/"+order.getRestaurant().getId();
    }


    @GetMapping("/orderDecline/{id}")
    public String orderDecline(@PathVariable("id") long orderId) {
        System.out.println("Decline");
        Orderr order = orderService.get(orderId);
        order.setStatus("rejected");
        orderService.save(order);
        System.out.println(order.getStatus());
        return "redirect:/restaurantDashBoard/"+order.getRestaurant().getId();
    }

}
