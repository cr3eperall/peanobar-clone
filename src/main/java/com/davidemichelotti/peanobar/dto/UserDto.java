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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    private int classroom;
    private ArrayList<OrderDto> orders;
    
    private ConfigurableApplicationContext context = new AnnotationConfigApplicationContext();
    
    public UserDto(UUID uuid, String fullName, String username, String email, Role role, int balance, int classroom, ArrayList<OrderDto> orders) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.classroom = classroom;
        this.orders = orders;
    }
    public UserDto(User user, OrderServiceImpl orderService) {
        this.uuid = user.getUuid();
        this.fullName = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.balance = user.getWallet().getBalance();
        this.classroom = user.getClassroom();
        context.refresh();
        this.orders = (ArrayList<OrderDto>) orderService.getOrdersByUser(user.getUuid());
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
        for (OrderDto order : orders) {
            if (order.getStatus()==Order.OrderStatus.IN_CART) {
                return order;
            }
        }
        return null;
    }
    
    public OrderDto[] getOldOrders(int size, int page){
        OrderDto[] oldOrders=new OrderDto[orders.size()-(page*size)];
        int idx=0;
        for (OrderDto order : oldOrders) {
            if (order.getStatus()==Order.OrderStatus.COMPLETED) {
                oldOrders[idx++]=order;
            }
        }
        return oldOrders;
    }
    
    public boolean isOrderInProgress(){
        for (OrderDto order : orders) {
            if (order.getStatus()==Order.OrderStatus.IN_PROGRESS) {
                return true;
            }
        }
        return false;
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
        hash = 37 * hash + Objects.hashCode(this.orders);
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
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return Objects.equals(this.orders, other.orders);
    }

    @Override
    public String toString() {
        return "UserDto{" + "uuid=" + uuid + ", fullName=" + fullName + ", username=" + username + ", email=" + email + ", role=" + role + ", balance=" + balance + ", classroom=" + classroom + '}';
    }

    public int getClassroom() {
        return classroom;
    }

    public void setClassroom(int classroom) {
        this.classroom = classroom;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
