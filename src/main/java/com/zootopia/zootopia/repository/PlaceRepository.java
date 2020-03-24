package com.zootopia.zootopia.repository;

import com.zootopia.zootopia.dao.models.Places;
import org.springframework.data.jpa.repository.JpaRepository;

//public interface CustomerRepository extends JpaRepository<Customer,Long>
public interface PlaceRepository extends JpaRepository<Places,String> {
    Places findByName(String place);

}
