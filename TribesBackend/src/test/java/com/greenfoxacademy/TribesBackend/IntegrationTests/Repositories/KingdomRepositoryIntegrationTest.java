package com.greenfoxacademy.TribesBackend.IntegrationTests.Repositories;

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
    private static TestEntityManager entityManager;
    @Autowired
    private KingdomRepository kingdomRepository;

    private static User user;
    private static Kingdom kingdom;

    @BeforeClass
    public static void SetUp(){
        kingdom = new Kingdom();
        user  = new User(1L, "test@test.com", "test",
                "Johnny test", kingdom, "ehfiweugfhiweg", true);
        kingdom = new Kingdom(1L, "Testonia", user, new ArrayList<Resource>(), new Location(),
                new ArrayList<Building>(), new ArrayList<Troop>());
        entityManager.persist(user);
        entityManager.persist(kingdom);
        entityManager.flush();
    }
    @Test
    public void whenFindByName_thenReturnKingdom() {
        Kingdom found = kingdomRepository.findByName(kingdom.getName());
        assertThat(found.getName()).isEqualTo(kingdom.getName());
    }

    @Test
    public void whenFindByUserId_thenReturnKingdom() {
        Kingdom found = kingdomRepository.findByUserId(1L);
        assertThat(found.getId()).isEqualTo(kingdom.getId());
    }
}
