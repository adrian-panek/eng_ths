package com.pwr.project.controllers;

import com.pwr.project.config.auth.TokenProvider;
import com.pwr.project.dto.JwtDTO;
import com.pwr.project.dto.LoginDTO;
import com.pwr.project.dto.RegisterDTO;
import com.pwr.project.entities.User;
import com.pwr.project.exceptions.InvalidJWTException;
import com.pwr.project.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDTO) throws InvalidJWTException {
        authService.register(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody @Valid LoginDTO loginDTO){
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.password());
        var authUser = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
        return ResponseEntity.ok(new JwtDTO(accessToken));
    }

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

}
