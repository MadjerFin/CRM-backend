package com.wtc.service;

import com.wtc.dto.request.*;
import com.wtc.dto.response.AuthResponse;
import com.wtc.entity.WtcUser;
import com.wtc.repository.WtcUserRepository;
import com.wtc.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WtcUserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        WtcUser user = userRepo.findByEmail(req.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        String token = jwtUtil.generateToken(user, user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getId(), user.getName());
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email já cadastrado");
        WtcUser user = WtcUser.builder()
            .name(req.getName())
            .email(req.getEmail())
            .password(encoder.encode(req.getPassword()))
            .role(req.getRole())
            .active(true)
            .build();
        userRepo.save(user);
        String token = jwtUtil.generateToken(user, user.getRole().name());
        return new AuthResponse(token, user.getRole().name(), user.getId(), user.getName());
    }

    public void updateFcmToken(String userId, String fcmToken) {
        WtcUser user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        user.setFcmToken(fcmToken);
        userRepo.save(user);
    }
}
