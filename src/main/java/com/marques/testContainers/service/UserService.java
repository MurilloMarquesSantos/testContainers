package com.marques.testContainers.service;

import com.marques.testContainers.domain.User;
import com.marques.testContainers.repository.UserRepository;
import com.marques.testContainers.request.UserRequest;
import com.marques.testContainers.response.UserResponse;
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

    public UserResponse createNewUser(UserRequest userRequest) throws BadRequestException {
        if(userRepository.existsByUsername(userRequest.getUsername())){
           throw new BadRequestException("This user already Exists");
        }
        User user = User.builder().username(userRequest.getUsername())
                .password(encoder.encode(userRequest.getPassword()))
                .roles(Collections.singleton(rolesService.getRoleByName("ADMIN")))
                .build();
        userRepository.save(user);

        return UserResponse.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .build();
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getUsername(), user.getPassword()))
                .toList();

    }
}
