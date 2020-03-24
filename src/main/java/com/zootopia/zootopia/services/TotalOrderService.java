package com.zootopia.zootopia.services;


import com.zootopia.zootopia.dao.models.TotalOrder;
import com.zootopia.zootopia.repository.TotalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TotalOrderService
{    @Autowired
    private TotalOrderRepository totalOrderRepository;

   public void save(TotalOrder totalOrder)
    {
        totalOrderRepository.save(totalOrder);
    }

  public List<TotalOrder> getAll()
  {
      return totalOrderRepository.findAll();
  }
}
