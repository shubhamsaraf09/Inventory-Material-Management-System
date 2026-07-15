package com.jsw.controller;

import com.jsw.dto.AuthRequestDto;
import com.jsw.entity.AppUser;
import com.jsw.repository.AppUserRepository;
import com.jsw.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AuthRequestDto request){
        if(appUserRepository.findByUsername(request.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Error: Username already exists!");
        }

        var newUser = new AppUser();
        newUser.setUsername(request.getUsername());

        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("ROLE_MANAGER");

        appUserRepository.save(newUser);
        log.info("New user registered successfully: {}",request.getUsername());

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequestDto request){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
            );
        } catch(BadCredentialsException e){
            log.warn("Failed login attempt for username: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }

        String jwtToken = jwtUtil.generateToken(request.getUsername());
        log.info("User {} successfully logged in. JWT Token generated.",request.getUsername());

        return ResponseEntity.ok(jwtToken);
    }

}
