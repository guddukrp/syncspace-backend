package com.syncspace.service;

import com.syncspace.dto.auth.AuthResponse;
import com.syncspace.dto.auth.LoginRequest;
import com.syncspace.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}