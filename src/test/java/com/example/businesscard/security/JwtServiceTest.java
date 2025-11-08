package com.example.businesscard.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("abcdefghijklmnopqrstuvwxyz123456", 3600000);
        userDetails = User.withUsername("testuser").password("password").authorities("ROLE_USER").build();
    }

    @Test
    void generateTokenShouldReturnNonEmptyToken() {
        String token = jwtService.generateToken(new TestingAuthenticationToken(userDetails, null));

        assertThat(token).isNotBlank();
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(new TestingAuthenticationToken(userDetails, null));

        assertThat(jwtService.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void validateTokenShouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(new TestingAuthenticationToken(userDetails, null));
        UserDetails otherUser = User.withUsername("other").password("password").authorities("ROLE_USER").build();

        assertThat(jwtService.validateToken(token, otherUser)).isFalse();
    }

    @Test
    void extractUsernameShouldReturnUsername() {
        String token = jwtService.generateToken(new TestingAuthenticationToken(userDetails, null));

        assertThat(jwtService.extractUsername(token)).isEqualTo("testuser");
    }

    @Test
    void parseInvalidTokenShouldThrow() {
        assertThatThrownBy(() -> jwtService.extractUsername("invalid.token"))
            .isInstanceOf(Exception.class);
    }
}
