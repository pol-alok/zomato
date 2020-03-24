package com.zootopia.zootopia.controller;

import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.dao.models.Orderr;
import com.zootopia.zootopia.dao.models.Restaurant;
import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.services.CustomerService;
import com.zootopia.zootopia.services.OrderService;
import com.zootopia.zootopia.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RestaurantService restaurantService;


    @PostMapping("/order/{restaurantID}")
    public String placeOrder(@PathVariable("restaurantID")int restaurantID,
                             @RequestParam("id") String id,
                             @RequestParam("name") String name,
                             @RequestParam("price") String price,
                             @RequestParam("noOfItem") String noOfItem,
                             @RequestParam("total") String total,
                             @RequestParam("type") String vegString,Principal principal) {

        System.out.println(price);

        List<Food> foodList = new ArrayList<>();


        String veglist[] = vegString.split(",");
        boolean vegList[] = new boolean[veglist.length];
        for (int i = 0; i < vegList.length; i++) {
            if (veglist[i].equals("true"))
                vegList[i] = true;
            else
                vegList[i] = false;
        }

        String foodIDList[] = id.split(",");
        String nameList[] = name.split(",");
        String priceList[] = price.split(",");
        String noOfItemList[] = noOfItem.split(",");
        String totalList[] = total.split(",");


        for (int i = 0; i < foodIDList.length; i++) {
            Food food = new Food();
            food.setFoodId(Integer.parseInt(foodIDList[i]));
            food.setFoodName(nameList[i]);
            food.setVeg(vegList[i]);
            food.setFoodPrice(Double.parseDouble(totalList[i]));
            food.setQuantity(Integer.parseInt(noOfItemList[i]));
            // food.setSubPrice(Double.parseDouble(priceList[i]));
            foodList.add(food);

        }

        String mail  = principal.getName();

        Customer customer = customerService.getByEmail(mail);
        Restaurant restaurant = restaurantService.get(restaurantID);

        Orderr orderr = new Orderr();
        orderr.setCustomer(customer);
        orderr.setRestaurant(restaurant);


        double bill = 0;
        for (String s : totalList) {
            bill += Double.parseDouble(s);

        }

        orderr.setBill(bill);
        orderr.setFoodItemList(foodList);
        orderService.save(orderr);


    return "redirect:/payment2?"+customer.getCid();
    }

    @GetMapping("/orderList")
    void orderList() {
        for (Orderr order : orderService.getList()) {
            System.out.print(order.getOrderId() + "  ");
            double bill = 0;
            for (Food food : order.getFoodItemList()) {
                System.out.print(food.getFoodId() + " " + food.getFoodName() + " " + food.getFoodPrice() + "  ");
                bill = bill + food.getFoodPrice();
            }
            order.setBill(bill);
            // System.out.print(bill);
            System.out.println();
        }

    }

    @GetMapping("/deleteOrder/{id}")
    void deleteOrder(@PathVariable("id") long orderId) {
        orderService.delete(orderId);
    }

}
