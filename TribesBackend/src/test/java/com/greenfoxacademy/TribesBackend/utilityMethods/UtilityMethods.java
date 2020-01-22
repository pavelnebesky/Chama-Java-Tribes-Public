package com.greenfoxacademy.TribesBackend.utilityMethods;

import com.auth0.jwt.JWT;
import com.greenfoxacademy.TribesBackend.enums.BuildingType;
import com.greenfoxacademy.TribesBackend.enums.ResourceType;
import com.greenfoxacademy.TribesBackend.models.*;
import com.greenfoxacademy.TribesBackend.repositories.*;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.greenfoxacademy.TribesBackend.constants.BuildingConstants.BUILDING_TIMES;
import static com.greenfoxacademy.TribesBackend.constants.ResourceConstants.GOLD_PER_MINUTE;
import static com.greenfoxacademy.TribesBackend.constants.SecurityConstants.*;

@Configuration
public class UtilityMethods {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KingdomRepository kingdomRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private TroopRepository troopRepository;
    @Autowired
    private BlacklistedTokenRepository blacklistedToken;
    @Autowired
    private AuthGrantAccessTokenRepository authGrantAccessToken;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    public User createEverything(String username, String kingdomName, int goldAmount, int foodAmount, List<BuildingType> types) {
        User user = createUser(username, "blah", true);
        Long userId = userRepository.findByUsername(username).getId();
        Kingdom kingdom = new Kingdom();
        kingdom.setUserId(userId);
        kingdom.setResources(new ArrayList<Resource>());
        kingdom.setTroops(new ArrayList<Troop>());
        kingdom.setLocation(new Location());
        kingdom.setName(kingdomName);
        List<Building> buildings = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            buildings.add(setupBuilding(types.get(i), kingdom));
        }
        kingdom.setBuildings(buildings);
        kingdomRepository.save(kingdom);
        setupResources(goldAmount, foodAmount, userId);
        user.setKingdom(kingdom);
        userRepository.save(user);
        return user;
    }

    public User createUser(String username, String password, boolean isEmailVerified) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(isEmailVerified);
        user.setVerificationCode(userService.generateEmailVerificationCode());
        userRepository.save(user);
        return userRepository.findByUsername(username);
    }

    private Building setupBuilding(BuildingType type, Kingdom kingdom) {
        Building building = new Building();
        building.setType(type);
        building.setHp(1);
        building.setKingdom(kingdom);
        building.setLevel(1);
        building.setStarted_at(System.currentTimeMillis());
        building.setFinished_at(building.getStarted_at() + BUILDING_TIMES.get(type));
        building.setUpdated_at(building.getFinished_at());
        return building;
    }

    private void setupResources(int goldAmount, int foodAmount, Long userId) {
        Kingdom kingdom = kingdomRepository.findByUserId(userId);
        Resource goldResource = new Resource();
        goldResource.setAmount(goldAmount);
        goldResource.setKingdom(kingdom);
        goldResource.setGeneration(2 * GOLD_PER_MINUTE);
        goldResource.setType(ResourceType.gold);
        Resource foodResource = new Resource();
        foodResource.setAmount(foodAmount);
        foodResource.setKingdom(kingdom);
        foodResource.setGeneration(2 * GOLD_PER_MINUTE);
        foodResource.setType(ResourceType.food);
        kingdom.setResources(List.of(goldResource, foodResource));
        kingdomRepository.save(kingdom);
    }

    public String generateToken(String username, String ip, Long userId) {
        Map<String, Object> headerMap = Map.of(IP_CLAIM, ip);
        return JWT.create()
                .withHeader(headerMap)
                .withClaim(ID_CLAIM, String.valueOf(userId))
                .withClaim(USERNAME_CLAIM, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }

    public void clearDB() {
        userRepository.deleteAll();
        kingdomRepository.deleteAll();
        buildingRepository.deleteAll();
        resourceRepository.deleteAll();
        troopRepository.deleteAll();
        blacklistedToken.deleteAll();
        authGrantAccessToken.deleteAll();
    }
}
