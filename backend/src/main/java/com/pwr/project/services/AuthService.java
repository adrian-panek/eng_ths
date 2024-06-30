package com.pwr.project.services;

import com.pwr.project.dto.RegisterDTO;
import com.pwr.project.entities.User;
import com.pwr.project.exceptions.InvalidJWTException;
import com.pwr.project.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login){
        return userRepository.findByLogin(login);
    }
    public void register(RegisterDTO registerDTO) throws InvalidJWTException {
        if (userRepository.findByLogin(registerDTO.login()) != null){
            throw new InvalidJWTException("Username already exists");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
        User newUser = new User(registerDTO.firstname(), registerDTO.surname(),registerDTO.login(), encryptedPassword, registerDTO.isSeller());
        userRepository.save(newUser);
    }

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String login = ((UserDetails) principal).getUsername();
            return userRepository.findUserByLogin(login);
        } else if (principal instanceof String) {
            return userRepository.findUserByLogin((String) principal);
        }
        throw new IllegalStateException("Current user is not authenticated");
    }

}
