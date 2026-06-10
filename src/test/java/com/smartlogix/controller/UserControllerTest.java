package com.smartlogix.controller;

import com.smartlogix.dto.UserResponse;
import com.smartlogix.exception.GlobalExceptionHandler;
import com.smartlogix.model.Role;
import com.smartlogix.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getProfile_Success() throws Exception {
        UserResponse response = new UserResponse(1L, "testuser", "test@test.com", Role.USER);

        when(userService.getProfile()).thenReturn(response);

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<UserResponse> responses = List.of(
                new UserResponse(1L, "testuser", "test@test.com", Role.USER),
                new UserResponse(2L, "admin", "admin@test.com", Role.ADMIN)
        );

        when(userService.getAllUsers()).thenReturn(responses);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].username").value("admin"));
    }
}
