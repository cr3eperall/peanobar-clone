/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.api;

import com.davidemichelotti.peanobar.model.Image;
import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.service.ImageServiceImpl;
import com.davidemichelotti.peanobar.service.ProductServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
public class ProductsAPIController {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    ImageServiceImpl imageService;
    
    @GetMapping()
    public Product getProduct(@RequestParam("id") int id){
        Product product=productService.findProductById(id);
        if (product==null) {
            throw new NullPointerException("Cannot find product with id "+id);
        }
        return product;
    }
    
    @GetMapping("/all")
    public List<Product> getAllProducts(){
        return productService.findProducts();
    }
    
    @PostMapping()
    public Product createProduct(@RequestParam("name") String name, @RequestParam("cost") int cost, @RequestParam("img") long imageId, @RequestParam("type") String type){
        Image img=imageService.findImageById(imageId);
        if (img==null) {
            throw new NullPointerException("Cannot find image with id "+imageId);
        }
        if(cost<0){
            throw new IllegalArgumentException("The cost must be positive");
        }
        Product product=new Product(null, name, cost, imageId, type);
        return productService.createProduct(product);
    }
    
    @PatchMapping()
    public Product updateProduct(@RequestParam(name="id",required = true) long id, @RequestParam(name="name",required = false) String name, @RequestParam(name="cost",required = false) Integer cost, @RequestParam(name="img",required = false) Long imageId, @RequestParam(name="type",required = false) String type){
        Product product=productService.findProductById(id);
        if (product==null) {
            throw new NullPointerException("Cannot find product with id "+id);
        }
        if (name!=null) {
            product.setName(name);
        }
        if (cost!=null) {
            product.setCost(cost);
        }
        if (imageId!=null) {
            Image img=imageService.findImageById(imageId);
            if (img==null) {
                throw new NullPointerException("Cannot find image with id "+imageId);
            }
            product.setImg(imageId);
        }
        if (type!=null) {
            product.setType(type);
        }
        return productService.updateProduct(id, product);
    }
}
