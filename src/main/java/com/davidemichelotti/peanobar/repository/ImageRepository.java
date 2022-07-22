/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.davidemichelotti.peanobar.repository;

import com.davidemichelotti.peanobar.model.Image;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author david
 */
public interface ImageRepository extends CrudRepository<Image, Long> {
    
}
