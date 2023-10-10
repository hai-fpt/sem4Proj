package com.lms.service;

import com.lms.dto.TeamDTO;
import com.lms.dto.TeamLeadDTO;
import com.lms.models.Team;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    //    List<Team> getAllTeams();
    Page<Team> getAllTeams(Pageable pageable);

    Optional<Team> findTeamById(Long id);

    List<User> getTeamManagers();

    Team findTeamByName(String teamName);

    Team createTeam(TeamDTO team);

    Team updateTeam(TeamDTO team);

    void deleteTeam(Long id);
}
