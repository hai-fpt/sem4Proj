package com.example.lms.Service;

import com.example.lms.DTO.TeamDTO;
import com.example.lms.Models.Team;
import com.example.lms.Models.User;
import com.example.lms.Models.UserTeam;
import com.example.lms.Repository.TeamRepository;
import com.example.lms.Repository.UserRepository;
import com.example.lms.Repository.UserTeamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    @Override
    public Optional<Team> findTeamById(Long id) {
        return teamRepository.findById(id);
    }

//    @Override
//    public List<String> getTeamLeadByUser(Long id) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            List<UserTeam> userTeams = user.getUserTeams();
//            List<String> teamLeads = new ArrayList<>();
//            if (!userTeams.isEmpty()) {
//                for (UserTeam userTeam : userTeams) {
//                    teamLeads.add(userTeam.getTeam().getTeamLead());
//                }
//                return teamLeads;
//            }
//            return Collections.emptyList();
//        }
//        return Collections.emptyList();
//    }
    @Override
    public Team findTeamByName(String name) {
        return teamRepository.findTeamByteamName(name);
    }

    //    @Override
//    public Team createTeam(TeamDTO team) {
//        ModelMapper modelMapper = new ModelMapper();
//        Team teamEntity = modelMapper.map(team, Team.class);
//        return teamRepository.save(teamEntity);
//    }
    @Override
    public Team createTeam(TeamDTO teamDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Optional<User> user = userRepository.findById(teamDTO.getManagerId());
        if (user.isPresent()) {
            User manager = user.get();
            modelMapper.addMappings(new PropertyMap<TeamDTO, Team>() {
                @Override
                protected void configure() {
                    map().setUser(manager);
                }
            });
            Team team = modelMapper.map(teamDTO, Team.class);
            teamRepository.save(team);
            List<Long> userList = teamDTO.getUserList();
            for (Long userId : userList) {
                Optional<User> userEmployee = userRepository.findById(userId);
                if (userEmployee.isPresent()) {
                    UserTeam userTeam = new UserTeam(userEmployee.get(), team);
                    userTeamRepository.save(userTeam);
                }
            }
            return team;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found");
    }
    @Override
    public Team updateTeam(TeamDTO team) {
        ModelMapper modelMapper = new ModelMapper();
        Team teamEntity = modelMapper.map(team, Team.class);
        return teamRepository.save(teamEntity);
    }
    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }
}
