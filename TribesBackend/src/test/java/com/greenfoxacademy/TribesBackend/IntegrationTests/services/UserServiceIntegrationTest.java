package com.greenfoxacademy.TribesBackend.IntegrationTests.services;

import com.greenfoxacademy.TribesBackend.models.User;
import com.greenfoxacademy.TribesBackend.repositories.UserRepository;
import com.greenfoxacademy.TribesBackend.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        user.setUsername("bloblo@cactus.cz");

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "bloblo@cactus.cz";
        User found = userservice.findByEmail(email);

        assertThat(found.getUsername()).isEqualTo(email);
    }
}
