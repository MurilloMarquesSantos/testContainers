package com.marques.testContainers.controller;

import com.marques.testContainers.request.LoginRequest;
import com.marques.testContainers.response.LoginResponse;
import com.marques.testContainers.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws BadRequestException {
        return new ResponseEntity<>(loginService.login(request), HttpStatus.OK);
    }
}
