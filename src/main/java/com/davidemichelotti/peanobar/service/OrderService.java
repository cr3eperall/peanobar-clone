/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.OrderDto;
import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.OrderItem;
import com.davidemichelotti.peanobar.model.Product;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author david
 */
public interface OrderService {
    public OrderDto getOrderById(long id);
    public List<OrderDto> getOrdersByUser(UUID uuid);
    public List<OrderDto> getOrdersByUserPaged(UUID uuid, int size, int page);
    public List<OrderDto> getAllCompletedOrdersPaged(int size, int page);
    public int countOrdersByUser(UUID uuid);
    public int countOrdersTotal();
    public List<OrderItem> getOrderItemsById(long id);
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status);
    
    public OrderDto createOrder(OrderDto order);
    
    public OrderDto addItem(long orderId, Product product, short quantity);
    public OrderDto removeItem(long orderId, long itemId);
    public OrderDto editOrder(long orderId, OrderDto order);
    
    public OrderDto updateStatus(long orderId,Order.OrderStatus status);
    public OrderDto updateTime(long orderId);
    
}
