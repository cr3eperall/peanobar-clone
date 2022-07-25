/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author david
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer{
    
    @Autowired
    CustomAuthenticationManager authManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        AuthenticationFilter filter=new AuthenticationFilter();
        filter.setAuthenticationManager(authManager);
        
        http.authorizeHttpRequests()
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                .antMatchers("/*", "/", "/assets/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/img","/api/order/complete","/api/product","/api/user/balance").hasAnyRole("ADMIN","BAR")
                .antMatchers(HttpMethod.PUT, "/api/img").hasAnyRole("ADMIN","BAR")
                .antMatchers(HttpMethod.DELETE, "/api/img").hasAnyRole("ADMIN","BAR")
                .antMatchers(HttpMethod.PATCH, "/api/product").hasAnyRole("ADMIN","BAR")
                .antMatchers(HttpMethod.GET, "/api/order/byuuid","/api/order/contents","/api/order","/api/order/toprocess","/api/user/classroom","/api/user/all","/api/user/byemail","/api/user/byusername","/api/user/byuuid").hasAnyRole("ADMIN","BAR")
                .antMatchers(HttpMethod.POST, "/api/user/changepassword","/api/order/send","/api/order/cart").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.GET, "/api/user/own","api/order/ownorders","api/order/countown").hasAnyRole("ADMIN","USER")
                .antMatchers(HttpMethod.POST,"/api/auth/revoke","/api/order","/api/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET,"/api/user/cart","/api/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/api/user","/api/user/classroom").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/api/user","/api/user/classroom").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().csrf().disable()
                .logout().disable()
                //.and().logout().logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))).and()
                .httpBasic().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(filter);
        return http.build();
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"
    };
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }
    
    
    
    
}
