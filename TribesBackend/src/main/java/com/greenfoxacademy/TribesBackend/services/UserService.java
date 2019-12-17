package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;

    public boolean doesUserExistById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean doesUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean doesPasswordMatchAccount(User user){
        return userRepository.findByEmail(user.getEmail()).getPassword().equals(user.getPassword());
    }

    public User findById(Long userId) {
        User user = userRepository.findById(userId).get();
        return user;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        userRepository.save(user);
        return user;
    }
}
