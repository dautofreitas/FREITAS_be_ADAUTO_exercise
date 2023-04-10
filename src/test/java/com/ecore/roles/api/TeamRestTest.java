package com.ecore.roles.api;

import com.ecore.roles.client.model.Team;
import com.ecore.roles.utils.RestAssuredHelper;
import com.ecore.roles.web.dto.TeamDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.ecore.roles.utils.MockUtils.mockGetTeamById;
import static com.ecore.roles.utils.MockUtils.mockGetTeams;
import static com.ecore.roles.utils.RestAssuredHelper.getTeam;
import static com.ecore.roles.utils.RestAssuredHelper.getTeams;
import static com.ecore.roles.utils.TestData.ORDINARY_CORAL_LYNX_TEAM;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamRestTest {
    @LocalServerPort
    private int port;
    private MockRestServiceServer mockServer;
    private final RestTemplate restTemplate;

    @Autowired
    public TeamRestTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        RestAssuredHelper.setUp(port);
    }

    @Test
    public void shouldGetTeamById() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeamById(mockServer, expectedTeam.getId(), expectedTeam);

        getTeam(expectedTeam.getId())
                .statusCode(200)
                .body("name", equalTo(expectedTeam.getName()));

    }

    @Test
    public void shouldFailGetTeamByIdWhenTeamDoesNotExist() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeamById(mockServer, expectedTeam.getId(), null);

        getTeam(expectedTeam.getId())
                .validate(404, format("Team %s not found", expectedTeam.getId()));

    }

    @Test
    public void shouldGetTeams() {
        Team expectedTeam = ORDINARY_CORAL_LYNX_TEAM();
        mockGetTeams(mockServer, List.of(expectedTeam));

        TeamDto[] teamsDto = getTeams()
                .statusCode(200)
                .extract().as(TeamDto[].class);

        assertThat(teamsDto.length).isEqualTo(1);
        assertThat(teamsDto[0].getId()).isNotNull();
        assertThat(teamsDto[0]).isEqualTo(TeamDto.fromModel(expectedTeam));

    }

}
