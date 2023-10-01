package com.example.lms.Service;

import com.example.lms.DTO.TeamDTO;
import com.example.lms.Models.Team;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<Team> getAllTeams();

    Optional<Team> findTeamById(Long id);

    Team findTeamByName(String teamName);

    Team createTeam(TeamDTO team);

    Team updateTeam(TeamDTO team);

    void deleteTeam(Long id);
}
