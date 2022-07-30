/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.model.Image;
import com.davidemichelotti.peanobar.repository.ImageRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepo;

    @Override
    public Image saveImage(byte[] image) {
        Image img=new Image();
        img.setData(image);
        return imageRepo.save(img);
    }

    @Override
    public Image findImageById(long id) {
        return imageRepo.findById(id).orElse(null);
    }

    @Override
    public List<Image> findAll() {
        List<Image> l=new ArrayList<>();
        Iterable<Image> it=imageRepo.findAll();
        for (Image img : it) {
            l.add(img);
        }
        return l;
    }

    @Override
    public Image updateImage(long id, Image img) {
        Image repoImg=imageRepo.findById(id).orElse(null);
        if (repoImg==null) {
            return null;
        }
        repoImg.setData(img.getData());
        return imageRepo.save(repoImg);
    }

    @Override
    public int deleteImage(long id) {
        Image repoImg=imageRepo.findById(id).orElse(null);
        if (repoImg==null) {
            return -1;
        }
        imageRepo.delete(repoImg);
        return 0;
    }
    
}
