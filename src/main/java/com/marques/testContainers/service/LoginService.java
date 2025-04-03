package com.marques.testContainers.service;

import com.marques.testContainers.domain.User;
import com.marques.testContainers.repository.UserRepository;
import com.marques.testContainers.request.LoginRequest;
import com.marques.testContainers.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) throws BadRequestException {
        Optional<User> user = userRepository.findByName(request.getName());
        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new BadRequestException("No user found!");
        }
        return tokenService.generateToken(user.get());
    }
}
