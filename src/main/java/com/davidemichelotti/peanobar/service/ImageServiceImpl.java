/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.model.Image;
import com.davidemichelotti.peanobar.repository.ImageRepository;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    ImageRepository imageRepo;

    @Override
    public Image saveImage(byte[] image) throws IOException {
        Image img=new Image();
        img.setData(image);
        return imageRepo.save(compress(img, 1080));
    }
    
    public Image compress(Image original, int height) throws IOException{
        InputStream fis=new ByteArrayInputStream(original.getData());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        java.awt.Image image = ImageIO.read(fis);
        fis.close();
        image=image.getScaledInstance((int)(image.getWidth(null)*((double)height/image.getHeight(null))), height, java.awt.Image.SCALE_REPLICATE);
        BufferedImage convertedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        final boolean canWrite = ImageIO.write(convertedImage, "jpg", os);
        Image img=new Image();
        img.setData(os.toByteArray());
        os.close();
        if (!canWrite) {
            throw new IOException("Failed to write image.");
        }
        return img;
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
