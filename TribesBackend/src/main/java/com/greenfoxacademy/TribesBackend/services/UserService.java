package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.EmailAlreadyTakenException;
import com.greenfoxacademy.TribesBackend.exceptions.IncorrectPasswordException;
import com.greenfoxacademy.TribesBackend.exceptions.MissingParamsException;
import com.greenfoxacademy.TribesBackend.exceptions.NoSuchEmailException;
import com.greenfoxacademy.TribesBackend.models.Kingdom;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ExceptionService exceptionService;
    @Autowired
    private KingdomRepository kingdomRepository;

    public boolean doesUserExistById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean doesUserExistByEmail(String email) {
        return userRepository.findByEmail(email) != null;
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

    public String generateKingdomNameByEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0] + "'s kingdom";
        } else {
            return null;
        }
    }

    public ModelMap createLoginResponse(User user, HttpServletRequest request) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("status", "ok");
        modelMap.addAttribute("token", generateTokenBasedOnEmail(user.getEmail(), request));
        return modelMap;
    }

    public ModelMap createRegisterResponse(User user) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("id", user.getId());
        modelMap.addAttribute("email", user.getEmail());
        modelMap.addAttribute("kingdom", user.getKingdom().getName());
        return modelMap;
    }

    public ModelMap registerUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Kingdom kingdom = new Kingdom();
        kingdom.setUser(user);
        kingdom.setName(generateKingdomNameByEmail(user.getEmail()));
        user.setKingdom(kingdom);
        userRepository.save(user);
        kingdomRepository.save(kingdom);
        return createRegisterResponse(findByEmail(user.getEmail()));
    }

    public void checkUserParamsForLogin(User user) throws MissingParamsException, NoSuchEmailException, IncorrectPasswordException {
        checkMissingParams(user);
        if (!doesUserExistByEmail(user.getEmail())) {
            throw new NoSuchEmailException(user.getEmail());
        } else if (!bCryptPasswordEncoder.matches(user.getPassword(), findByEmail(user.getEmail()).getPassword())) {
            throw new IncorrectPasswordException();
        }
    }

    public void checkUserParamsForReg(User user) throws MissingParamsException, EmailAlreadyTakenException {
        checkMissingParams(user);
        if (doesUserExistByEmail(user.getEmail())) {
            throw new EmailAlreadyTakenException(user.getEmail());
        }
    }

    public void checkMissingParams(User user) throws MissingParamsException {
        List<String> missingParams = new ArrayList<String>();
        if (user.getEmail() == null) {
            missingParams.add("email");
        }
        if (user.getPassword() == null) {
            missingParams.add("password");
        }
        if (!missingParams.isEmpty()) {
            throw new MissingParamsException(missingParams);
        }
    }

    public String generateTokenBasedOnEmail(String email, HttpServletRequest request) {
        User user = findByEmail(email);
        return authenticationService.generateJWT(request.getRemoteAddr(), user.getId());
    }

    public boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}