/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.api;

import com.davidemichelotti.peanobar.config.ConfigProperties;
import com.davidemichelotti.peanobar.dto.OrderDto;
import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.Order;
import com.davidemichelotti.peanobar.model.OrderItem;
import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.service.OrderServiceImpl;
import com.davidemichelotti.peanobar.service.ProductServiceImpl;
import com.davidemichelotti.peanobar.service.UserServiceImpl;
import com.davidemichelotti.peanobar.util.UUIDFormatter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

/**
 *
 * @author david
 */
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrdersAPIRestController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    OrderServiceImpl orderService;
    @Autowired 
    ProductServiceImpl productService;
    @Autowired
    ConfigProperties config;
    
    @PostMapping("/cart")
    public OrderDto addToOwnCart(Authentication auth,@RequestParam("product") long productId, @RequestParam("q") byte quantity) {
        UserDto user=(UserDto)auth.getPrincipal();
        OrderDto cart = getCart(user.getUuid());
        Product prod= productService.findProductById(productId);
        if (prod==null) {
            throw new NullPointerException("Product with id " + productId + " doesn't exist");
        }
        if (quantity<0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        orderService.updateTime(cart.getId());
        return orderService.addItem(cart.getId(), prod, quantity);
    }
    
    @GetMapping("/ownorders")
    public List<OrderDto> getOwnOrders(Authentication auth, @RequestParam("size") int size, @RequestParam("page") int page) {
        UserDto user = (UserDto)auth.getPrincipal();
        if (user == null) {
            throw new NullPointerException("User with uuid " + user.getUuid().toString() + " doesn't exist");
        }
        return orderService.getOrdersByUserPaged(user.getUuid(), size, page);
    }
    
    @GetMapping("/countown")
    public int countOwnOrders(Authentication auth) {
        UserDto user = (UserDto)auth.getPrincipal();
        if (user == null) {
            throw new NullPointerException("User with uuid " + user.getUuid().toString() + " doesn't exist");
        }
        return orderService.countOrdersByUser(user.getUuid());
    }
    
    @GetMapping("/all")
    public List<OrderDto> getAllOrders(@RequestParam("size") int size, @RequestParam("page") int page){
        return orderService.getAllCompletedOrdersPaged(size, page);
    }
    
    @GetMapping("/count")
    public int countAllOrders() {
        return orderService.countOrdersTotal();
    }
    
    @GetMapping("/byuuid")
    public List<OrderDto> getOrdersByUUID(@RequestParam("uuid") String uuid) {
        UUID userUUID = UUIDFormatter.format(uuid);
        UserDto user = userService.findUserByUUID(userUUID);
        if (user == null) {
            throw new NullPointerException("User with uuid " + userUUID.toString() + " doesn't exist");
        }
        return orderService.getOrdersByUser(user.getUuid());
    }

    @GetMapping("/contents")
    public List<OrderItem> getOrderContents(@RequestParam("id") long orderId) {
        return orderService.getOrderItemsById(orderId);
    }

    @GetMapping()
    public OrderDto getOrderById(@RequestParam("id") long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PostMapping()
    public OrderDto addToCart(@RequestParam("uuid") String uuid, @RequestParam("product") long productId, @RequestParam("q") byte quantity) {
        OrderDto cart = getCart(UUIDFormatter.format(uuid));
        Product prod= productService.findProductById(productId);
        if (prod==null) {
            throw new NullPointerException("Product with id " + productId + " doesn't exist");
        }
        if (quantity<0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        orderService.updateTime(cart.getId());
        return orderService.addItem(cart.getId(), prod, quantity);
    }
    
    @PostMapping("/send")
    public ResponseEntity<OrderDto> sendOrder(Authentication auth){
        UserDto user=(UserDto)auth.getPrincipal();
        if(user.getRole().getName().equals("ROLE_USER")){
            if (LocalTime.now().isBefore(config.getStartAcceptingOrders())||LocalTime.now().isAfter(config.getStopAcceptingOrders())) {
                return new ResponseEntity("The bar doesnt accept orders at this time",HttpStatus.SERVICE_UNAVAILABLE);
            }
        }
        OrderDto cart = user.getCartOrder();
        if(cart==null || cart.getContents().isEmpty()){
            return new ResponseEntity("Cart is empty",HttpStatus.BAD_REQUEST);
        }
        if(cart.getTotal()>user.getBalance()){
            return new ResponseEntity("Not enough balance in wallet for user with uuid "+ user.getUuid().toString(),HttpStatus.BAD_REQUEST);
        }
        
        userService.updateBalance(user.getUuid(), user.getBalance()-cart.getTotal());
        ResponseEntity resp=new ResponseEntity(orderService.updateStatus(cart.getId(), Order.OrderStatus.IN_PROGRESS),HttpStatus.OK);
        return resp;
    }
    
    @GetMapping("/toprocess")
    public List<OrderDto> getOrdersToProcess() {
        return orderService.getOrdersByStatus(Order.OrderStatus.IN_PROGRESS);
    }
    
    @PostMapping("/complete")
    public OrderDto completeOrder(@RequestParam("id") long orderId){
        OrderDto order=orderService.getOrderById(orderId);
        if (order==null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Cannot find order to complete");
        }
        if(order.getStatus()!=Order.OrderStatus.IN_PROGRESS){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Cannot find order to complete");
        }
        return orderService.updateStatus(order.getId(), Order.OrderStatus.COMPLETED);
    }
    
    private OrderDto getCart(UUID userUUID) {
        UserDto user = userService.findUserByUUID(userUUID);
        if (user == null) {
            throw new NullPointerException("User with uuid " + userUUID.toString() + " doesn't exist");
        }
        if (user.getCartOrder() != null) {
            return user.getCartOrder();
        } else {
            OrderDto order = new OrderDto(null, 0, user.getUuid(), LocalDateTime.now(), Order.OrderStatus.IN_CART, new ArrayList<>());
            return orderService.createOrder(order);
        }
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> nullPointerEx(NullPointerException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> nullPointerEx(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> serverEx(HttpClientErrorException ex){
        return new ResponseEntity<>(ex.getMessage(),ex.getStatusCode());
    }
}
