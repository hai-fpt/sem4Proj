package com.lms.Controller;

import com.lms.DTO.TeamDTO;
import com.lms.Models.Team;
import com.lms.Service.TeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TeamController {
    private final TeamServiceImpl teamServiceImpl;

    @Autowired
    public TeamController(TeamServiceImpl teamServiceImpl) {
        this.teamServiceImpl = teamServiceImpl;
    }

    @GetMapping("/team")
    public ResponseEntity<Page<Team>> getAllTeams(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Page<Team> teams = teamServiceImpl.getAllTeams(pageable);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable("id") Long id) {
        Optional<Team> team = teamServiceImpl.findTeamById(id);
        return team.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/team/lead/{userid}")
//    public ResponseEntity<List<String>> getTeamLeadByUserId(@PathVariable("userid") Long id) {
//        List<String> teamLead = teamServiceImpl.getTeamLeadByUser(id);
//        return ResponseEntity.ok(teamLead);
//    }

    @PostMapping("/team")
    public ResponseEntity<Team> createTeam(@RequestBody TeamDTO team) {
        Team newTeam = teamServiceImpl.createTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTeam);
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable("id") Long id, @RequestBody TeamDTO team) {
        team.setId(id);
        Team updateTeam = teamServiceImpl.updateTeam(team);
        return ResponseEntity.ok(updateTeam);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable("id") Long id) {
        teamServiceImpl.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
