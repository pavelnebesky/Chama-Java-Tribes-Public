package com.greenfoxacademy.TribesBackend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.greenfoxacademy.TribesBackend.controllers.UserController;
import com.greenfoxacademy.TribesBackend.controllers.KingdomController;
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
    private KingdomController kingdomController;
    @Autowired
    private UserService userService;
    @Autowired
    private UserController userController;

    @Test
    public void kingdomControllerExists() throws InstanceNotFoundException {
        assertThat(kingdomController).isNotNull();
    }

    @Test
    public void serviceLoads() throws InstanceNotFoundException {
        assertThat(userService).isNotNull();
    }

    @Test
    public void controllerLoads() throws InstanceNotFoundException {
        assertThat(userController).isNotNull();
    }

}
