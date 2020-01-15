package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.exceptions.*;
import com.greenfoxacademy.TribesBackend.models.*;
import com.greenfoxacademy.TribesBackend.repositories.AuthGrantAccessTokenRepository;
import com.greenfoxacademy.TribesBackend.repositories.BlacklistedTokenRepository;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.userinfo.GoogleUserInfo;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.greenfoxacademy.TribesBackend.constants.EmailVerificationConstants.*;
import static com.greenfoxacademy.TribesBackend.constants.ExternalLoginConstants.*;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.HEADER_STRING;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.TOKEN_PREFIX;

@Getter
@Setter
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;
    @Autowired
    private AuthGrantAccessTokenRepository authGrantAccessTokenRepository;
    @Autowired
    private UtilityService utilityService;
    @Autowired
    private KingdomService kingdomService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean doesUserExistById(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean doesUserExistByEmail(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public User findByEmail(String username) {
        return userRepository.findByUsername(username);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void logout(HttpServletRequest request){
        BlacklistedToken blacklistedToken=new BlacklistedToken();
        blacklistedToken.setToken(request.getHeader(HEADER_STRING).replace(TOKEN_PREFIX, ""));
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public String createOAuthRedirection(String redirectUri, OAuth2ConnectionFactory connectionFactory) {
        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
        OAuth2Parameters params = new OAuth2Parameters();
        params.setRedirectUri(redirectUri);
        params.setScope("email");
        return oauthOperations.buildAuthorizeUrl(params);
    }

    public ModelMap authenticateExternalUser(String idExternal, String email, String accessGrantToken, HttpServletRequest request) throws ExternalAccountWithNoEmailException, EmailAlreadyTakenException {
        AuthGrantAccessToken authGrantAccessToken = authGrantAccessTokenRepository.findByIdExternal(idExternal);
        if (authGrantAccessToken == null) {
            if (email == null) {
                throw new ExternalAccountWithNoEmailException();
            }
            if( userRepository.findByUsername(email) !=null ){
                throw new EmailAlreadyTakenException(email);
            }
            User user = new User();
            user.setUsername(email);
            user.setPassword("");
            authGrantAccessToken = new AuthGrantAccessToken();
            authGrantAccessToken.setIdExternal(idExternal);
            authGrantAccessToken.setUser(user);
            authGrantAccessToken.setAccessGrantToken(accessGrantToken);
            user.setAuthGrantAccessToken(authGrantAccessToken);
            authGrantAccessTokenRepository.save(authGrantAccessToken);
            registerUser(user, null);
            return createRegisterResponse(userRepository.findByUsername(user.getUsername()));
        } else {
            return createLoginResponse(authGrantAccessToken.getUser(), request);
        }
    }

    public String createRedirectionToGoogle() {
        GoogleConnectionFactory connectionFactory = new GoogleConnectionFactory(GOOGLE_APP_ID, GOOGLE_APP_SECRET);
        return createOAuthRedirection(GOOGLE_REDIRECT_URI, connectionFactory);
    }

    public ModelMap authenticateGoogleUser(String authenticationCode, HttpServletRequest request) throws ExternalAccountWithNoEmailException, EmailAlreadyTakenException {
        GoogleConnectionFactory connectionFactory = new GoogleConnectionFactory(GOOGLE_APP_ID, GOOGLE_APP_SECRET);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(authenticationCode, GOOGLE_REDIRECT_URI, null);
        String accessToken = accessGrant.getAccessToken();
        GoogleTemplate googleTemplate = new GoogleTemplate(accessToken);
        String[] fields = {"id", "email"};
        GoogleUserInfo userInfo = googleTemplate.userOperations().getUserInfo();
        return authenticateExternalUser(userInfo.getId(), userInfo.getEmail(), accessToken, request);
    }

    public String createRedirectionToFacebook() {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET);
        return createOAuthRedirection(FACEBOOK_REDIRECT_URI, connectionFactory);
    }

    public ModelMap authenticateFbUser(String authenticationCode, HttpServletRequest request) throws ExternalAccountWithNoEmailException, EmailAlreadyTakenException {
        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET);
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(authenticationCode, FACEBOOK_REDIRECT_URI, null);
        String accessToken = accessGrant.getAccessToken();
        FacebookTemplate fbTemplate = new FacebookTemplate(accessToken);
        String[] fields = {"id", "email"};
        org.springframework.social.facebook.api.User userProfile = fbTemplate.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
        return authenticateExternalUser(userProfile.getId(), userProfile.getEmail(), accessToken, request);
    }

    public void sendEmailVerification(String receiver, String verCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(receiver);
        msg.setSubject(SUBJECT);
        String content = utilityService.readFile("verificationEmailContent.txt");
        msg.setText(content.replace(CHARS_TO_BE_REPLACED, verCode));
        javaMailSender.send(msg);
    }

    public String generateEmailVerificationCode() {
        String code;
        do {
            code = "";
            for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
                code += (char) ThreadLocalRandom.current().nextInt(65, 91);
            }
        } while (userRepository.findByVerificationCode(code) != null);
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
        User user = userRepository.findByVerificationCode(verCode);
        if (user != null) {
            if (!user.isEmailVerified()) {
                user.setEmailVerified(true);
                userRepository.save(user);
            } else {
                throw new EmailAlreadyVerifiedException();
            }
        } else {
            throw new IncorrectVerCodeException();
        }
    }

    public ModelMap createLoginResponse(User user, HttpServletRequest request) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("status", "ok");
        modelMap.addAttribute("token", generateTokenBasedOnEmail(user.getUsername(), request));
        return modelMap;
    }

    public ModelMap createRegisterResponse(User user) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("id", user.getId());
        modelMap.addAttribute("username", user.getUsername());
        modelMap.addAttribute("kingdom", user.getKingdom().getName());
        return modelMap;
    }

    public User getUserFromModelMap(ModelMap modelMap) {
        User user = new User();
        user.setUsername((String) modelMap.getAttribute("username"));
        user.setPassword((String) modelMap.getAttribute("password"));
        return user;
    }

    public ModelMap registerUser(User user, String kingdomName) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Kingdom kingdom = new Kingdom();
        if (kingdomName == null) {
            kingdom.setName(generateKingdomNameByEmail(user.getUsername()));
        } else {
            kingdom.setName(kingdomName);
        }
        kingdom.setResources(resourceService.createInitialResources());
        kingdom.setLocation(new Location());
        user.setKingdom(kingdom);
        user.setEmailVerified(false);
        String verCode = generateEmailVerificationCode();
        user.setVerificationCode(verCode);
        userRepository.save(user);
        kingdom.setUserId(user.getId());
        kingdomRepository.save(kingdom);
        sendEmailVerification(user.getUsername(), verCode);
        return createRegisterResponse(findByEmail(user.getUsername()));
    }

    public void checkUserParamsForLogin(User user) throws MissingParamsException, NoSuchEmailException, IncorrectPasswordException, EmailNotVerifiedException {
        checkMissingParams(user);
        if (!doesUserExistByEmail(user.getUsername())) {
            throw new NoSuchEmailException(user.getUsername());
        }
        if (!findByEmail(user.getUsername()).isEmailVerified()) {
            throw new EmailNotVerifiedException();
        }
        if (!bCryptPasswordEncoder.matches(user.getPassword(), findByEmail(user.getUsername()).getPassword())) {
            throw new IncorrectPasswordException();
        }
    }

    public void checkUserParamsForReg(User user) throws MissingParamsException, EmailAlreadyTakenException {
        checkMissingParams(user);
        if (doesUserExistByEmail(user.getUsername())) {
            throw new EmailAlreadyTakenException(user.getUsername());
        }
    }

    public void checkMissingParams(User user) throws MissingParamsException {
        List<String> missingParams = new ArrayList<String>();
        if (user.getUsername() == null) {
            missingParams.add("username");
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
        return utilityService.generateJWT(request.getRemoteAddr(), user.getId(), user.getUsername());
    }

    public boolean isEmailValid(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}