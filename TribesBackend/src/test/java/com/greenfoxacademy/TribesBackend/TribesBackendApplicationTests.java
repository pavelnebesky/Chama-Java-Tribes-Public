package com.greenfoxacademy.TribesBackend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import com.greenfoxacademy.TribesBackend.controllers.HomeController;
import com.greenfoxacademy.TribesBackend.controllers.UserController;
import com.greenfoxacademy.TribesBackend.controllers.KingdomController;
import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import com.greenfoxacademy.TribesBackend.services.KingdomService;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletRequest;

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

    @Test
    public void controllerLoads() throws InstanceNotFoundException {
        assertThat(userController).isNotNull();
    }
    
    @Test
    public void doesEmailValidatorWork() throws Exception{
        assertEquals(userService.isEmailValid("jajenc@seznam.cz"), true);
        assertEquals(userService.isEmailValid("blablafuk@smrdim,prd"), false);
    }
}
