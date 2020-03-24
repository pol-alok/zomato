package com.zootopia.zootopia.repository;


import com.zootopia.zootopia.dao.models.TotalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TotalOrderRepository extends JpaRepository<TotalOrder,Long>
{
     List<TotalOrder> findAll();
}
