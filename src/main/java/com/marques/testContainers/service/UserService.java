package com.marques.testContainers.service;

import com.marques.testContainers.domain.User;
import com.marques.testContainers.repository.UserRepository;
import com.marques.testContainers.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolesService rolesService;
    private final BCryptPasswordEncoder encoder;

    public User createNewUser(UserRequest userRequest) throws BadRequestException {
        User user = User.builder().name(userRequest.getName())
                .password(encoder.encode(userRequest.getPassword()))
                .roles(Collections.singleton(rolesService.getRoleByName("ADMIN")))
                .build();
        return userRepository.save(user);
    }

    public List<User> listAll() {
        return userRepository.findAll();
    }
}
