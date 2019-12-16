package com.greenfoxacademy.TribesBackend;

import static org.assertj.core.api.Assertions.assertThat;
import com.greenfoxacademy.TribesBackend.controllers.HomeController;
<<<<<<< HEAD
import com.greenfoxacademy.TribesBackend.controllers.UserController;
=======
import com.greenfoxacademy.TribesBackend.controllers.KingdomController;
>>>>>>> d55a0a979f52109125888c720fa28a8ac33c2b0f
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.management.InstanceNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
class TribesBackendApplicationTests {

    @Autowired
    private HomeController homeController;
    @Autowired
    private KingdomController kingdomController;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;

    @Test
    public void contexLoads() throws InstanceNotFoundException {
        assertThat(homeController).isNotNull();
    }
    
    @Test
    public void kingdomControllerExists() throws InstanceNotFoundException {
        assertThat(kingdomController).isNotNull();
    }
    
    @Test
    public void serviceLoads() throws InstanceNotFoundException {
        assertThat(userService).isNotNull();
    }
<<<<<<< HEAD

    @Test
    public void controllerLoads() throws InstanceNotFoundException{
        assertThat(userController).isNotNull();
=======
    
    @Test
    public void getKingdomMethodExists() throws InstanceNotFoundException {
        assertThat(kingdomController.getKingdom()).isNotNull();
>>>>>>> d55a0a979f52109125888c720fa28a8ac33c2b0f
    }
}
