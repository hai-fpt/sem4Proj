package com.lms.Service;

import com.lms.DTO.TeamDTO;
import com.lms.Models.Team;
import com.lms.Models.User;
import com.lms.Models.UserTeam;
import com.lms.Repository.TeamRepository;
import com.lms.Repository.UserRepository;
import com.lms.Repository.UserTeamRepository;
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
                    map().setManager(manager);
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
