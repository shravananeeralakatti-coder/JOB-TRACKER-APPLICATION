package com.shravana.jobtracker.controller;

import com.shravana.jobtracker.model.User;
import com.shravana.jobtracker.repository.UserRepository;
import com.shravana.jobtracker.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthorizationController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ REGISTER
    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (repo.findByUsername(user.getUsername()) != null) {
            return "User already exists";
        }

        repo.save(user);
        return "Registered successfully";
    }

    // ✅ LOGIN 
    @PostMapping("/login")
    public String login(@RequestBody User user) {

        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());

        User existingUser = repo.findByUsername(user.getUsername());

        if (existingUser == null) {
            return "User not found";
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            return "Invalid password";
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return token;
    }
}