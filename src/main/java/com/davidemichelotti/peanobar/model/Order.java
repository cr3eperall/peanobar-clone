/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author david
 */
@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "owner_uuid", referencedColumnName = "uuid",nullable = false)
    private User owner;
    
    private Integer total;
    
    @Column(name = "made_at")
    private Timestamp madeAt;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(Long id, User owner, Integer total, Timestamp madeAt, OrderStatus status) {
        this.id = id;
        this.owner = owner;
        this.total = total;
        this.madeAt = madeAt;
        this.status = status;
    }

    public Order() {
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Timestamp getMadeAt() {
        return madeAt;
    }

    public void setMadeAt(Timestamp madeAt) {
        this.madeAt = madeAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public static enum OrderStatus{
        IN_CART,
        IN_PROGRESS,
        COMPLETED
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.owner);
        hash = 79 * hash + Objects.hashCode(this.total);
        hash = 79 * hash + Objects.hashCode(this.madeAt);
        hash = 79 * hash + Objects.hashCode(this.status);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        if (!Objects.equals(this.madeAt, other.madeAt)) {
            return false;
        }
        return Objects.equals(this.status, other.status);
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", owner=" + owner + ", total=" + total + ", madeAt=" + madeAt + ", status=" + status + '}';
    }
}
