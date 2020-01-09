package com.greenfoxacademy.TribesBackend.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Test
    public void whenReceivedValidMail_thenReturnGeneratedKingdomName() {
        String validMail = "koko@jezkokosu.com";
        String newKingdomName = userService.generateKingdomNameByEmail(validMail);

        assertThat(newKingdomName).isEqualTo("koko's kingdom");
    }

    @Test
    public void whenReceivedInvalidMail_thenReturnNull() {
        String invalidMail = "bubu_baba_bebebu";
        String newKingdomName = userService.generateKingdomNameByEmail(invalidMail);

        assertThat(newKingdomName).isNull();
    }

    @Test
    public void doesEmailValidatorWork() {
        assertEquals(userService.isEmailValid("jajenc@seznam.cz"), true);
        assertEquals(userService.isEmailValid("blablafuk@smrdim,prd"), false);
    }
}
