package com.zootopia.zootopia.services;

import com.zootopia.zootopia.dao.models.Restaurant;
import com.zootopia.zootopia.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    public void save(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }
    public Restaurant get(Integer id) {
        return restaurantRepository.findById(id).get();
    }
    public void delete(Integer id) {
        restaurantRepository.deleteById(id);
    }

    public List<Restaurant> listAll()
    {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> searchByCity(String city)
    {
        return restaurantRepository.findAllByCityContains(city);
    }

    public List<Restaurant> searchByName(String search)
    {
        return restaurantRepository.findAllByNameContainsIgnoringCase(search);
    }

    public List<Restaurant> searchFood(String food)
    {
        return  restaurantRepository.findAllByFoodsContains(food);
    }


    public  List<Restaurant> searchCuisine(String cuisine)
    {
        return restaurantRepository.findAllByTagLineContainsIgnoringCase(cuisine);
    }
}
