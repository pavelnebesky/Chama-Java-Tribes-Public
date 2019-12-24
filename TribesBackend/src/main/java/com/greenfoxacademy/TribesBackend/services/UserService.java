package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.*;
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
import org.springframework.ui.ModelMap;

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
        String code;
        do{
            code="";
            for (int i=0; i<VER_CODE_LENGTH; i++){
                code+= (char) ThreadLocalRandom.current().nextInt(65,91);
            }
        }while(userRepository.findByVerificationCode(code)!=null);
        return code;
    }

    public String generateKingdomNameByEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0] + "'s kingdom";
        } else {
            return null;
        }
    }

    public void verifyEmail(String verCode) throws IncorrectVerCodeException, EmailAlreadyVerifiedException {
        User user=userRepository.findByVerificationCode(verCode);
        if(user!=null){
            if(!user.isEmailVerified()){
                user.setEmailVerified(true);
                userRepository.save(user);
            }else{
                throw new EmailAlreadyVerifiedException();
            }
        }else{
            throw new IncorrectVerCodeException();
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
        user.setEmailVerified(false);
        String verCode=generateEmailVerificationCode();
        user.setVerificationCode(verCode);
        userRepository.save(user);
        kingdomRepository.save(kingdom);
        sendEmailVer(user.getEmail(),verCode);
        return createRegisterResponse(findByEmail(user.getEmail()));
    }

    public void checkUserParamsForLogin(User user) throws MissingParamsException, NoSuchEmailException, IncorrectPasswordException, EmailNotVerifiedException {
        checkMissingParams(user);
        if (!doesUserExistByEmail(user.getEmail())) {
            throw new NoSuchEmailException(user.getEmail());
        }
        if(!findByEmail(user.getEmail()).isEmailVerified()){
            throw new EmailNotVerifiedException();
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(), findByEmail(user.getEmail()).getPassword())) {
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