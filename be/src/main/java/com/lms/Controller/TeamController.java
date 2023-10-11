package com.lms.controller;

import com.lms.dto.Team;
import com.lms.dto.TeamLead;
import com.lms.dto.projection.ManagerProjection;
import com.lms.dto.projection.TeamProjection;
import com.lms.models.User;
import com.lms.service.TeamService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TeamController {
    private final TeamService teamServiceImpl;
    private final ControllerUtils controllerUtils;

    @Autowired
    public TeamController(TeamService teamServiceImpl, ControllerUtils controllerUtils) {
        this.teamServiceImpl = teamServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping("/team")
    public ResponseEntity<Page<TeamProjection>> getAllTeams(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<TeamProjection> teams = teamServiceImpl.getAllTeams(sorted);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<TeamProjection> getTeamById(@PathVariable("id") Long id) {
        Optional<com.lms.models.Team> teamOptional = teamServiceImpl.findTeamById(id);
        if (teamOptional.isPresent()) {
            TeamProjection projection = ProjectionMapper.mapToTeamProjection(teamOptional.get());
            return ResponseEntity.ok(projection);
        }
        throw new NullPointerException("Team with id " + id + " does not exists");
    }

    @GetMapping("/team/manager")
    public ResponseEntity<List<ManagerProjection>> getTeamManagers() {
        List<ManagerProjection> managers = teamServiceImpl.getTeamManagers();
        return ResponseEntity.ok(managers);
    }

    @PostMapping("/team")
    @Operation(summary = "Create team. Require userList: int array")
    public ResponseEntity<TeamProjection> createTeam(@RequestBody Team team) {

        if (!controllerUtils.validateRequestedUser(team.getRequestedByEmail())) {
            throw new NullPointerException("Email " + team.getRequestedByEmail() + " not found");
        }
        if (!controllerUtils.validateRequestedUser(team.getManagerId())) {
            throw new NullPointerException("Manager with id " + team.getManagerId() + " does not exists");
        }
        TeamProjection newTeam = teamServiceImpl.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTeam);
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<TeamProjection> updateTeam(@PathVariable("id") Long id, @RequestBody Team team) {
        Optional<com.lms.models.Team> teamOptional = teamServiceImpl.findTeamById(id);
        if (teamOptional.isEmpty()) {
            throw new NullPointerException("Team with id " + id + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(team.getRequestedByEmail())) {
            throw new NullPointerException("Email " + team.getRequestedByEmail() + " does not exists");
        }
        team.setId(id);
        TeamProjection updateTeam = teamServiceImpl.updateTeam(team);
        return ResponseEntity.ok(updateTeam);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        Optional<com.lms.models.Team> teamOptional = teamServiceImpl.findTeamById(id);
        if (teamOptional.isEmpty()) {
            throw new NullPointerException("Team with id " + id + " does not exists");
        }
        teamServiceImpl.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
