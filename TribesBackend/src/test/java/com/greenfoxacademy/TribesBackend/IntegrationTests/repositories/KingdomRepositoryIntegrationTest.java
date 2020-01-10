/*
package com.greenfoxacademy.TribesBackend.IntegrationTests.repositories;

import com.greenfoxacademy.TribesBackend.models.*;
import com.greenfoxacademy.TribesBackend.repositories.KingdomRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class KingdomRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private KingdomRepository kingdomRepository;

    @Test
    public void whenFindByName_thenReturnKingdom() {
        Kingdom kingdom = new Kingdom();
        kingdom.setName("Testonia");

        entityManager.persistAndFlush(kingdom);

        Kingdom found = kingdomRepository.findByName(kingdom.getName());
        assertThat(found.getName()).isEqualTo(kingdom.getName());
    }

    @Test
    public void whenFindByUserId_thenReturnKingdom() {
        User user  = new User();
        Kingdom kingdom = new Kingdom();
        kingdom.setUserId(user.getId());
        user.setKingdom(kingdom);

        entityManager.persist(user);
        entityManager.persist(kingdom);
        entityManager.flush();

        Kingdom found = kingdomRepository.findByUserId(user.getId());
        assertThat(found.getId()).isEqualTo(kingdom.getId());
    }
}
*/
