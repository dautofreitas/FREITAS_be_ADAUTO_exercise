package com.ecore.roles.service.impl;

import com.ecore.roles.client.TeamsClient;
import com.ecore.roles.client.model.Team;
import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.service.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamsServiceImpl implements TeamsService {

    private final TeamsClient teamsClient;

    @Autowired
    public TeamsServiceImpl(TeamsClient teamsClient) {
        this.teamsClient = teamsClient;
    }

    public Team getTeam(UUID id) {
        return Optional.ofNullable(teamsClient.getTeam(id).getBody())
                .orElseThrow(() -> new ResourceNotFoundException(Team.class, id));
    }

    public List<Team> getTeams() {
        return teamsClient.getTeams().getBody();
    }
}
