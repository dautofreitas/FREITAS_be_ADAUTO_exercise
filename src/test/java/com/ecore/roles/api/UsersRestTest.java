package com.ecore.roles.api;

import com.ecore.roles.client.model.User;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.ecore.roles.utils.MockUtils.*;
import static com.ecore.roles.utils.RestAssuredHelper.*;
import static com.ecore.roles.utils.TestData.GIANNI_USER;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersRestTest {
    @LocalServerPort
    private int port;
    private MockRestServiceServer mockServer;
    private final RestTemplate restTemplate;

    @Autowired
    public UsersRestTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    public void shouldGetUserById() {
        User expectedUser = GIANNI_USER();
        mockGetUserById(mockServer, expectedUser.getId(), expectedUser);

        getUser(expectedUser.getId())
                .statusCode(200)
                .body("id", equalTo(expectedUser.getId().toString()));

    }

    @Test
    public void shouldFailGetUserByIdWhenDoesNotExist() {
        User expectedUser = GIANNI_USER();
        mockGetUserById(mockServer, expectedUser.getId(), null);

        getUser(expectedUser.getId())
                .validate(404, format("User %s not found", expectedUser.getId()));

    }

    @Test
    public void shouldGetUsers() {
        User expectedUser = GIANNI_USER();
        mockGetUsers(mockServer, List.of(expectedUser));

        UserDto[] UsersDto = getUsers()
                .statusCode(200)
                .extract().as(UserDto[].class);

        assertThat(UsersDto.length).isEqualTo(1);
        assertThat(UsersDto[0].getId()).isNotNull();
        assertThat(UsersDto[0]).isEqualTo(UserDto.fromModel(expectedUser));

    }
}
