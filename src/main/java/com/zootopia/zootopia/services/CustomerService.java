package com.zootopia.zootopia.services;


import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Transactional
@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public void save(Customer customer) {
        customerRepository.save(customer);


    }


    public Customer get(long customerId) {
        return customerRepository.getOne(customerId);
    }

    public void delete(Long customerId) {
        customerRepository.deleteByCid(customerId);
    }

    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public  Customer getBySub(String sub) {
        return customerRepository.findBySub(sub);
    }

}
