/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.dto;

import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.Role;
import com.davidemichelotti.peanobar.model.User;
import com.davidemichelotti.peanobar.service.OrderServiceImpl;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
/**
 *
 * @author david
 */
public class UserDto {
    private UUID uuid;
    private String fullName;
    private String username;
    private String email;
    private Role role;
    private int balance;
    private String classroom;
    
    private OrderServiceImpl orderService;
    
    public UserDto(UUID uuid, String fullName, String username, String email, Role role, int balance, String classroom, OrderServiceImpl orderService) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.classroom = classroom;
        this.orderService = orderService;
    }
    public UserDto(User user,OrderServiceImpl orderService) {
        this.uuid = user.getUuid();
        this.fullName = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.balance = user.getWallet().getBalance();
        this.classroom = user.getClassroom();
        this.orderService=orderService;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public int getBalance() {
        return balance;
    }

    public OrderDto getCartOrder() {
        return orderService.getCartOrderForUUID(uuid);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.uuid);
        hash = 37 * hash + Objects.hashCode(this.fullName);
        hash = 37 * hash + Objects.hashCode(this.username);
        hash = 37 * hash + Objects.hashCode(this.email);
        hash = 37 * hash + Objects.hashCode(this.role);
        hash = 37 * hash + this.balance;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserDto other = (UserDto) obj;
        if (this.balance != other.balance) {
            return false;
        }
        if (!Objects.equals(this.fullName, other.fullName)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return Objects.equals(this.role, other.role);
    }

    @Override
    public String toString() {
        return "UserDto{" + "uuid=" + uuid + ", fullName=" + fullName + ", username=" + username + ", email=" + email + ", role=" + role + ", balance=" + balance + ", classroom=" + classroom + '}';
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
