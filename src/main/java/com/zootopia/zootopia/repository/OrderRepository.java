package com.zootopia.zootopia.repository;

import com.zootopia.zootopia.dao.models.Orderr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orderr,Long> {
    void deleteByOrderId(long orderId);

    List<Orderr> findAllByOrderByOrderIdDesc();
}
