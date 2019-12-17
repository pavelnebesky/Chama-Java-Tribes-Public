package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
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

    public boolean doesPasswordMatchAccount(User user) {
        return userRepository.findByEmail(user.getEmail()).getPassword().equals(user.getPassword());
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public String checkUserParams(User user, HttpServletResponse response) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            response.setStatus(400);
            if (user == null || (user.getEmail() == null && user.getPassword() == null)) {
                return "Missing parameter(s): email, password!";
            } else if (user.getEmail() == null) {
                return "Missing parameter(s): email!";
            } else {
                return "Missing parameter(s): password!";
            }
        } else if (!doesUserExistByEmail(user.getEmail())) {
            return "No such user: " + user.getEmail() + "!";
        } else if (!doesPasswordMatchAccount(user)) {
            return "Wrong password!";
        } else {
            return null;
        }
    }

    public boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}