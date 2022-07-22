/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.davidemichelotti.peanobar.service;

import com.davidemichelotti.peanobar.dto.UserDto;
import com.davidemichelotti.peanobar.model.Product;
import com.davidemichelotti.peanobar.model.Role;
import com.davidemichelotti.peanobar.model.User;
import com.davidemichelotti.peanobar.model.Wallet;
import com.davidemichelotti.peanobar.repository.RoleRepository;
import com.davidemichelotti.peanobar.repository.UserRepository;
import com.davidemichelotti.peanobar.repository.WalletRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    WalletRepository walletRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    OrderServiceImpl orderService;
    PasswordEncoder pwEncoder=passwordEncoder();

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
    
    @Override
    public UserDto findUserByUUID(UUID uuid) {
        User user=userRepo.findById(uuid).orElse(null);
        if (user==null) {
            return null;
        }
        return new UserDto(user,orderService);
    }

    @Override
    public UserDto findUserByUsername(String username) {
        User user=userRepo.findOneByUsername(username);
        if (user==null) {
            return null;
        }
        return new UserDto(user,orderService);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user=userRepo.findOneByEmail(email);
        if (user==null) {
            return null;
        }
        return new UserDto(user,orderService);
    }

    @Override
    public List<UserDto> findUsersByClassroom(int classroom) {
        List<User> users=userRepo.findAllByClassroom(classroom);
        List<UserDto> dtoUsers=new ArrayList(users.size());
        for (User user : users) {
            dtoUsers.add(new UserDto(user,orderService));
        }
        return dtoUsers;
    }

    @Override
    public List<UserDto> findAllUsers(int size, int page) {
        List<UserDto> l=new ArrayList();
        int offset=page<2 ? 0 : (size*(page-1));
        Iterable<User> it=userRepo.findAllPaged(size<0 ? userRepo.count() : size, offset);
        for (User user : it) {
            l.add(new UserDto(user,orderService));
        }
        return l;
    }

    @Override
    public UserDto createUser(UserDto user, String rawPw) {
        UserDto existingUser=findUserByUsername(user.getUsername());
        if (existingUser!=null) {
            return null;
        }
        existingUser=findUserByEmail(user.getEmail());
        if (existingUser!=null) {
            return null;
        }
        
        User repoUser=new User();
        repoUser.setClassroom(user.getClassroom());
        repoUser.setEmail(user.getEmail());
        repoUser.setName(user.getFullName());
        repoUser.setRole(user.getRole());
        repoUser.setUsername(user.getUsername());
        repoUser.setPassword(pwEncoder.encode(rawPw));
        repoUser=userRepo.save(repoUser);
        Wallet repoWallet=new Wallet(repoUser,0);
        repoWallet.setBalance(0);
        repoWallet.setId(repoUser.getUuid());
        
        repoUser.setWallet(walletRepo.save(repoWallet));
        
        return new UserDto(repoUser, orderService);
    }

    @Override
    public UserDto updateUser(UUID uuid, UserDto user) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return null;
        }
        repoUser.setClassroom(user.getClassroom());
        repoUser.setEmail(user.getEmail());
        repoUser.setName(user.getFullName());
        repoUser.setRole(user.getRole());
        repoUser.setUsername(user.getUsername());
        return new UserDto(userRepo.save(repoUser),orderService);
    }

    @Override
    public Role createRole(Role role) {
        Role repoRole=new Role();
        repoRole.setName(role.getName());
        return roleRepo.save(repoRole);
    }

    @Override
    public Role updateRoleName(long id, String name) {
        Role repoRole=roleRepo.findById(id).orElse(null);
        if (repoRole==null) {
            return null;
        }
        repoRole.setName(name);
        return roleRepo.save(repoRole);
    }

    @Override
    public Role findRoleById(long id) {
        return roleRepo.findById(id).orElse(null);
    }
    

    @Override
    public UserDto updatePassword(UUID uuid, String newRawPassword) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return null;
        }
        repoUser.setPassword(pwEncoder.encode(newRawPassword));
        return new UserDto(userRepo.save(repoUser),orderService);
    }

    @Override
    public UserDto updateRole(UUID uuid, long roleID) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return null;
        }
        Role repoRole=roleRepo.findById(roleID).orElse(null);
        if (repoRole==null) {
            return null;
        }
        repoUser.setRole(repoRole);
        return new UserDto(userRepo.save(repoUser),orderService);
    }

    @Override
    public UserDto updateClassroom(UUID uuid, int classroom) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return null;
        }
        repoUser.setClassroom(classroom);
        return new UserDto(userRepo.save(repoUser),orderService);
    }

    @Override
    public List<UserDto> updateEntireClassroom(int oldClassroom, int newClassroom) {
        List<User> repoUsers=userRepo.findAllByClassroom(oldClassroom);
        List<UserDto> newUsers=new ArrayList<>();
        for (User repoUser : repoUsers) {
            newUsers.add(updateClassroom(repoUser.getUuid(), newClassroom));
        }
        return newUsers;
    }

    @Override
    public UserDto updateBalance(UUID uuid, int balance) {
        Wallet repoWallet=walletRepo.findById(uuid).orElse(null);
        if (repoWallet==null) {
            return null;
        }
        repoWallet.setBalance(balance);
        walletRepo.save(repoWallet);
        return new UserDto(userRepo.findById(repoWallet.getId()).get(), orderService);
    }

    @Override
    public int deleteUserByUUID(UUID uuid) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return -1;
        }
        userRepo.delete(repoUser);
        walletRepo.deleteById(uuid);
        return 0;
    }

    @Override
    public int deleteEntireClassroom(List<UUID> users) {
        for (UUID user : users) {
            if (!userRepo.existsById(user)) {
                return -1;
            }
        }
        for (UUID user : users) {
            userRepo.deleteById(user);
            walletRepo.deleteById(user);
        }
        return 0;
    }

    @Override
    public Role deleteRoleById(long id, long defaultId) {
        Role repoRole=roleRepo.findById(id).orElse(null);
        Role newRepoRole=roleRepo.findById(defaultId).orElse(null);
        if (repoRole==null) {
            return null;
        }
        if (newRepoRole==null) {
            return null;
        }
        List<User> repoUsers=userRepo.findAllByRoleId(id);
        for (User repoUser : repoUsers) {
            repoUser.setRole(newRepoRole);
            userRepo.save(repoUser);
        }
        roleRepo.delete(repoRole);
        return newRepoRole;
    }

    @Override
    public boolean checkPassword(UUID uuid, String rawPassword) {
        User repoUser=userRepo.findById(uuid).orElse(null);
        if (repoUser==null) {
            return false;
        }
        return pwEncoder.matches(rawPassword, repoUser.getPassword());
    }
    
}
