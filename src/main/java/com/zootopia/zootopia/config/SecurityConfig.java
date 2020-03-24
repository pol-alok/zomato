package com.zootopia.zootopia.config;


import com.zootopia.zootopia.services.CustomerOIdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomerOIdUserService customerOIdUserService;

    //    @Qualifier("customerDetailService")
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/showRestaurant/**").permitAll()
                .antMatchers("/insertPlaces/**").permitAll()
                .antMatchers("/searchVeg/**").permitAll()
                .antMatchers("/cuisineSearch/**").permitAll()
                .antMatchers("/searchByPrice/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
//                .loginPage("/login")
                .defaultSuccessUrl("/showRestaurant", true)
                .redirectionEndpoint().baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .oidcUserService(customerOIdUserService);
    }



    /* this is for spring security*/
//
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/showRestaurant/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .defaultSuccessUrl("/showRestaurant", true)
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/showRestaurant").permitAll();
//    }

}
