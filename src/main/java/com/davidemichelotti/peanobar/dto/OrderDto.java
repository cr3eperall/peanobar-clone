/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.dto;

import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.OrderItem;
import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.service.OrderServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author david
 */
public class OrderDto {
    private Long id;
    private int total;
    private UUID owner;
    private LocalDateTime madeAt;
    private Order.OrderStatus status;
    private List<OrderItem> contents;
    
    @Autowired
    private static OrderServiceImpl orderService;

    public OrderDto(Long id, int total, UUID owner, LocalDateTime madeAt, Order.OrderStatus status, List<OrderItem> contents) {
        this.id=id;
        this.total = total;
        this.owner=owner;
        this.madeAt = madeAt;
        this.status = status;
        this.contents = contents;
    }

    public OrderDto(Order order, OrderServiceImpl orderService) {
        this.id= order.getId();
        this.total = order.getTotal();
        this.owner=order.getOwner().getUuid();
        this.madeAt = order.getMadeAt().toLocalDateTime();
        this.status = order.getStatus();
        this.contents = orderService.getOrderItemsById(order.getId());
    }

    
    public int getTotal() {
        return total;
    }

    public LocalDateTime getMadeAt() {
        return madeAt;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public List<OrderItem> getContents() {
        if(contents==null){
            return Collections.unmodifiableList(new ArrayList<OrderItem>());
        }
        return Collections.unmodifiableList(contents);
    }

    public long getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }
    
}
