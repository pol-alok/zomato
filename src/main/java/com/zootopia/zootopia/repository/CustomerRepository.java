package com.zootopia.zootopia.repository;

import com.zootopia.zootopia.dao.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    void deleteByCid(Long customerId);
    Customer findByEmail(String email);
    Customer findBySub(String sub);
}
