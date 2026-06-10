package com.smartlogix.repository;

import com.smartlogix.model.Role;
import com.smartlogix.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(Role.USER)
                .build();
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test findByUsername - Success")
    void testFindByUsername_Success() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Test findByUsername - Not Found")
    void testFindByUsername_NotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("unknownuser");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Test existsByUsername - Exists")
    void testExistsByUsername_Exists() {
        // Act
        boolean exists = userRepository.existsByUsername("testuser");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test existsByUsername - Does Not Exist")
    void testExistsByUsername_DoesNotExist() {
        // Act
        boolean exists = userRepository.existsByUsername("unknownuser");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Test existsByEmail - Exists")
    void testExistsByEmail_Exists() {
        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Test existsByEmail - Does Not Exist")
    void testExistsByEmail_DoesNotExist() {
        // Act
        boolean exists = userRepository.existsByEmail("unknown@example.com");

        // Assert
        assertThat(exists).isFalse();
    }
}
