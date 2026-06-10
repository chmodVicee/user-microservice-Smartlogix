package com.smartlogix.service;

import com.smartlogix.dto.UserResponse;
import com.smartlogix.model.Role;
import com.smartlogix.model.User;
import com.smartlogix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();
    }

    @Test
    void getProfile_Success() {
        // Setup Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserResponse response = userService.getProfile();

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@test.com", response.getEmail());
    }

    @Test
    void getProfile_ThrowsUsernameNotFoundException() {
        // Setup Security Context
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("unknown");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getProfile());
    }

    @Test
    void getAllUsers_Success() {
        User user2 = User.builder()
                .id(2L)
                .username("adminuser")
                .email("admin@test.com")
                .role(Role.ADMIN)
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user, user2));

        List<UserResponse> responses = userService.getAllUsers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("testuser", responses.get(0).getUsername());
        assertEquals("adminuser", responses.get(1).getUsername());
    }
}
