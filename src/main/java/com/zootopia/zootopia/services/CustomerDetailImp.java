package com.zootopia.zootopia.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomerDetailImp implements UserDetails {

//    String ROLE_PREFIX = "ROLE_";
    private String customerEmail;
    private String password;
    private String role;

    public CustomerDetailImp(String customerEmail, String password, String role) {
        this.customerEmail = customerEmail;
        this.password = password;
        this.role = role;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(new SimpleGrantedAuthority("USER"));
//    }
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
//    List<GrantedAuthority> list = new ArrayList<>();
//    list.add(new SimpleGrantedAuthority(role));
//    return list;
    return Collections.singleton(new SimpleGrantedAuthority(role));
}

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.customerEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
