package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean doesUserExist(User user) {
        return userRepository.findById(user.getId()).isPresent();
    }
}
