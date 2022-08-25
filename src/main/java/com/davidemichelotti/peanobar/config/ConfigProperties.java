/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.config;

import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 *
 * @author david
 */
@ConfigurationProperties(prefix = "peanobar")
public class ConfigProperties {
    
    private int keyDurationHours;
    private String url;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startAcceptingOrders;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime stopAcceptingOrders;

    public LocalTime getStartAcceptingOrders() {
        return startAcceptingOrders;
    }

    public void setStartAcceptingOrders(LocalTime startAcceptingOrders) {
        this.startAcceptingOrders=startAcceptingOrders;
    }

    public LocalTime getStopAcceptingOrders() {
        return stopAcceptingOrders;
    }

    public void setStopAcceptingOrders(LocalTime stopAcceptingOrders) {
        this.stopAcceptingOrders=stopAcceptingOrders;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getKeyDurationHours() {
        return keyDurationHours;
    }

    public void setKeyDurationHours(int keyDurationHours) {
        this.keyDurationHours = keyDurationHours;
    }
    
}
