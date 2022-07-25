/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.config;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.service.ApiKeyServiceImpl;
import com.davidemichelotti.peanobar.service.UserServiceImpl;
import com.davidemichelotti.peanobar.util.UUIDFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 *
 * @author david
 */
@Component
public class CustomAuthenticationManager implements AuthenticationManager{

    @Autowired
    ApiKeyServiceImpl apiKeyService;
    @Autowired
    UserServiceImpl userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String uuid=authentication.getPrincipal().toString();
        String apiKey=authentication.getCredentials().toString();
        if (uuid==null || apiKey==null) {
            throw new BadCredentialsException("Wrong API Key or UUID");
        }
        if(!apiKeyService.verifyKey(UUIDFormatter.format(uuid), apiKey)){
            throw new BadCredentialsException("Wrong API Key or UUID");
        }
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        UserDto user=userService.findUserByUUID(UUIDFormatter.format(uuid));
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new UsernamePasswordAuthenticationToken(user, apiKey, authorities);
    }

}
