package com.greenfoxacademy.TribesBackend.services;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private userRepository userRepo;

    public boolean doesUserExist(User user) {
        return userRepo.findById(user.getId()).isPresent();
    }
}
