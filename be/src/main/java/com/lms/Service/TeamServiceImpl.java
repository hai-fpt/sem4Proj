package com.lms.service;

import com.lms.dto.Team;
import com.lms.dto.projection.ManagerProjection;
import com.lms.dto.projection.TeamProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.models.User;
import com.lms.models.UserTeam;
import com.lms.repository.TeamRepository;
import com.lms.repository.UserRepository;
import com.lms.repository.UserTeamRepository;
import com.lms.utils.ProjectionMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.lms.utils.Constants.USER_NOT_EXISTS;

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
    public Page<TeamProjection> getAllTeams(Pageable pageable) {
        return teamRepository.findAllProjectedBy(pageable);
    }
    @Override
    public Optional<com.lms.models.Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public TeamProjection findTeamProjectionByName(String name) {
        return teamRepository.findTeamProjectionByTeamName(name);
    }

    @Override
    public TeamProjection createTeam(Team teamDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<User> user = userRepository.findById(teamDTO.getManager());
        User manager = user.get();
        modelMapper.addMappings(new PropertyMap<Team, com.lms.models.Team>() {
            @Override
            protected void configure() {
                map().setManager(manager);
            }
        });
        com.lms.models.Team team = modelMapper.map(teamDTO, com.lms.models.Team.class);
        team.setUpdatedBy(teamDTO.getUpdatedBy());
        teamRepository.save(team);
        TeamProjection projection = ProjectionMapper.mapToTeamProjection(team);

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
        return projection;
    }

    @Override
    public List<ManagerProjection> getTeamManagers() {
        List<com.lms.models.Team> teams = teamRepository.findAll();
        if (teams.isEmpty()) {
            throw new NullPointerException("No team managers available");
        }
        List<ManagerProjection> managers = new ArrayList<>();
        for (com.lms.models.Team team : teams) {
            if (team.getManager() != null)
                managers.add(ProjectionMapper.mapToManagerProjection(team.getManager()));
        }
        return managers;
    }
    @Override
    public TeamProjection updateTeam(Team team) {
        Optional<User> manager = userRepository.findById(team.getManager());
        if (manager.isEmpty()) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Team teamEntity = modelMapper.map(team, com.lms.models.Team.class);
        teamEntity.setManager(manager.get());
        teamEntity.setUpdatedBy(team.getUpdatedBy());
        teamEntity.setUpdatedDate(LocalDateTime.now());
        com.lms.models.Team updated = teamRepository.save(teamEntity);
        return ProjectionMapper.mapToTeamProjection(updated);
    }
    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public Page<UserProjection> getUsersByTeam(Long id, Pageable pageable) {
        return teamRepository.findUsersByTeam(id, pageable);
    }
}
