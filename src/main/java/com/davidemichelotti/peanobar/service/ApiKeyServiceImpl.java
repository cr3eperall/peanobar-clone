/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.ApiKey;
import com.davidemichelotti.peanobar.model.User;
import com.davidemichelotti.peanobar.repository.ApiKeyRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    
    public static final int KEY_DURATION_HOURS=12;
    @Autowired
    ApiKeyRepository apiKeyRepo;

    @Override
    public boolean verifyKey(UUID uuid, String key) {
        ApiKey repoKey=getKeyByUUID(uuid);
        if (repoKey==null) {
            return false;
        }
        if (Duration.between(repoKey.getIssuedAt().toLocalDateTime(),LocalDateTime.now()).toHours()>12) {
            return false;
        }
        if (!repoKey.getApikey().equals(key)) {
            return false;
        }
        return true;
    }
    
    @Override
    public ApiKey getKeyByUUID(UUID uuid) {
        return apiKeyRepo.findById(uuid).orElse(null);
    }

    @Override
    public ApiKey getKeyByUser(UserDto user) {
        return apiKeyRepo.findById(user.getUuid()).orElse(null);
    }

    @Override
    public int revokeKeyByUUID(UUID uuid) {
        if (!apiKeyRepo.existsById(uuid)) {
            return -1;
        }
        apiKeyRepo.deleteById(uuid);
        return 0;
    }

    @Override
    public int revokeKeyByUser(UserDto user) {
        if (!apiKeyRepo.existsById(user.getUuid())) {
            return -1;
        }
        apiKeyRepo.deleteById(user.getUuid());
        return 0;
    }

    @Override
    public ApiKey addKey(UUID uuid, ApiKey key) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(new ApiKey(uuid));
        repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
        repoKey.setApikey(key.getApikey());
        return apiKeyRepo.save(repoKey);
    }

    @Override
    public ApiKey addKey(UUID uuid) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(new ApiKey(uuid));
        repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
        repoKey.setApikey(generateKey());
        return apiKeyRepo.save(repoKey);
    }

    @Override
    public ApiKey updateKey(UUID uuid, ApiKey key) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(null);
        if (repoKey==null) {
            return null;
        }
        repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
        repoKey.setApikey(key.getApikey());
        return apiKeyRepo.save(repoKey);
    }
    private static final char[] POSSIBLE_KEY_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.-_".toCharArray();

    private String generateKey() {
        char[] key=new char[60];
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] bytes = new byte[60];
            random.nextBytes(bytes);
            for (int i = 0; i < key.length; i++) {
                int idx=bytes[i];
                key[i]=POSSIBLE_KEY_CHARS[Math.abs(idx%POSSIBLE_KEY_CHARS.length)];
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ApiKeyServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return String.valueOf(key);
    }
    
}
