/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.api;

import com.davidemichelotti.peanobar.model.Image;
import com.davidemichelotti.peanobar.service.ImageServiceImpl;
import com.davidemichelotti.peanobar.service.OrderServiceImpl;
import com.davidemichelotti.peanobar.service.ProductServiceImpl;
import com.davidemichelotti.peanobar.service.UserServiceImpl;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author david
 */
@RestController
@RequestMapping(path = "/api/img")
@CrossOrigin(origins = "*")
public class ImagesAPIController {
    
    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ImageServiceImpl imageService;
    
    @PostMapping()
    public Image uploadImage(@RequestParam("img") MultipartFile img) throws IOException{
        byte[] data=null;
        try {
            data=img.getBytes();
            if (data.length>16777215) {
                throw new IOException("Image too large");
            }
        } catch (IOException ex) {
            Logger.getLogger(ImagesAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return imageService.saveImage(data);
    }
    
    @GetMapping("/all")
    public ResponseEntity<Image[]> getAllImages(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Image> repoImgs=imageService.findAll();
        if (repoImgs==null || repoImgs.isEmpty()) {
            return new ResponseEntity(new Image[]{}, headers, HttpStatus.NOT_FOUND);
        }
        ResponseEntity<Image[]> responseEntity = new ResponseEntity(repoImgs, headers, HttpStatus.OK);
        return responseEntity;
    }
    
    @GetMapping()
    public ResponseEntity<byte[]> getImage(@RequestParam long id){
        Image repoImg=imageService.findImageById(id);
        if (repoImg==null) {
            throw new NullPointerException("No image exists with id "+id);
        }
        byte[] img = repoImg.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity(img, headers, HttpStatus.OK);
        return responseEntity;
    }
    
    @PutMapping()
    public Image updateImage(@RequestParam("img") MultipartFile img,@RequestParam("id") long id){
        byte[] data=null;
        try {
            data=img.getBytes();
            if (data.length>16777215) {
                throw new IOException("Image too large");
            }
        } catch (IOException ex) {
            Logger.getLogger(ImagesAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Image repoImg=imageService.findImageById(id);
        if (repoImg==null) {
            throw new NullPointerException("No image exists with id "+id);
        }
        repoImg.setData(data);
        return imageService.updateImage(id, repoImg);
    }
    
    @DeleteMapping()
    public ResponseEntity<Object> deleteImage(@RequestParam("id") long id){
        if (imageService.findImageById(id)==null) {
            throw new NullPointerException("No image exists with id "+id);
        }
        imageService.deleteImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerEx(NullPointerException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> serverEx(IOException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.PAYLOAD_TOO_LARGE);
    }
    
}
