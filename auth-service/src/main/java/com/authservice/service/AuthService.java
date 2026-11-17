package com.authservice.service;

import com.authservice.entity.UserCredential;
import com.authservice.repository.UserCredentialRepository;
import com.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String saveUser(UserCredential credential) {
        UserCredential userToSave = repository.findByName(credential.getName())
                .orElse(credential);
        userToSave.setName(credential.getName());
        userToSave.setEmail(credential.getEmail());
        userToSave.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(userToSave);
        return "user added to the system";
    }

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }

    public void validateToken(String token) {
        jwtUtil.validateToken(token);
    }
}
