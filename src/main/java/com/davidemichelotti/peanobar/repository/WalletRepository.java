/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.davidemichelotti.peanobar.repository;

import com.davidemichelotti.peanobar.model.Wallet;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author david
 */
public interface WalletRepository extends CrudRepository<Wallet, UUID> {
    
}
