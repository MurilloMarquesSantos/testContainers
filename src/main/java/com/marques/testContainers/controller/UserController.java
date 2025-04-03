package com.marques.testContainers.controller;

import com.marques.testContainers.domain.User;
import com.marques.testContainers.request.UserRequest;
import com.marques.testContainers.response.UserResponse;
import com.marques.testContainers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) throws BadRequestException {
        return new ResponseEntity<>(userService.createNewUser(userRequest), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> listUsers(){
        return ResponseEntity.ok(userService.listAll());
    }

}
