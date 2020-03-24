package com.zootopia.zootopia.services;
import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println(s);
        Customer customer = customerRepository.findByEmail(s);
        if(customer==null) {
            throw  new UsernameNotFoundException("Author 404!");
        }
        return new CustomerDetailImp(customer.getEmail(),customer.getPassword(),customer.getRole());
    }
}
