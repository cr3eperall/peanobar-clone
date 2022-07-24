/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package com.davidemichelotti.peanobar.controller.ui;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author david
 */
@Controller
public class UIController implements ErrorController{
    
    private static final String PATH = "/error";

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = PATH)
    public String error() {
        return "forward:/index.html";
    }

    public static String getPATH() {
        return PATH;
    }

    
    
}
