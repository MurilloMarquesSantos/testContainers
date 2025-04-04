package com.marques.testContainers.repository;

import com.marques.testContainers.domain.Roles;
import com.marques.testContainers.domain.User;
import com.marques.testContainers.integrationTests.AbstractIntegration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // habilitando ordem nos testes
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest extends AbstractIntegration {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("Murillo")
                .password("Murillo")
                .roles(Set.of(new Roles(null, "ADMIN"))) //criando um user para ser salvo
                .build();
    }

    @AfterAll
    void cleanDatabase(){
        userRepository.deleteAll();
    }
    @Test
    @Order(1) // importante que esse seja o primeiro para que os testes não falhem
    void save_ReturnsUser_WhenSuccessful(){

        User savedUser = userRepository.save(user);

        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
    }
    @Test
    void findAll_ReturnsListOfUser_WhenSuccessful(){

        List<User> userList = userRepository.findAll();

        assertThat(userList).isNotEmpty().hasSize(1);

    }
    @Test
    void findByUsername_ReturnsOptionalOfUser_WhenSuccessful(){

        Optional<User> userOpt = userRepository.findByUsername("Murillo");

        assertThat(userOpt).isPresent();

        assertThat(userOpt.get().getUsername()).isEqualTo(user.getUsername());

    }
    @Test
    void existsByUsername_ReturnsTrue_WhenSuccessful(){

        boolean exists = userRepository.existsByUsername("Murillo");

        assertThat(exists).isTrue();
    }
}