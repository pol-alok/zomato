package com.zootopia.zootopia.repository;

import com.zootopia.zootopia.dao.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {

    Optional<Restaurant> findById(Integer id);

    List<Restaurant> findAllByCityContains(String city);
    List<Restaurant> findAllByNameContainsIgnoringCase(String search);
    List<Restaurant> findAllByFoodsContains(String food);

    List<Restaurant> findAllByTagLineContainsIgnoringCase(String cuisine);
}
