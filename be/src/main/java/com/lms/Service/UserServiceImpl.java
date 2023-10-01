package com.example.lms.Service;

import com.example.lms.Models.Team;
import com.example.lms.Models.User;
import com.example.lms.DTO.UserDTO;
import com.example.lms.Models.UserTeam;
import com.example.lms.Repository.TeamRepository;
import com.example.lms.Repository.UserRepository;
import com.example.lms.Repository.UserTeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository, UserTeamRepository userTeamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.userTeamRepository = userTeamRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User createUser(UserDTO user) {
        ModelMapper modelMapper = new ModelMapper();
        User userEntity = modelMapper.map(user, User.class);
        User userSaved = userRepository.save(userEntity);
        Team team = teamRepository.findTeamByteamName(user.getTeam());
        if (team != null) {
            UserTeam userTeam = new UserTeam(userEntity, team);
            userTeamRepository.save(userTeam);
        }
        return userSaved;
    }

//    @Override
//    public List<User> getUserByTeam(String teamName) {
//        return userRepository.getUserByTeam(teamName);
//    }

//    @Override
//    public List<User> getUserByTeamLead(String teamLead) {
//        List<User> users = new ArrayList<>();
//        Team team = teamRepository.findTeamByTeamLead(teamLead);
//        if (team != null) {
//            List<UserTeam> userTeams = team.getUserTeams();
//            for (UserTeam userTeam : userTeams) {
//                users.add(userTeam.getUser());
//            }
//            return users;
//        } else {
//            throw new IllegalArgumentException();
//        }
//    };

    @Override
    public List<User> getUserBetweenDates(Date startDate, Date endDate) {
        return userRepository.getUserByCreatedDateBetween(startDate, endDate);
    }

    @Override
    public User updateUser(UserDTO user) {
        ModelMapper modelMapper = new ModelMapper();
        User userEntity = modelMapper.map(user, User.class);
        return userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
