/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.davidemichelotti.peanobar.repository;

import com.davidemichelotti.peanobar.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author david
 */
public interface UserRepository extends CrudRepository<User, UUID> {
    public User findOneByUsername(String username);
    public User findOneByEmail(String email);
    public List<User> findAllByClassroom(int classroom);
    public List<User> findAllByRoleId(long role_id);
    @Query(value="SELECT * FROM users limit ?1 offset ?2", nativeQuery = true)
    public List<User> findAllPaged(long limit, long offset);
    @Query(value="SELECT DISTINCT classroom FROM users", nativeQuery = true)
    public List<Integer> findAllClassrooms();
    @Query(value="SELECT * FROM users WHERE LOWER(username) LIKE CONCAT('%', ?1 ,'%') LIMIT 50", nativeQuery = true)
    public List<User> searchByUsername(String username);
    @Query(value="SELECT * FROM users WHERE LOWER(name) LIKE CONCAT('%', ?1 ,'%') LIMIT 50", nativeQuery = true)
    public List<User> searchByFullname(String fullname);
}
