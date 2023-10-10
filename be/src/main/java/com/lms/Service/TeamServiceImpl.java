package com.lms.service;

import com.lms.dto.TeamDTO;
import com.lms.dto.TeamLeadDTO;
import com.lms.models.Team;
import com.lms.models.User;
import com.lms.models.UserTeam;
import com.lms.repository.TeamRepository;
import com.lms.repository.UserRepository;
import com.lms.repository.UserTeamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, UserRepository userRepository, UserTeamRepository userTeamRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.userTeamRepository = userTeamRepository;
    }

    @Override
    public Page<Team> getAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }
    @Override
    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findTeamByteamName(name);
    }

    @Override
    public Team createTeam(TeamDTO teamDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<User> user = userRepository.findById(teamDTO.getManagerId());
        User manager = user.get();
        modelMapper.addMappings(new PropertyMap<TeamDTO, Team>() {
            @Override
            protected void configure() {
                map().setManager(manager);
                map().setUpdated_by(teamDTO.getRequestedByEmail());
            }
        });
        Team team = modelMapper.map(teamDTO, Team.class);
        teamRepository.save(team);

        List<Long> userList = teamDTO.getUserList();
        List<User> users = userRepository.findAllById(userList);
        if (userList.size() != users.size()) {
            throw new NullPointerException("Missing user");
        }

        List<UserTeam> userTeams = new ArrayList<>();
        for (User user1 : users) {
            UserTeam userTeam = new UserTeam(user1, team);
            userTeams.add(userTeam);
        }

        userTeamRepository.saveAll(userTeams);
        return team;
    }

    @Override
    public List<User> getTeamManagers() {
        List<Team> teams = teamRepository.findAll();
        if (teams.isEmpty()) {
            throw new NullPointerException("No team managers available");
        }
        List<User> managers = new ArrayList<>();
        for (Team team : teams) {
            if (team.getManager() != null)
                managers.add(team.getManager());
        }
        return managers;
    }
    @Override
    public Team updateTeam(TeamDTO team) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<TeamDTO, Team>() {
            @Override
            protected void configure() {
                map().setUpdated_by(team.getRequestedByEmail());
            }
        });
        Team teamEntity = modelMapper.map(team, Team.class);
        return teamRepository.save(teamEntity);
    }
    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}
