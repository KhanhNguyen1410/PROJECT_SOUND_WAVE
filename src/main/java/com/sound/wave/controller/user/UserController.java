package com.sound.wave.controller.user;

import com.sound.wave.model.Role;
import com.sound.wave.model.User;
import com.sound.wave.service.role.IRoleService;
import com.sound.wave.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin("*")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        Role role = iRoleService.findRoleByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (iUserService.findByUsername(user.getUsername())){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        iUserService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<Iterable<User>> findAllUser(){
        return new ResponseEntity<>( iUserService.findAll(), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Optional<User>> updateUser(@PathVariable("id") Long id, @RequestBody User user){
        Optional<User> user1= iUserService.findById(id);
        if (!user1.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        user1.get().setFullName(user.getFullName());
        user1.get().setAddress(user.getAddress());
        user1.get().setEmail(user.getEmail());
        user1.get().setAvatar(user.getAvatar());

            iUserService.save(user1.get());
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
