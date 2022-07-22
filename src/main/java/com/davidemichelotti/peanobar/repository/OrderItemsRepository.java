/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.davidemichelotti.peanobar.repository;

import com.davidemichelotti.peanobar.model.OrderItem;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author david
 */
public interface OrderItemsRepository extends CrudRepository<OrderItem, Long> {
    public List<OrderItem> findAllByOrderId(long order_id);
}
