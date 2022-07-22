/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.api;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.ApiKey;
import com.davidemichelotti.peanobar.service.ApiKeyServiceImpl;
import com.davidemichelotti.peanobar.service.UserServiceImpl;
import com.davidemichelotti.peanobar.util.UUIDFormatter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthAPIRestController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ApiKeyServiceImpl apiKeyService;
    
    @PostMapping("/login")
    public ApiKey getKey(@RequestParam("username") String username,@RequestParam("password") String password,HttpServletResponse response){
        UserDto user=userService.findUserByUsername(username);
        if (user==null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        if (!userService.checkPassword(user.getUuid(), password)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        return apiKeyService.addKey(user.getUuid());
    }
    
    @PostMapping("/revoke")
    public ResponseEntity<Object> revokeKey(@RequestParam("uuid") String uuid, @RequestHeader("x-api-key") String key,HttpServletResponse response){
        UserDto user=userService.findUserByUUID(UUIDFormatter.format(uuid));
        if (user==null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        if (!apiKeyService.verifyKey(UUIDFormatter.format(uuid), key)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
        if(apiKeyService.revokeKeyByUser(user)<0){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Object> logout(Authentication auth){
        UserDto user=(UserDto)auth.getPrincipal();
        if(apiKeyService.revokeKeyByUUID(user.getUuid())>=0){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
