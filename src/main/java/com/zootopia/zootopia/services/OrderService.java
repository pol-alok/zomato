package com.zootopia.zootopia.services;


import com.zootopia.zootopia.dao.models.Orderr;
import com.zootopia.zootopia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
public class OrderService
{     @Autowired
    private OrderRepository orderRepository;

    public void save(Orderr orderr)
    {
        orderRepository.save(orderr);
    }

    public Orderr get(long orderId)
    {
        return orderRepository.getOne(orderId);
    }

    public List<Orderr> getList()
    {
       return orderRepository.findAll();
    }

    public void delete(long orderId)
    {
        orderRepository.deleteByOrderId(orderId);
    }
}
