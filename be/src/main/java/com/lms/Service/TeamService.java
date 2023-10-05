package com.lms.Service;

import com.lms.DTO.TeamDTO;
import com.lms.Models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TeamService {
    //    List<Team> getAllTeams();
    Page<Team> getAllTeams(Pageable pageable);

    Optional<Team> findTeamById(Long id);

    Team findTeamByName(String teamName);

    Team createTeam(TeamDTO team);

    Team updateTeam(TeamDTO team);

    void deleteTeam(Long id);
}
