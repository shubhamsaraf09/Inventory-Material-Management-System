package com.jsw.security;

import com.jsw.entity.AppUser;
import com.jsw.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void shouldLoadUser_WhenUserExistsInDatabase() {
        AppUser mockUser = new AppUser();
        mockUser.setUsername("manager2");
        mockUser.setPassword("1234");

        mockUser.setRole("ROLE_MANAGER");

        when(appUserRepository.findByUsername("manager2")).thenReturn(Optional.of(mockUser));

        UserDetails result = userDetailsService.loadUserByUsername("manager2");

        assertNotNull(result);
        assertEquals("manager2",result.getUsername());
        assertEquals("1234",result.getPassword());

        verify(appUserRepository, times(1)).findByUsername("manager2");
    }

    @Test
    void shouldThrowException_WhenUserDoesNotExist() {
        when(appUserRepository.findByUsername("ghost_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, ()->{
            userDetailsService.loadUserByUsername("ghost_user");
        });
    }
}