package com.greenfoxacademy.TribesBackend;

import static org.assertj.core.api.Assertions.assertThat;
import com.greenfoxacademy.TribesBackend.controllers.HomeController;
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
    private UserService userService;

    @Test
    public void contexLoads() throws InstanceNotFoundException {
        assertThat(homeController).isNotNull();
    }

    @Test
    public void serviceLoads() throws InstanceNotFoundException {
        assertThat(userService).isNotNull();
    }
    
}
