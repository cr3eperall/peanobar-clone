/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.Role;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author david
 */
public interface UserService {
    
    public UserDto createUser(UserDto user, String rawPw);
    
    public UserDto findUserByUUID(UUID uuid);
    public UserDto findUserByUsername(String username);
    public UserDto findUserByEmail(String email);
    
    public List<UserDto> findUsersByClassroom(String classroom);
    public List<UserDto> findAllUsers(int size, int page);
    public List<String> findAllClassrooms();
    public int countUsersTotal();
    
    public List<UserDto> searchUserByUsername(String username);
    public List<UserDto> searchUserByFullname(String fullname);
    
    public boolean checkPassword(UUID uuid,String rawPassword);
    
    public UserDto updateUser(UUID uuid, UserDto user);
    public UserDto updatePassword(UUID uuid, String newRawPassword);
    public UserDto updateRole(UUID uuid, long roleID);
    public UserDto updateClassroom(UUID uuid, String classroom);
    public List<UserDto> updateEntireClassroom(String oldClassroom, String newClassroom);
    public UserDto updateBalance(UUID uuid, int balance);
    
    public int deleteUserByUUID(UUID uuid);
    public int deleteEntireClassroom(List<UUID> users);
    
    public Role createRole(Role role);
    
    public Role findRoleById(long id);
    
    public Role updateRoleName(long id, String name);
    
    public Role deleteRoleById(long id, long defaultId);
    
}
