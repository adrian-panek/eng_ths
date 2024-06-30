package com.pwr.project.repositories;

import com.pwr.project.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);
    User findUserByLogin(String login);
}
