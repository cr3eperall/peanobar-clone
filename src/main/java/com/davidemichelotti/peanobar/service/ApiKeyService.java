/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.ApiKey;
import java.util.UUID;

/**
 *
 * @author david
 */
public interface ApiKeyService {
    public boolean verifyKey(UUID uuid, String key);
    public boolean verifyResetToken(UUID uuid, String token);
    public ApiKey getKeyByUUID(UUID uuid);
    public ApiKey getKeyByUser(UserDto user);
    
    public int revokeKeyByUUID(UUID uuid);
    public int revokeKeyByUser(UserDto user);
    
    public ApiKey addKey(UUID uuid, ApiKey key);
    public ApiKey addKey(UUID uuid);
    
    public ApiKey updateKey(UUID uuid, ApiKey key);
    
    public ApiKey updateResetToken(UUID uuid, String rawToken);
    public ApiKey updateResetToken(UUID uuid);
    
}
