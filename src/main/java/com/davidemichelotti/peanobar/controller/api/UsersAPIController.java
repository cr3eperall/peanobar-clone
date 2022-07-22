/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.api;

import com.davidemichelotti.peanobar.dto.OrderDto;
import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.Role;
import com.davidemichelotti.peanobar.service.OrderServiceImpl;
import com.davidemichelotti.peanobar.service.UserServiceImpl;
import com.davidemichelotti.peanobar.util.UUIDFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UsersAPIController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    PasswordEncoder pwEncoder;
    
    @GetMapping("/own")
    public UserDto getOwnAccount(Authentication auth){
        return (UserDto)auth.getPrincipal();
    }
    
    @GetMapping(path = "/cart")
    public OrderDto getCart(@RequestParam("uuid") String uuid){
        UUID userUUID=UUIDFormatter.format(uuid);
        UserDto user=userService.findUserByUUID(userUUID);
        if (user==null) {
            throw new NullPointerException("User with uuid "+userUUID.toString()+" doesn't exist");
        }
        OrderDto order= user.getCartOrder();
        if (order==null) {
            return new OrderDto(-1l, 0, userUUID, null, Order.OrderStatus.IN_CART, new ArrayList<>());
        }else{
            return order;
        }
    }
    
    @GetMapping("/byuuid")
    public UserDto getUserByUUID(@RequestParam("uuid") String uuid){
        UUID userUUID=UUIDFormatter.format(uuid);
        UserDto user=userService.findUserByUUID(userUUID);
        if (user==null) {
            throw new NullPointerException("User with uuid "+userUUID.toString()+" doesn't exist");
        }
        return user;
    }
    
    @GetMapping("/byusername")
    public UserDto getUserByUsername(@RequestParam("username") String username){
        UserDto user=userService.findUserByUsername(username);
        if (user==null) {
            throw new NullPointerException("User with username "+username+" doesn't exist");
        }
        return user;
    }
    
    @GetMapping("/byemail")
    public UserDto getUserByEmail(@RequestParam("email") String email){
        UserDto user=userService.findUserByEmail(email);
        if (user==null) {
            throw new NullPointerException("User with e-mail "+email+" doesn't exist");
        }
        return user;
    }
    
    @GetMapping("/all")
    public List<UserDto> getAllUsers(@RequestParam("size") int size, @RequestParam("page") int page){
        return userService.findAllUsers(size, page);
    }
    
    @GetMapping("/classroom")
    public List<UserDto> getUsersByClassroom(@RequestParam("classroom") int classroom){
        return userService.findUsersByClassroom(classroom);
    }
    
    @PostMapping()
    public UserDto createNewUser(@RequestParam(name="name",required = true) String name, @RequestParam(name="username",required = true) String username,@RequestParam(name="email",required = true) String email,@RequestParam(name="role_id",required = true) long role_id, @RequestParam(name="classroom",required = true) int classroom, @RequestParam(name="password",required = true) String password){
        Role role=userService.findRoleById(role_id);
        if (role==null) {
            throw new NullPointerException("Role with id "+role_id+" doesn't exist");
        }
        UserDto user=new UserDto(null, name, username, email, role, 0, classroom, null);
        user=userService.createUser(user, password);
        if (user==null) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while creating user");
        }
        return user;
    }
    
    @PatchMapping()
    public UserDto updateUser(@RequestParam(name="uuid",required = true) String uuid, @RequestParam(name="name",required = false) String name,@RequestParam(name="email",required = false) String email,@RequestParam(name="classroom",required = false) Integer classroom){
        UserDto user=userService.findUserByUUID(UUIDFormatter.format(uuid));
        if (user==null) {
            throw new NullPointerException("User with uuid "+UUIDFormatter.format(uuid)+" doesn't exist");
        }
        if (email!=null) {
            user.setEmail(email);
        }
        if (name!=null) {
            user.setFullName(name);
        }
        if (classroom!=null) {
            user.setClassroom(classroom);
        }
        return userService.updateUser(user.getUuid(), user);
    }
    
    @PostMapping(path = "changepassword")
    public ResponseEntity<Object> changePassword(Authentication auth, @RequestParam(name="old",required = true) String oldPassword, @RequestParam(name="new",required = true) String newPassword){
        UserDto user=(UserDto)auth.getPrincipal();
        if (user==null) {
            throw new NullPointerException("User not found");
        }
        if (userService.checkPassword(user.getUuid(), oldPassword)) {
            userService.updatePassword(user.getUuid(), newPassword);
        }else{
            return new ResponseEntity<>("The provided password is wrong", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }
    
    @PatchMapping(path = "classroom")
    public List<UserDto> updateEntireClassroom(@RequestParam(name="old") int oldClassroom,@RequestParam(name="new") Integer newClassroom){
        return userService.updateEntireClassroom(oldClassroom, newClassroom);
    }
    
    @PostMapping(path = "/balance")
    public int updateBalance(@RequestParam(name="uuid") String uuid, @RequestParam(name="bal") int newBalance){
        UserDto user=userService.findUserByUUID(UUIDFormatter.format(uuid));
        if (user==null) {
            throw new NullPointerException("User with uuid "+UUIDFormatter.format(uuid)+" doesn't exist");
        }
        if (newBalance<0) {
            throw new IllegalArgumentException("Balance can't be negative");
        }
        return userService.updateBalance(user.getUuid(), newBalance).getBalance();
    }
    
    @DeleteMapping()
    public ResponseEntity<Object> deleteUser(@RequestParam(name="uuid") String uuid){
        if (userService.deleteUserByUUID(UUIDFormatter.format(uuid))<0) {
            throw new NullPointerException("User with uuid "+UUIDFormatter.format(uuid)+" doesn't exist");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping("/classroom")
    public ResponseEntity<Object> deleteClassroom(@RequestParam(name="classroom") int classroom){
        List<UserDto> users=userService.findUsersByClassroom(classroom);
        if (users.isEmpty()) {
            throw new NullPointerException("Users with classroom "+classroom+" doesn't exist");
        }
        List<UUID> uuids=new ArrayList<>(users.size());
        for (UserDto user : users) {
            uuids.add(user.getUuid());
        }
        if (userService.deleteEntireClassroom(uuids)<0) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
