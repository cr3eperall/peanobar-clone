/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 *
 * @author david
 */
public class AuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String header=request.getHeader("x-api-key");
        if (header==null||header.split(" ").length!=2) {
            return null;
        }
        String[] authHeader = header.split(" ");
        return authHeader[0];
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        String header=request.getHeader("x-api-key");
        if (header==null||header.split(" ").length!=2) {
            return null;
        }
        String[] authHeader = header.split(" ");
        return authHeader[1];
    }
    
}
