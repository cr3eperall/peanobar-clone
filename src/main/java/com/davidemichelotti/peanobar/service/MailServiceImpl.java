/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    SpringTemplateEngine thymeleafTemplateEngine;
    @Value("${peanobar.url}")
    String url;
    
    @Override
    public int sendMailToken(String token, String name, String to, Locale locale) {
        return sendMail(to, "Reset Password", getMailBody(token, name, locale));
    }
    
    private String getMailBody(String token, String name, Locale locale){
        Context thymeleafContext = new Context();
        Map<String,Object> templateModel=new HashMap<String, Object>();
        templateModel.put("url", url);
        templateModel.put("token", token);
        templateModel.put("name", name);
        
        thymeleafContext.setVariables(templateModel);
        thymeleafContext.setLocale(locale != null ? locale : Locale.ITALIAN);
        String htmlBody = thymeleafTemplateEngine.process("mailTempl.html", thymeleafContext);
        return htmlBody;
    }

    @Override
    public int sendMail(String to, String subject, String html) {
        MimeMessage message = emailSender.createMimeMessage();
     
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("michelotti.davide@peano.it");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html,true);
        } catch (MessagingException ex) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
        try {
            emailSender.send(message);
        } catch (MailSendException sendException) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, sendException);
            return 1;
        } catch (MailParseException parseException) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, parseException);
            return 2;
        } catch (MailAuthenticationException authException) {
            Logger.getLogger(MailServiceImpl.class.getName()).log(Level.SEVERE, null, authException);
            return 3;
        }
        return 0;
    }
    
}
