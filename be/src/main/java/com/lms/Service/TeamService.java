package com.lms.service;

import com.lms.dto.Team;
import com.lms.dto.projection.ManagerProjection;
import com.lms.dto.projection.TeamProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    //    List<Team> getAllTeams();
    Page<TeamProjection> getAllTeams(Pageable pageable);

    Optional<com.lms.models.Team> findTeamById(Long id);

    List<ManagerProjection> getTeamManagers();

    TeamProjection findTeamProjectionByName(String teamName);

    TeamProjection createTeam(Team team);

    TeamProjection updateTeam(Team team);

    void deleteTeam(Long id);

    Page<UserProjection> getUsersByTeam(Long id, Pageable pageable);
}
