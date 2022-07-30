/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.model.Image;
import java.util.List;

/**
 *
 * @author david
 */
public interface ImageService {
    public List<Image> findAll();
    public Image saveImage(byte[] image);
    public Image findImageById(long id);
    public Image updateImage(long id, Image img);
    public int deleteImage(long id);
}
