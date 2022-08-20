/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.model.Product;
import java.util.List;

/**
 *
 * @author david
 */
public interface ProductService {
    public List<Product> findProducts();
    public Product findProductById(long id);
    
    public Product createProduct(Product product);
    
    public Product updateProduct(long id, Product product);
    public Product updateProductName(long id, String name);
    public Product updateProductCost(long id, int cost);
    public Product updateProductImg(long id, Long imgId);
    public Product updateProductType(long id, String type);
    
    public int deleteProduct(long product);
}
