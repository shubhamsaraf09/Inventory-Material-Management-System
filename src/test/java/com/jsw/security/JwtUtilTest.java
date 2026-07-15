package com.jsw.security;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
public class JwtUtilTest {
    private JwtUtil jwtUtil;

    private UserDetails testUser;

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil();

        testUser = new User("inventory_clerk", "password123" , new ArrayList<>());
    }

    @Test
    void shouldGenerateTokenAndExtractUsername(){
        String token = jwtUtil.generateToken(testUser.getUsername());

        String extracrtedUsername = jwtUtil.extractUsername(token);

        assertNotNull(token,"The generated token should not be null");
        assertEquals("inventory_clerk", extracrtedUsername, "The extracted username must match the original");
    }

    @Test
    void shouldValidateCorrectToken(){
        String token = jwtUtil.generateToken(testUser.getUsername());

        boolean isValid = jwtUtil.validateToken(token , testUser);

        assertTrue(isValid , "Token should be perfectlyvalid for the user who created it");
    }

    @Test
    void shouldInvalidateTokenForWringUSer(){
        String token = jwtUtil.generateToken(testUser.getUsername());

        UserDetails hackerUser = new User("hacker_guy", "password123",new ArrayList<>());

        boolean isValid = jwtUtil.validateToken(token, hackerUser);

        assertFalse(isValid,"Token must be invalid if the username do not match");
    }
}