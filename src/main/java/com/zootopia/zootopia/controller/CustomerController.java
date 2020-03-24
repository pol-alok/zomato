package com.zootopia.zootopia.controller;


import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.dao.models.Orderr;
import com.zootopia.zootopia.dao.models.Restaurant;
import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.services.CustomerService;
import com.zootopia.zootopia.services.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/addCustomer")
    public void addCustomer() {
        Customer customer = new Customer();
        customer.setName("Indresh");
        customer.setEmail("idr@gmail.com");
        customer.setAddress("Pandepur Varanasi");
        customer.setPhoneNo(2345674589l);
        customer.setRole("customer");
        customerService.save(customer);


    }

    @RequestMapping("customer")
    @ResponseBody
    public Principal author(Principal principal) {
        return principal;
    }

//    @GetMapping("/login")
//    public String getLoginPage(Principal principal) {
//        if (principal != null) {
//            return "redirect:/showRestaurant";
//        } else {
//            return "loginForm";
//        }
//    }


    @GetMapping("/customerDashBoard/{id}")
    public String customerDetails(@PathVariable("id") long id, Model model) {
        Customer customer = customerService.get(id);
        Collections.reverse(customer.getOrderrList());
        model.addAttribute("customer", customer);
        return "customerDashBoard";
    }

    @GetMapping("/deleteCustomer/{id}")
    void deleteCustomer(@PathVariable("id") long id) {
        customerService.delete(id); //delete by Id

    }


    @GetMapping("/cancelOrder/{id}")
    public String cancelOrder(@PathVariable("id") long orderID,Principal principal) {
        Orderr order = orderService.get(orderID);
        order.setStatus("cancel");
        orderService.save(order);
        return "redirect:/customerDashBoard/"+customerService.getBySub(principal.getName()).getCid();
    }

    @GetMapping("/customerProfile/{id}")
    public String customerInfo(@PathVariable("id")long customerID,Model model)
    {
        Customer customer=customerService.get(customerID);
        model.addAttribute("customer",customer);
        System.out.println("JKJKJ");
        return "customerInfo";
    }

    @PostMapping("/updateCustomerInfo")
    public String updateCXInfo(@ModelAttribute("customer") Customer newCustomer,Principal principal) {

//        System.out.println( newCustomer.getName());
        System.out.println(newCustomer.getCid()+"\n"+newCustomer.getName()+"\n"+newCustomer.getRole()+"\n"+newCustomer.getRestaurants()+"\n"+newCustomer.getOrderrList()+"\n"+newCustomer.getPhoneNo()+"\n"+newCustomer.getEmail()+"\n"+newCustomer.getPicture()+"\n");
        Customer currentCustomer = customerService.getBySub(principal.getName());
        newCustomer.setRestaurants(currentCustomer.getRestaurants());
        newCustomer.setOrderrList(currentCustomer.getOrderrList());

        customerService.save(newCustomer);

        return "redirect:/customerDashBoard/"+newCustomer.getCid();
    }



}

