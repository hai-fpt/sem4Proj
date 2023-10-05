package com.lms.Service;

import com.lms.DTO.RoleDTO;
import com.lms.Models.*;
import com.lms.DTO.UserDTO;
import com.lms.Repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository, UserTeamRepository userTeamRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.userTeamRepository = userTeamRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User createUser(UserDTO user) {
        ModelMapper modelMapper = new ModelMapper();
        User userEntity = modelMapper.map(user, User.class);

        Optional<Role> roleExists = roleRepository.findById(3L);
        User userSaved = userRepository.save(userEntity);
        if (roleExists.isPresent()) {
            Role role = roleExists.get();
            UserRole userRole = new UserRole(userSaved, role);
            userRoleRepository.save(userRole);
        }

        Team team = teamRepository.findTeamByteamName(user.getTeam());
        if (team != null) {
            UserTeam userTeam = new UserTeam(userEntity, team);
            userTeamRepository.save(userTeam);
        }
        return userSaved;
    }

    @Override
    public Page<User> getUserBetweenDates(Date startDate, Date endDate, Pageable pageable) {
        return userRepository.getUserByCreatedDateBetween(startDate, endDate, pageable);
    }

    @Override
    public Page<UserTeam> getUserTeamByUser(UserDTO userDTO, Pageable pageable) {
        ModelMapper modelMapper = new ModelMapper();
        User userSearch = modelMapper.map(userDTO, User.class);
        Optional<User> user = userRepository.findById(userSearch.getId());
        UserTeam userTeam = userTeamRepository.getUserTeamByUser(user.get());
        return userTeamRepository.getUserTeamByTeam(userTeam.getTeam(), pageable);
    }

    @Override
    public Page<User> getUserByRole(UserDTO userDTO, Pageable pageable) {
        ModelMapper modelMapper = new ModelMapper();
        User userSearch = modelMapper.map(userDTO, User.class);
        Optional<User> userOptional = userRepository.findById(userSearch.getId());
        User user = userOptional.get();
        List<UserRole> userRoles = user.getUserRoles();
        for (UserRole userRole : userRoles) {
            long role = userRole.getRole().getId();
            if (role == 1) {
                return userRepository.findAll(pageable);
            } else if (role == 2) {
                UserTeam userTeam = userTeamRepository.getUserTeamByUser(user);
                return userRepository.getUserByTeam(user.getId(), pageable);
            } else {
                List<User> userList = Collections.singletonList(userRepository.findById(userDTO.getId()).get());
                return new PageImpl<>(userList);
            }
        }
        throw new IllegalArgumentException("Invalid role");
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

    @Override
    @Transactional
    public void addUserRole(UserDTO userDTO, RoleDTO roleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);
        Role role = modelMapper.map(roleDTO, Role.class);
        UserRole userRole = new UserRole(user, role);
        user.getUserRoles().add(userRole);
        role.getUserRoles().add(userRole);
        userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public void removeUserRole(UserDTO userDTO, RoleDTO roleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userDTO, User.class);
        Role role = modelMapper.map(roleDTO, Role.class);
        UserRole userRole = new UserRole(user, role);
        user.getUserRoles().remove(userRole);
        role.getUserRoles().remove(userRole);
        userRoleRepository.delete(userRole);
    }
}
