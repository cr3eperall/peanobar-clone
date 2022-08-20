/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.OrderDto;
import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.OrderItem;
import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.repository.OrderItemsRepository;
import com.davidemichelotti.peanobar.repository.OrderRepository;
import com.davidemichelotti.peanobar.repository.UserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    UserRepository userService;
    @Autowired
    OrderItemsRepository orderItemRepo;
    //TODO remake paged queries and add getall

    @Override
    public OrderDto getOrderById(long id) {
        Order order = orderRepo.findById(id).orElse(null);
        if (order==null) {
            return null;
        }
        return new OrderDto(order,this);
    }

    @Override
    public List<OrderDto> getOrdersByUser(UUID uuid) {
        List<Order> orders = orderRepo.findAllByOwnerUuid(uuid);
        List<OrderDto> dto=new ArrayList(orders.size());
        for (Order order : orders) {
            dto.add(new OrderDto(order,this));
        }
        return dto;
    }

    @Override
    public List<OrderDto> getOrdersByUserPaged(UUID uuid, int size, int page) {
        List<Order> all=orderRepo.findAllByOwnerUuid(uuid);
        Collections.reverse(all);
        List<OrderDto> l=new ArrayList(size);
        for (Order order : all) {
            l.add(new OrderDto(order, this));
        }
        return page(l,size,page);
    }

    @Override
    public List<OrderDto> getAllCompletedOrdersPaged(int size, int page) {
        List<OrderDto> l=new ArrayList<>(size < 1 ? 10 : size);
        int offset=page<2 ? 0 : (size*(page-1));
        int notCounted=orderRepo.findAllByStatus(Order.OrderStatus.IN_CART).size()+orderRepo.findAllByStatus(Order.OrderStatus.IN_PROGRESS).size();
        List<Order> all=orderRepo.findAllCompletedPaged(size<0 ? orderRepo.count()-notCounted : size, offset);
        for (Order order : all) {
            if (order.getOwner()!=null) {
                l.add(new OrderDto(order, this));
            }
        }
        return l;
    }
    
    @Override
    public int countOrdersByUser(UUID uuid) {
        List<Order> all=orderRepo.findAllByOwnerUuid(uuid);
        return all.size();
    }

    @Override
    public int countOrdersTotal() {
        return (int)orderRepo.count();
    }
    
    public List<OrderDto> page(List<OrderDto> orders, int size, int page){
        List<OrderDto> l=new ArrayList();
        int offset=page<2 ? 0 : (size*(page-1));
        if(offset<0){
            offset=0;
        }
        if(offset>orders.size()){
            offset=orders.size();
        }
        int sizeN= size <0 ? orders.size() : size;
        if(offset+sizeN>orders.size()){
            int t=orders.size()-offset;
            sizeN= t<0 ? 0 : t;
        }
        for (int i = 0; i < sizeN; i++) {
            l.add(orders.get(i+offset));
        }
        return l;
    }

    @Override
    public List<OrderDto> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> orders = orderRepo.findAllByStatus(status);
        List<OrderDto> dto=new ArrayList(orders.size());
        for (Order order : orders) {
            dto.add(new OrderDto(order,this));
        }
        return dto;
    }

    @Override
    public OrderDto createOrder(OrderDto order) {
        Order repoOrder=new Order(null, userService.findById((order.getOwner())).get(), order.getTotal(), order.getMadeAt() == null ? Timestamp.valueOf(LocalDateTime.now()) : Timestamp.valueOf(order.getMadeAt()), order.getStatus());
        return new OrderDto(orderRepo.save(repoOrder),this);
    }

    @Override
    public List<OrderItem> getOrderItemsById(long id) {
        return orderItemRepo.findAllByOrderId(id);
    }

    @Override
    public OrderDto addItem(long orderId, Product product, short quantity) {//TODO calcola totale
        if(!orderRepo.existsById(orderId)){
            return null;
        }
        OrderItem item;
        Long id=null;
        List<OrderItem> orderItems=orderItemRepo.findAllByOrderId(orderId);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProduct().equals(product)) {
                id=orderItem.getId();
            }
        }
        if(id==null){
            if(quantity!=0){
                item=new OrderItem();
                item.setOrder(orderRepo.findById(orderId).get());
                item.setProduct(product);
                item.setQuantity(quantity);
                orderItemRepo.save(item);
            }
            
        }else{
            item=orderItemRepo.findById(id).get();
            if (quantity==0) {
                orderItemRepo.deleteById(id);
                Order order=orderRepo.findById(orderId).get();
                order.setTotal(calcTotal(new OrderDto(order,this)));
                return new OrderDto(orderRepo.save(order),this);
            }else{
                item.setQuantity(quantity);
                orderItemRepo.save(item);
            }
        }
        Order order=orderRepo.findById(orderId).get();
        order.setTotal(calcTotal(new OrderDto(order,this)));
        
        return new OrderDto(orderRepo.save(order),this);
    }

    @Override
    public OrderDto editOrder(long orderId, OrderDto order) {//TODO //TODO calcola totale
        Order repoOrder = orderRepo.findById(orderId).orElse(null);
        if (repoOrder==null) {
            return null;
        }
        repoOrder.setMadeAt(Timestamp.valueOf(order.getMadeAt()));
        repoOrder.setOwner(userService.findById((order.getOwner())).get());
        repoOrder.setStatus(order.getStatus());
        for (OrderItem content : order.getContents()) {
            Optional<OrderItem> optItem = orderItemRepo.findById(content.getId());
            if (optItem.isPresent()) {
                if (!optItem.get().getProduct().getId().equals(content.getProduct().getId())
                        || optItem.get().getQuantity()!=content.getQuantity()) {
                    addItem(orderId, content.getProduct(),content.getQuantity());
                }
            }
        }
        repoOrder.setTotal(calcTotal(new OrderDto(repoOrder,this)));
        return new OrderDto(orderRepo.save(repoOrder),this);
    }

    @Override
    public OrderDto removeItem(long orderId, long itemId) {//TODO calcola totale
        if(!orderRepo.existsById(orderId)){
            return null;
        }
        if(!orderItemRepo.existsById(orderId)){
            return null;
        }
        orderItemRepo.deleteById(itemId);
        Order order=orderRepo.findById(orderId).get();
        order.setTotal(calcTotal(new OrderDto(order,this)));
        orderRepo.save(order);
        return new OrderDto(orderRepo.findById(orderId).get(),this);
    }
    

    @Override
    public OrderDto updateStatus(long orderId, Order.OrderStatus status) {
        if(!orderRepo.existsById(orderId)){
            return null;
        }
        Order repoOrder =orderRepo.findById(orderId).get();
        repoOrder.setStatus(status);
        return new OrderDto(orderRepo.save(repoOrder),this);
    }

    @Override
    public OrderDto updateTime(long orderId) {
        if(!orderRepo.existsById(orderId)){
            return null;
        }
        Order repoOrder =orderRepo.findById(orderId).get();
        repoOrder.setMadeAt(Timestamp.valueOf(LocalDateTime.now()));
        return new OrderDto(orderRepo.save(repoOrder),this);
    }
    
    public int calcTotal(OrderDto order){
        long total=0;
        for (OrderItem content : order.getContents()) {
            total+=content.getQuantity()*content.getProduct().getCost();
        }
        if (total>Integer.MAX_VALUE) {
            throw new IllegalArgumentException("This order costs too much: "+total/100);
        }
        return (int)total;
    }
    
}
