package com.marques.testContainers.integrationTests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marques.testContainers.domain.Roles;
import com.marques.testContainers.repository.RolesRepository;
import com.marques.testContainers.request.LoginRequest;
import com.marques.testContainers.request.UserRequest;
import com.marques.testContainers.response.LoginResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
class LoginControllerIT extends AbstractIntegration {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static LoginRequest loginRequest;

    @Autowired
    private RolesRepository rolesRepository;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/home/create")
                .setPort(8888)
                .build();

        loginRequest = LoginRequest.builder().name("Murillo").password("Murillo").build();

        rolesRepository.save(new Roles(null, "ADMIN"));

        RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .body(new UserRequest("Murillo", "Murillo"))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();
    }

    @Test
    void login_ReturnsJWT_WhenSuccessful() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .setBasePath("/home/login")
                .setPort(8888)
                .build();

        String returnedContent = RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .body(loginRequest)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        LoginResponse loginResponse = mapper.readValue(returnedContent, LoginResponse.class);

        assertThat(loginResponse).isNotNull();

        assertThat(loginResponse.getExpiresIn()).isEqualTo(300L);

    }

}
