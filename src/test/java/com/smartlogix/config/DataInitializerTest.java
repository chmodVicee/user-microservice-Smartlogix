package com.smartlogix.config;

import com.smartlogix.model.User;
import com.smartlogix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(dataInitializer, "adminUsername", "admin");
        ReflectionTestUtils.setField(dataInitializer, "adminEmail", "admin@example.com");
        ReflectionTestUtils.setField(dataInitializer, "adminPassword", "password");
    }

    @Test
    void run_AdminDoesNotExist() {
        ApplicationArguments args = mock(ApplicationArguments.class);

        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        dataInitializer.run(args);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("admin", savedUser.getUsername());
        assertEquals("admin@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("ADMIN", savedUser.getRole().name());
    }

    @Test
    void run_AdminAlreadyExists() {
        ApplicationArguments args = mock(ApplicationArguments.class);

        when(userRepository.existsByUsername("admin")).thenReturn(true);

        dataInitializer.run(args);

        verify(userRepository, never()).save(any(User.class));
    }
}
