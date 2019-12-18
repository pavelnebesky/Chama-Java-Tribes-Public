package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private KingdomService kingdomService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean doesUserExistById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean doesUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean doesPasswordMatchAccount(User user) {
        return userRepository.findByEmail(user.getEmail()).getPassword().equals(bCryptPasswordEncoder.encode(user.getPassword()));
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

    public void addNewUser(User user){
        Kingdom kingdom=new Kingdom();
        kingdomService.addNewKingdom(kingdom);
        user.setKingdom(kingdom);
        userRepository.save(user);
    }

    public String getMissingParameters(User user) {
        if (user == null || (user.getEmail() == null && user.getPassword() == null)) {
            return "Missing parameter(s): email, password!";
        } else if (user.getEmail() == null) {
            return "Missing parameter(s): email!";
        } else if (user.getPassword() == null) {
            return "Missing parameter(s): password!";
        } else{
            return null;
        }
    }

    public String checkUserRegisterParams(User user, HttpServletResponse response) {
        String missingParams = getMissingParameters(user);
        if (missingParams != null) {
            response.setStatus(400);
            return missingParams;
        } else if (doesUserExistByEmail(user.getEmail())) {
            response.setStatus(409);
            return "Username already taken, please choose an other one.";
        } else{
            return null;
        }
    }

    public String checkUserLoginParams(User user, HttpServletResponse response) {
        String missingParams=getMissingParameters(user);
        if (missingParams!=null) {
            response.setStatus(400);
            return missingParams;
        } else if (!doesUserExistByEmail(user.getEmail())) {
            response.setStatus(401);
            return "No such user: " + user.getEmail() + "!";
        } else if (!doesPasswordMatchAccount(user)) {
            response.setStatus(401);
            return "Wrong password!";
        } else {
            response.setStatus(200);
            return null;
        }
    }
}
