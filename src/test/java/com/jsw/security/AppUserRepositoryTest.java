package com.jsw.security;

import com.jsw.entity.AppUser;
import com.jsw.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void shouldFinduserByUsername_WhenUserIsSaved(){
        AppUser newUser = new AppUser(null, "test_admin", "securePass11", "ROLE_ADMIN");
        appUserRepository.save(newUser);

        Optional<AppUser> foundUser = appUserRepository.findByUsername("test_admin");

        assertTrue(foundUser.isPresent(), "User should be found in the database!");
        assertEquals("test_admin", foundUser.get().getUsername());
        assertEquals("ROLE_ADMIN",foundUser.get().getRole());

        assertNotNull(foundUser.get().getAppUserid(),"Database should have generated an ID");
    }

    @Test
    void shouldRetunEmpty_WhenUsernameDoesNotExist(){
        Optional<AppUser> foundUser = appUserRepository.findByUsername("ghost_user");

        assertTrue(foundUser.isEmpty(),"Ghost user should not exist in the database!");
    }

}
