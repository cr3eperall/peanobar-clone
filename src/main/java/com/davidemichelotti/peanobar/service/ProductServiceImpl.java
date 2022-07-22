/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.repository.ProductsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    ProductsRepository productRepo;
    
    @Override
    public Product findProductById(long id) {
        Optional<Product> op;
        if ((op = productRepo.findById(id)).isPresent()) {
            return op.get();
        }else{
            return null;
        }
    }

    @Override
    public Product createProduct(Product product) {
        product.setId(null);
        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(long id, Product product) {
        Product original=findProductById(id);
        if (original==null) {
            return null;
        }
        original.setImg(product.getImg());
        original.setCost(product.getCost());
        original.setName(product.getName());
        original.setType(product.getType());
        return productRepo.save(original);
    }

    @Override
    public Product updateProductName(long id, String name) {
        Product original=findProductById(id);
        if (original==null) {
            return null;
        }
        original.setName(name);
        return productRepo.save(original);
    }

    @Override
    public Product updateProductCost(long id, int cost) {
        Product original=findProductById(id);
        if (original==null) {
            return null;
        }
        Product product=new Product(null, original.getName(), cost, original.getImg(), original.getType());
        return createProduct(product);
    }

    @Override
    public Product updateProductImg(long id, Long imgId) {
        Product original=findProductById(id);
        if (original==null) {
            return null;
        }
        original.setImg(imgId);
        return productRepo.save(original);
    }

    @Override
    public Product updateProductType(long id, String type) {
        Product original=findProductById(id);
        if (original==null) {
            return null;
        }
        original.setType(type);
        return productRepo.save(original);
    }

    @Override
    public List<Product> findProducts() {
        List l=new ArrayList();
        Iterable<Product> it=productRepo.findAll();
        for (Product product : it) {
            l.add(product);
        }
        return l;
    }
    
}
