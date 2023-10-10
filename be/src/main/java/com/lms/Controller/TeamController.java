package com.lms.controller;

import com.lms.dto.TeamDTO;
import com.lms.dto.TeamLeadDTO;
import com.lms.models.Team;
import com.lms.models.User;
import com.lms.service.TeamService;
import com.lms.service.TeamServiceImpl;
import com.lms.utils.ControllerUtils;
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
    public ResponseEntity<Page<Team>> getAllTeams(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<Team> teams = teamServiceImpl.getAllTeams(sorted);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Long id) {
        Optional<Team> teamOptional = teamServiceImpl.findTeamById(id);
        Team team = teamOptional.orElseThrow(() -> new NullPointerException("Team does not exists"));
        return ResponseEntity.ok(team);
    }

    @GetMapping("/team/manager")
    public ResponseEntity<List<TeamLeadDTO>> getTeamManagers() {
        List<User> managers = teamServiceImpl.getTeamManagers();
        List<TeamLeadDTO> teamLeadDTOS = new ArrayList<>();
        for (User manager : managers) {
            TeamLeadDTO teamLeadDTO = new TeamLeadDTO(manager.getId(), manager.getName());
            teamLeadDTOS.add(teamLeadDTO);
        }
        return ResponseEntity.ok(teamLeadDTOS);
    }

    @PostMapping("/team")
    @Operation(summary = "Create team. Require userList: int array")
    public ResponseEntity<Team> createTeam(@RequestBody TeamDTO team) {

        if (!controllerUtils.validateRequestedUser(team.getRequestedByEmail())) {
            throw new NullPointerException("Email " + team.getRequestedByEmail() + " not found");
        }
        if (!controllerUtils.validateRequestedUser(team.getManagerId())) {
            throw new NullPointerException("Manager with id " + team.getManagerId() + " does not exists");
        }
        Team newTeam = teamServiceImpl.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTeam);
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable("id") Long id, @RequestBody TeamDTO team) {
        Optional<Team> teamOptional = teamServiceImpl.findTeamById(id);
        if (teamOptional.isEmpty()) {
            throw new NullPointerException("Team with id " + id + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(team.getRequestedByEmail())) {
            throw new NullPointerException("Email " + team.getRequestedByEmail() + " does not exists");
        }
        team.setId(id);
        Team updateTeam = teamServiceImpl.updateTeam(team);
        return ResponseEntity.ok(updateTeam);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        Optional<Team> teamOptional = teamServiceImpl.findTeamById(id);
        if (teamOptional.isEmpty()) {
            throw new NullPointerException("Team with id " + id + " does not exists");
        }
        teamServiceImpl.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
