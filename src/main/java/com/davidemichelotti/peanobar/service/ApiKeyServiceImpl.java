/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.ApiKey;
import com.davidemichelotti.peanobar.repository.ApiKeyRepository;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {
    
    @Value("${peanobar.keyDurationHours}")
    int KEY_DURATION_HOURS;
    @Autowired
    ApiKeyRepository apiKeyRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Override
    public boolean verifyKey(UUID uuid, String key) {
        ApiKey repoKey=getKeyByUUID(uuid);
        if (repoKey==null) {
            return false;
        }
        if (Duration.between(repoKey.getIssuedAt().toLocalDateTime(),LocalDateTime.now()).toHours()>KEY_DURATION_HOURS) {
            return false;
        }
        return repoKey.getApikey().equals(key);
    }
    
    @Override
    public boolean verifyResetToken(UUID uuid, String token) {
        ApiKey repoKey=getKeyByUUID(uuid);
        if (repoKey==null) {
            System.out.println("null repokey");
            return false;
        }
        return passwordEncoder.matches(token, repoKey.getPasswordResetToken());
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
        repoKey.setPasswordResetToken(null);
        return apiKeyRepo.save(repoKey);
    }

    @Override
    public ApiKey addKey(UUID uuid) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(new ApiKey(uuid));
        repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
        repoKey.setApikey(generateKey());
        repoKey.setPasswordResetToken(null);
        return apiKeyRepo.save(repoKey);
    }

    @Override
    public ApiKey updateKey(UUID uuid, ApiKey rawKey) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(null);
        if (repoKey==null) {
            return null;
        }
        repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
        repoKey.setApikey(rawKey.getApikey());
        repoKey.setPasswordResetToken(null);
        return apiKeyRepo.save(repoKey);
    }

    @Override
    public ApiKey updateResetToken(UUID uuid, String rawToken) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(new ApiKey(uuid));
        if (repoKey.getApikey()==null) {
            repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
            repoKey.setApikey(generateKey());
        }
        if (rawToken!=null) {
            repoKey.setPasswordResetToken(passwordEncoder.encode(rawToken));
        }else{
            repoKey.setPasswordResetToken(null);
        }
        ApiKey saved=apiKeyRepo.save(repoKey);
        saved.setPasswordResetToken(rawToken);
        return saved;
    }
    
    @Override
    public ApiKey updateResetToken(UUID uuid) {
        ApiKey repoKey=apiKeyRepo.findById(uuid).orElse(new ApiKey(uuid));
        if (repoKey.getApikey()==null) {
            repoKey.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()));
            repoKey.setApikey(generateKey());
        }
        String token=generateKey();
        repoKey.setPasswordResetToken(passwordEncoder.encode(token));
        ApiKey saved=apiKeyRepo.save(repoKey);
        saved.setPasswordResetToken(token);
        return saved;
    }
    
    private static final char[] POSSIBLE_KEY_CHARS="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890.-_".toCharArray();

    private String generateKey() {
        UUID uuid=UUID.randomUUID();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        byte[] salt=buffer.array();
        char[] key=new char[60];
        Random random = new Random();
        byte[] bytes = new byte[60];
        random.nextBytes(bytes);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i]=(byte)(bytes[i]^salt[(int)Math.abs(Math.random()*bytes[i])%16]);
        }
        for (int i = 0; i < key.length; i++) {
            int idx=bytes[i];
            key[i]=POSSIBLE_KEY_CHARS[Math.abs(idx%POSSIBLE_KEY_CHARS.length)];
        }
        return String.valueOf(key);
    }
    
}
