package com.zootopia.zootopia.services;
import com.zootopia.zootopia.dao.models.Customer;
import com.zootopia.zootopia.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class CustomerOIdUserService extends OidcUserService {


    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);
        Map attributes=oidcUser.getAttributes();
        Customer customer =new Customer();
        customer.setPicture(""+attributes.get("picture"));
        customer.setSub(""+attributes.get("sub"));
        customer.setRole("restaurant");
        customer.setEmail(""+attributes.get("email"));
        customer.setName(""+attributes.get("name"));
        updateUser(customer);
        return oidcUser;
    }

    private void updateUser(Customer customer)
    {
        Customer customer1=customerRepository.findByEmail(customer.getEmail());

        if(customer1==null)
        {
            customer1=new Customer();

            customer1.setRole(customer.getRole());
            customer1.setPicture(customer.getPicture());
            customer1.setName(customer.getName());
            customer1.setEmail(customer.getEmail());
            customer1.setSub(customer.getSub());
            customerRepository.save(customer1);

        }
    }

}