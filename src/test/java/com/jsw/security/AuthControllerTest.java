package com.jsw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsw.controller.AuthController;
import com.jsw.dto.AuthRequestDto;
import com.jsw.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ✅ Create directly — no need to autowire
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private AppUserRepository appUserRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturn200ok_WhenLoginIsSuccessful() throws Exception {
        AuthRequestDto loginRequest = new AuthRequestDto();
        loginRequest.setUsername("manager2");
        loginRequest.setPassword("1234");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        "manager2", null, List.of()
                ));

        when(jwtUtil.generateToken("manager2")).thenReturn("fake.jwt.token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
}