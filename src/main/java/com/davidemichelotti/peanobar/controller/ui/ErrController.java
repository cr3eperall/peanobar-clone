/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.ui;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author david
 */
@Controller
public class ErrController implements ErrorController{
    
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(HttpServletRequest request, HttpServletResponse response) {
        String uri = (String)request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if(uri.startsWith("/api/")){
            return null;
        }
        if(uri.startsWith("/en/")||uri.endsWith("/en")){
            response.setStatus(HttpStatus.OK.value());
            return "forward:/en/index.html";
        }else{
            response.setStatus(HttpStatus.OK.value());
            return "forward:/it/index.html";
        }
    }

    public static String getPATH() {
        return PATH;
    }
}
