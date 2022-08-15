/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.davidemichelotti.peanobar.repository;

import com.davidemichelotti.peanobar.model.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author david
 */
public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> findAllByOwnerUuid(UUID owner_uuid);
    public List<Order> findAllByStatus(Order.OrderStatus status);
    @Query(value = "SELECT * FROM orders WHERE status = 'COMPLETED' limit ?1 offset ?2",nativeQuery = true)
    public List<Order> findAllCompletedPaged(long limit, long offset);
}
