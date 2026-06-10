package com.smartlogix.security;

import com.smartlogix.model.Role;
import com.smartlogix.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        // Need a key of at least 256 bits (32 bytes) for HMAC-SHA256
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000 * 60 * 24); // 24 minutes

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .role(Role.USER)
                .build();
    }

    @Test
    void generateToken_Success() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_Success() {
        String token = jwtService.generateToken(user);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void isTokenValid_ReturnsTrue() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_ReturnsFalse_WhenUsernameDiffers() {
        String token = jwtService.generateToken(user);
        User differentUser = User.builder().username("different").build();
        assertFalse(jwtService.isTokenValid(token, differentUser));
    }

    @Test
    void generateToken_WithExtraClaims_Success() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        
        String token = jwtService.generateToken(extraClaims, user);
        assertNotNull(token);
        
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }
}
