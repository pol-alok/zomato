package com.zootopia.zootopia.payment;

import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.dao.models.Orderr;
import com.zootopia.zootopia.dao.models.Restaurant;
import com.zootopia.zootopia.pojo.Food;
import com.zootopia.zootopia.services.CustomerService;
import com.zootopia.zootopia.services.OrderService;
import com.zootopia.zootopia.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
@Controller
public class PaymentController {
    @Autowired
    private PaytmDetails paytmDetails;
    @Autowired
    private Environment environment;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private OrderService orderService;

    Orderr orderr = new Orderr();
    @PostMapping("/payment/{restaurantID}")
    public String placeOrder(@PathVariable("restaurantID")int restaurantID,
                             @RequestParam("id") String id,
                             @RequestParam("name") String name,
                             @RequestParam("price") String price,
                             @RequestParam("noOfItem") String noOfItem,
                             @RequestParam("total") String total,
                             @RequestParam("type") String vegString, Principal principal,Model model) {

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
            foodList.add(food);

        }

        String sub  = principal.getName();

        Customer customer = customerService.getBySub(sub);
        Restaurant restaurant = restaurantService.get(restaurantID);


        orderr.setCustomer(customer);
        orderr.setRestaurant(restaurant);


        double bill = 0;
        for (String s : totalList) {
            bill += Double.parseDouble(s);

        }

        orderr.setBill(bill);
        orderr.setFoodItemList(foodList);
        long randomNumber = getRandomNumber();
        String txId = "Tx_"+randomNumber;

        model.addAttribute("transactionId",txId);
        model.addAttribute("customer",customer);
        model.addAttribute("bill",bill);
        return "home";
    }
//    @GetMapping("/payment/{id}")
//    public String home(@PathVariable Integer id,Model model) {
//
//        return "home";
//    }
    @PostMapping("/redirect")
    public ModelAndView  pageRedirect(@RequestParam(required = false,name = "C_ID") String customerId,
                                      @RequestParam(name = "TXN_AMOUNT") String transactionAmount,
                                      @RequestParam(name = "ORDER_ID") String orderId) throws Exception {
        System.out.println(customerId);
        System.out.println(transactionAmount);
        System.out.println(paytmDetails.getPaytmUrl());
        System.out.println(paytmDetails.getMerchantId());
                orderService.save(orderr);
        System.out.println("order saved");
        orderr = null;
        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetails.getPaytmUrl());
        TreeMap<String, String> parameters = new TreeMap<>();
        paytmDetails.getDetails().forEach((k, v) -> parameters.put(k, v));
        parameters.put("MOBILE_NO", environment.getProperty("paytm.mobile"));
        parameters.put("EMAIL", environment.getProperty("paytm.email"));
        parameters.put("ORDER_ID", orderId);
        parameters.put("TXN_AMOUNT", transactionAmount);
        parameters.put("CUST_ID", customerId);
        String checkSum = getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checkSum);
        modelAndView.addAllObjects(parameters);
        return modelAndView;
    }
    @PostMapping(value = "/pgresponse")
    public String getResponseRedirect(HttpServletRequest request, Model model,Principal principal) {
        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<String, String>();
        mapData.forEach((key, val) -> parameters.put(key, val[0]));
        String paytmChecksum = "";
        if (mapData.containsKey("CHECKSUMHASH")) {
            paytmChecksum = mapData.get("CHECKSUMHASH")[0];
        }
        String result;
        boolean isValideChecksum = false;
        System.out.println("RESULT : "+parameters.toString());
        try {
            isValideChecksum = validateCheckSum(parameters, paytmChecksum);
            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
                if (parameters.get("RESPCODE").equals("01")) {
                    result = "Payment Successful";
                } else {
                    result = "Payment Failed";
                }
            } else {
                result = "Checksum mismatched";
            }
        } catch (Exception e) {
            result = e.toString();
        }
        model.addAttribute("result",result);
        parameters.remove("CHECKSUMHASH");
        model.addAttribute("parameters",parameters);
        model.addAttribute("customer",customerService.getBySub(principal.getName()));
        return "report";
    }
    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return CheckSumServiceHelper.getCheckSumServiceHelper().verifycheckSum(paytmDetails.getMerchantKey(),
                parameters, paytmChecksum);
    }
    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return CheckSumServiceHelper.getCheckSumServiceHelper().genrateCheckSum(paytmDetails.getMerchantKey(),
                parameters);
    }

    public long getRandomNumber() {
        return (long) (Math.random() * 1000000L);
    }
}
