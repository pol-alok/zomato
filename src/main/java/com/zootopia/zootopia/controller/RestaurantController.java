package com.zootopia.zootopia.controller;

import com.zootopia.zootopia.dao.models.*;
import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.pojo.Location;
import com.zootopia.zootopia.services.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class RestaurantController {

    @Autowired

    private RestaurantService restaurantService;
    @Autowired
    private TotalOrderService totalOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PlacesServices placesServices;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public  String redirectToHomePage() {
        return "redirect:/showRestaurant/";
    }

    @GetMapping("/newRestaurant")
    public String newRestaurantForm(Model model,Principal principal) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("customer",customerService.getBySub(principal.getName()));
        return "addRestaurantForm";
    }

    @PostMapping("/newRestaurant")
    public String newGame(@ModelAttribute("restaurant") Restaurant newRestaurant,
                          @RequestParam("lat") Double lat,
                          @RequestParam("lng") Double lng,Principal principal) {

        Location location = new Location();
        location.setLat(lat);
        location.setLng(lng);
        newRestaurant.setLocation(location);
        Customer currentCustomer = customerService.getBySub(principal.getName());

        newRestaurant.setCustomerHaveRestaurant(currentCustomer);

        try {
            restaurantService.save(newRestaurant);

            customerService.save(currentCustomer);

        } catch (Exception e) {

            return "Exception in saving restaurant";

        }
        System.out.println("success");
        return "redirect:/customerDashBoard/"+newRestaurant.getCustomerHaveRestaurant().getCid();
    }


    @GetMapping("/restaurantByLocation")
    public String searchRestaurantByLocation(@RequestParam("loc") String place, Model model) {
        Places currentPlace = placesServices.findByName(place);
        model.addAttribute("listRestaurant", getRestaurant(currentPlace.getLocation()));
        return "foodCard";
    }

    List<Restaurant> getRestaurant(Location currentLocation) {
        Double locationLat = currentLocation.getLat();
        Double locationLng = currentLocation.getLng();
        List<Restaurant> resultantList = restaurantService.listAll().stream()                // convert list to stream
                .filter(p -> {
                    if (distance(p.getLocation().getLat(), locationLat, p.getLocation().getLng(), locationLng) <= 4000) {
                        System.out.println("in range");
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
        return resultantList;
    }

    @GetMapping("/resto")
    public String getSingleRestaurant(@RequestParam("id") Integer id, Model model,Principal principal) {
        Restaurant res = restaurantService.get(id);
        model.addAttribute("singleRestaurant", res);
        model.addAttribute("customer",customerService.getBySub(principal.getName()));
        return "singleResto";
    }

    @GetMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable Integer id) {
        restaurantService.delete(id);
        System.out.println("deleted successfully");
        return "deleted successfully";

    }

    @GetMapping("/showRestaurant")
    public String displayRestaurant(
            @RequestParam(required = false, name = "keyword") String keyword,
            @RequestParam(required = false, name = "place") String place,
            @RequestParam(required = false, name = "lat") Double lat,
            @RequestParam(required = false, name = "lng") Double lng,
            @RequestParam(required = false, name = "price") Double price,
            Model model,Principal principal) {
        List<Restaurant> restaurantListByKeyword = null;
        List<Restaurant> restaurantListByKeyPrice = null;
        List<Restaurant> restaurantListByPlace = null;
        List<Restaurant> restaurantListByLatLng = null;
        List<Restaurant> allList = restaurantService.listAll();

        List<List<Restaurant>> content = new ArrayList<>();
        int reference[] = new int[]{0, 0, 0, 0};

        if (keyword != null && !keyword.equals("")) {
            restaurantListByKeyword = restaurantService.searchByName(keyword);
            if (restaurantListByKeyword.size() == 0) {
                System.out.println("search by food");
                restaurantListByKeyword = searchFood(keyword);

            }
            reference[0] = 1;
        }

        if (place != null && !place.equals("")) {
            Places currentPlace = placesServices.findByName(place);
            System.out.println(currentPlace.getName());
            restaurantListByPlace = getRestaurant(currentPlace.getLocation());
            reference[1] = 1;
        }

        if (price != null) {
            restaurantListByKeyPrice = allList.stream()
                    .filter((restaurant) -> {
                        System.out.println(restaurant.getMinPrice());
                        return restaurant.getMinPrice() <= price;
                    })
                    .collect(Collectors.toList());

            reference[2] = 1;
        }
        if (lat != null) {
            restaurantListByLatLng = getRestaurant(new Location(lat, lng));
            reference[3] = 1;
        }
        content.add(restaurantListByKeyword);
        content.add(restaurantListByPlace);
        content.add(restaurantListByKeyPrice);
        content.add(restaurantListByLatLng);

        List<List<Restaurant>> listForPerformCalculation = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            if (reference[i] == 1) {
                listForPerformCalculation.add(content.get(i));
            }
        }

        List<Restaurant> resultList = new ArrayList<>();
        if(listForPerformCalculation.size()>0) {
            resultList = filterList(listForPerformCalculation);
        }
        else  {
            resultList = restaurantService.listAll();

        }

        //logic for name and id for customer
        Customer customer = new Customer();
        if(principal!=null) {
            customer = customerService.getBySub(principal.getName());

        }
        model.addAttribute("customer",customer);
        model.addAttribute("listRestaurant", resultList);
        return "foodCard";
    }

    private List<Restaurant> filterList(List<List<Restaurant>> listForPerformCalculation) {
        List<Restaurant> filteredList = listForPerformCalculation.get(0);
        if (listForPerformCalculation.size() == 3) {
            filteredList = filteredList.stream()
                    .filter(listForPerformCalculation.get(1)::contains)
                    .collect(Collectors.toList());

            filteredList = filteredList.stream()
                    .filter(listForPerformCalculation.get(2)::contains)
                    .collect(Collectors.toList());

        }else if(listForPerformCalculation.size()==2) {
            filteredList = filteredList.stream()
                    .filter(listForPerformCalculation.get(1)::contains)
                    .collect(Collectors.toList());
        }
        else  {

        }
        return filteredList;
    }


    @GetMapping("/searchRestaurant")
    public String searchRestaurant(HttpServletRequest request, Model model) {
        String search = request.getParameter("search");

        List<Restaurant> restaurantList = restaurantService.searchByName(search);
        if (restaurantList.size() == 0) {
            model.addAttribute("listRestaurant", searchFood(search));
        } else {
            model.addAttribute("listRestaurant", restaurantList);

        }
        return "foodCard";
    }


    List<Restaurant> searchFood(String food) {
        List<Restaurant> restaurantList = restaurantService.listAll();

        List<Restaurant> searchedRestaurantList = new ArrayList<>();

        for (Restaurant restaurant : restaurantList) {
            for (Food food1 : restaurant.getFoods()) {
                if (food1.getFoodName().contains(food) || food1.getFoodName().equalsIgnoreCase(food)) {
                    searchedRestaurantList.add(restaurant);
                    System.out.println(restaurant.getName());
                    break;
                }
            }
        }
        return searchedRestaurantList;
    }

//    @GetMapping("/addMenu")
//    public String addMenu() {
//        return "addFoodForm";
//    }
//
//    @PostMapping("/addMenu/{id}")
//    public String addFood(@PathVariable("id") Integer restaurantID,
//                          @RequestParam("name") String name,
//                          @RequestParam("price") String price,
//                          @RequestParam("type") String veg) {
//        Restaurant restaurant = restaurantService.get(restaurantID);//Restaurant ID
//        int foodId = 100;
//        String nameList[] = name.split(",");
//        String priceList[] = price.split(",");
//        String vegList[] = veg.split(",");
//
//        Boolean categoryList[] = new Boolean[vegList.length];
//
//        for (int i = 0; i < vegList.length; i++) {
//            if (vegList[i].equals("Veg"))
//                categoryList[i] = true;
//            else
//                categoryList[i] = false;
//        }
//        List<Food> foodList = new ArrayList<>();
//
//
//        for (int i = 0; i < nameList.length; i++) {
//            Food food = new Food();
//            food.setFoodId(Integer.parseInt("" + restaurantID + (foodId++)));  //Depends on restaurant
//            food.setFoodName(nameList[i]);
//            food.setFoodPrice(Double.parseDouble(priceList[i]));
//            food.setVeg(categoryList[i]);
//            foodList.add(food);
//
//        }
//        restaurant.setFoods(foodList);
//        restaurantService.save(restaurant);
//        return "redirect:/updateMenu/"+restaurantID;
//    }

    @GetMapping("/updateMenu/{id}")
    public String updateMenuForm(@PathVariable("id") int restaurantID, Model model,Principal principal) {

        String sub = principal.getName();
        Customer loggedInUser = customerService.getBySub(sub);
        System.out.println(principal);
        System.out.println(loggedInUser.getRestaurants().getId()+"<--->"+restaurantID);
        if(loggedInUser.getRestaurants().getId() == restaurantID  && loggedInUser.getRole().equals("restaurant")) {

            model.addAttribute("restaurant", restaurantService.get(restaurantID));
            model.addAttribute("customer",customerService.getBySub(principal.getName()));
            return "updateMenu";
        } else  {
            return "redirect:/showRestaurant/";
        }

    }

    @PostMapping("/updateMenu/{id}")
    public String updateMenu(@PathVariable("id") Integer restaurantID,
                             @RequestParam("name") String name,
                             @RequestParam("price") String price,
                             @RequestParam("type") String veg) {
        Restaurant restaurant = restaurantService.get(restaurantID);//Restaurant ID
        int foodId = 100;
        String nameList[] = name.split(",");
        String priceList[] = price.split(",");
        String vegList[] = veg.split(",");

        Boolean categoryList[] = new Boolean[vegList.length];

        for (int i = 0; i < vegList.length; i++) {
            if (vegList[i].equals("Veg"))
                categoryList[i] = true;
            else
                categoryList[i] = false;
        }
        List<Food> foodList = new ArrayList<>();


        for (int i = 0; i < nameList.length; i++) {
            Food food = new Food();
            food.setFoodId(Integer.parseInt("" + restaurantID + (foodId++)));  //Depends on restaurant
            food.setFoodName(nameList[i]);
            food.setFoodPrice(Double.parseDouble(priceList[i]));
            food.setVeg(categoryList[i]);
            foodList.add(food);

        }
        restaurant.setFoods(foodList);
        restaurantService.save(restaurant);
        return "redirect:/updateMenu/"+restaurantID;
    }


    @GetMapping("/searchVeg")
    public String searchVeg(@RequestParam("value") String veg, Model model,Principal principal) {
        boolean category;

        if (veg.equals("veg"))
            category = true;
        else
            category = false;


        List<Restaurant> restaurantList = restaurantService.listAll();

        List<Restaurant> searchedRestaurant = new ArrayList<>();

        if (category == false) {
            for (Restaurant restaurant : restaurantList) {
                int i = 0;
                for (Food food : restaurant.getFoods()) {
                    if (food.getVeg() == false) {
                        i++;
                    }

                }
                if (i == restaurant.getFoods().size())
                    searchedRestaurant.add(restaurant);

            }
            for (Restaurant restaurant : searchedRestaurant)
                System.out.println(restaurant.getName());
            model.addAttribute("listRestaurant", searchedRestaurant);
        } else {
            for (Restaurant restaurant : restaurantList) {

                for (Food food : restaurant.getFoods()) {
                    if (food.getVeg() == true) {
                        searchedRestaurant.add(restaurant);
                        break;
                    }
                }


            }

            for (Restaurant restaurant : searchedRestaurant)
                System.out.println(restaurant.getName());
            model.addAttribute("listRestaurant", searchedRestaurant);

        }
        model.addAttribute("customer", customerService.getBySub(principal.getName()));
        return "foodCard";
    }


    @GetMapping("/cuisineSearch")
    public String cuisineSearch(@RequestParam("cuisine") String cuisine, Model model,Principal principal) {

        List<Restaurant> restaurantList = new ArrayList<>();
        model.addAttribute("listRestaurant", restaurantService.searchCuisine(cuisine));
        model.addAttribute("customer", customerService.getBySub(principal.getName()));
        return "foodCard";

    }


    @GetMapping("/restaurantDashBoard/{id}")
    public String restaurantDashBoard(@PathVariable("id") int id, Model model,Principal principal)
    {
        Restaurant restaurant = restaurantService.get(id);
        Collections.reverse(restaurant.getOrderrList());
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("customer",customerService.getBySub(principal.getName()));
        return "restaurantDashBoard";
    }

    @GetMapping("/searchByPrice/{price}")
    public String searchByPrice(@PathVariable("price") double price, Model model) {
        System.out.println(price);
        List<Restaurant> restaurantList = restaurantService.listAll();

        List<Restaurant> searchedRestaurant = new ArrayList<>();

        if (price < 250) {
            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getMinPrice() < price)
                    searchedRestaurant.add(restaurant);


            }
            model.addAttribute("listRestaurant", searchedRestaurant);
        } else {
            model.addAttribute("listRestaurant", restaurantList);
        }

        return "foodCard";
    }


    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
    }
}

