/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author david
 */
@Entity
@Table(name = "wallets")
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private UUID uuid;
    
    private Integer balance;

    public Wallet() {
    }

    public Wallet(User owner, Integer balance) {
        this.uuid = owner.getUuid();
        this.balance = balance;
    }

    public UUID getId() {
        return uuid;
    }

    public void setId(UUID id) {
        this.uuid = id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.uuid);
        hash = 97 * hash + Objects.hashCode(this.balance);
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
        final Wallet other = (Wallet) obj;
        if (!Objects.equals(this.uuid, other.uuid)) {
            return false;
        }
        return Objects.equals(this.balance, other.balance);
    }

    @Override
    public String toString() {
        return "Wallet{" + "id=" + uuid + ", balance=" + balance + '}';
    }
    
    
}
