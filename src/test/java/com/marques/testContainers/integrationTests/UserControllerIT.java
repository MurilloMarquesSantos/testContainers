package com.marques.testContainers.integrationTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marques.testContainers.domain.Roles;
import com.marques.testContainers.repository.RolesRepository;
import com.marques.testContainers.request.UserRequest;
import com.marques.testContainers.response.UserResponse;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "server.port=8888")
class UserControllerIT extends AbstractIntegration {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static UserRequest userRequest;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private RolesRepository roleRepository;

    @BeforeEach
    void setUp() {

        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/home/create")
                .setPort(8888)
                .build();

        userRequest = new UserRequest("Murillo", "Marques");

        if(!roleRepository.existsByName("ADMIN")){
            roleRepository.save(new Roles(null, "ADMIN"));
        }
    }


    private String generateAdminToken(){
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("test-containers")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .subject("admin")
                .claim("scope", "ADMIN")
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Test
    void create_ReturnsUser_WhenSuccessful() throws JsonProcessingException {


        String returnedContent = RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .body(userRequest)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        UserResponse userResponse = mapper.readValue(returnedContent, UserResponse.class);

        assertThat(userResponse.getUsername()).isEqualTo(userRequest.getUsername());
    }

    @Test
    void list_ReturnsListOfUser_WhenSuccessful() throws JsonProcessingException {
        String adminToken = generateAdminToken();

        RestAssured.given()
                .spec(specification)
                .contentType("application/json")
                .body(new UserRequest("Maria", "Maria"))
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        specification = new RequestSpecBuilder()
                .setBasePath("/home/list")
                .setPort(8888)
                .build();

        String returnedContent = RestAssured.given()
                .spec(specification)
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();


        List<UserResponse> userList = Arrays.asList(mapper.readValue(returnedContent, UserResponse[].class));

        assertThat(userList).isNotNull().hasSize(1);

        assertThat(userList.getFirst().getUsername()).isEqualTo("Maria");
    }
}
