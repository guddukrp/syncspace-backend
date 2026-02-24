package com.syncspace.service.impl;

import com.syncspace.dto.auth.AuthResponse;
import com.syncspace.dto.auth.AuthUserResponse;
import com.syncspace.dto.auth.LoginRequest;
import com.syncspace.dto.auth.RegisterRequest;
import com.syncspace.entity.User;
import com.syncspace.entity.UserRole;
import com.syncspace.exception.BadRequestException;
import com.syncspace.exception.UnauthorizedException;
import com.syncspace.repository.UserRepository;
import com.syncspace.security.JwtService;
import com.syncspace.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        userRepository.findByEmailAndDeletedFalse(request.getEmail().trim().toLowerCase())
                .ifPresent(existing -> {
                    throw new BadRequestException("Email is already registered");
                });

        User user = new User();
        user.setDisplayName(request.getDisplayName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        log.info("User registered: {}", saved.getEmail());
        return AuthResponse.builder()
                .token(token)
                .user(toUserResponse(saved))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        log.info("User login: {}", user.getEmail());
        return AuthResponse.builder()
                .token(token)
                .user(toUserResponse(user))
                .build();
    }

    private AuthUserResponse toUserResponse(User user) {
        return AuthUserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role("ROLE_" + user.getRole().name())
                .build();
    }
}