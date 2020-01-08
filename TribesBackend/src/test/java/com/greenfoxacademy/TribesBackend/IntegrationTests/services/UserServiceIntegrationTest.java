package com.greenfoxacademy.TribesBackend.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userservice;

    @Before
    public void SetUp() {
        User user = new User();
        user.setEmail("bloblo@cactus.cz");

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "bloblo@cactus.cz";
        User found = userservice.findByEmail(email);

        assertThat(found.getEmail()).isEqualTo(email);
    }
}
