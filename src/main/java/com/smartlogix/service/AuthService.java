package com.smartlogix.service;

import com.smartlogix.dto.AuthResponse;
import com.smartlogix.dto.LoginRequest;
import com.smartlogix.dto.RegisterRequest;
import com.smartlogix.exception.UserAlreadyExistsException;
import com.smartlogix.model.Role;
import com.smartlogix.model.User;
import com.smartlogix.repository.UserRepository;
import com.smartlogix.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register (RegisterRequest request) {

        if (userRepository.existByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Ya se ha utilizado el username de: " + request.getUsername());
        }

        if (userRepository.existByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Ya se ha utilizado el email de: "+ request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
