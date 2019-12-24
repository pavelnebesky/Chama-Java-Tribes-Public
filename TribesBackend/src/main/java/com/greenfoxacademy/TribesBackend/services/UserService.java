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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.greenfoxacademy.TribesBackend.constants.EmailVerConstants.*;

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
    @Autowired
    private ExceptionService exceptionService;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private JavaMailSender javaMailSender;

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

    public void sendEmailVer(String receiver, String verCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(SUBJECT);
        msg.setText(MESSAGE.replace(CHARS_TO_BE_REPLACED, verCode));
        javaMailSender.send(msg);
    }

    public String generateEmailVerificationCode(){
        String code="";
        for (int i=0; i<VER_CODE_LENGTH; i++){
            code+= (char) ThreadLocalRandom.current().nextInt(65,91);
        }
        return code;
    }

    public void registerUser(User user) {
        Kingdom kingdom = new Kingdom();
        kingdom.setUser(user);
        user.setKingdom(kingdom);
        user.setEmailVerified(false);
        String verCode=generateEmailVerificationCode();
        user.setVerificationCode(verCode);
        userRepository.save(user);
        kingdomRepository.save(kingdom);
        sendEmailVer(user.getEmail(),verCode);
    }

    public void checkUserParamsForLogin(User user) throws MissingParamsException, NoSuchEmailException, IncorrectPasswordException {
        List<String> missingParams = new ArrayList<String>();
        if (user.getEmail() == null) {
            missingParams.add("email");
        } else if (user.getPassword() == null) {
            missingParams.add("password");
        }
        if (!missingParams.isEmpty()) {
            throw new MissingParamsException(missingParams);
        }
        if (!doesUserExistByEmail(user.getEmail())) {
            throw new NoSuchEmailException(user.getEmail());
        } else if (!doesPasswordMatchAccount(user)) {
            throw new IncorrectPasswordException();
        }
    }

    public void checkUserParamsForReg(User user) throws MissingParamsException, NoSuchEmailException, IncorrectPasswordException, EmailAlreadyTakenException {
        List<String> missingParams = new ArrayList<String>();
        if (user.getEmail() == null) {
            missingParams.add("email");
        } else if (user.getPassword() == null) {
            missingParams.add("password");
        }
        if (!missingParams.isEmpty()) {
            throw new MissingParamsException(missingParams);
        }
        if (doesUserExistByEmail(user.getEmail())) {
            throw new EmailAlreadyTakenException(user.getEmail());
        } else if (!doesPasswordMatchAccount(user)) {
            throw new IncorrectPasswordException();
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