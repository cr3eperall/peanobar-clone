/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import java.util.Locale;

/**
 *
 * @author david
 */
public interface MailService {
    public int sendMailToken(String token, String name, String to, Locale locale);
    public int sendMail(String to, String subject, String body);
}
