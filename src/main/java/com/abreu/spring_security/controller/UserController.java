package com.abreu.spring_security.controller;

import com.abreu.spring_security.entities.Role;
import com.abreu.spring_security.entities.User;
import com.abreu.spring_security.entities.dto.user.CreateUserDTO;
import com.abreu.spring_security.repositories.RoleRepository;
import com.abreu.spring_security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Void> register(@RequestBody CreateUserDTO data) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(data.username());
        if (userFromDb.isPresent()) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);

        var user = new User(data, passwordEncoder, Set.of(basicRole));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
