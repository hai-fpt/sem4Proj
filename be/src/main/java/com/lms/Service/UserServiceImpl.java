package com.lms.service;

import com.lms.dto.ChangeUserStatusDTO;
import com.lms.dto.RoleDTO;
import com.lms.exception.UnauthorizedException;
import com.lms.helper.ExcelHelper;
import com.lms.models.*;
import com.lms.dto.UserDTO;
import com.lms.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(UserDTO user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<UserDTO, User>() {
            @Override
            protected void configure() {
                map().setUpdatedBy(user.getRequestedByEmail());
            }
        });
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
    public Page<User> getUserCreatedBetweenDates(Date startDate, Date endDate, Pageable pageable) {
        return userRepository.getUserByCreatedDateBetween(startDate, endDate, pageable);
    }

    //    @Override
//    public Page<UserTeam> getUserTeamByUser(UserDTO userDTO, Pageable pageable) {
//        ModelMapper modelMapper = new ModelMapper();
//        User userSearch = modelMapper.map(userDTO, User.class);
//        Optional<User> user = userRepository.findById(userSearch.getId());
//        UserTeam userTeam = userTeamRepository.getUserTeamByUser(user.get());
//        return userTeamRepository.getUserTeamByTeam(userTeam.getTeam(), pageable);
//    }
    @Override
    public Page<UserTeam> getUserTeamByUser(User user, Pageable pageable) {
        UserTeam userTeam = userTeamRepository.getUserTeamByUser(user);
        if (userTeam == null || userTeam.getTeam() == null) {
            throw new NullPointerException("User " + user.getId() + " is not in any team");
        }
        return userTeamRepository.getUserTeamByTeam(userTeam.getTeam(), pageable);
    }

    @Override
    public Page<User> getUserByRole(User user, Pageable pageable) {
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles.isEmpty()) {
            throw new NullPointerException("This user does not have any roles");
        }
        userRoles.sort(Comparator.comparing(userRole -> userRole.getRole().getName()));
        for (UserRole userRole : userRoles) {
            Role.RoleEnum role = userRole.getRole().getName();
            if (Objects.equals(role, Role.RoleEnum.ADMIN)) {
                return userRepository.findAll(pageable);
            } else if (Objects.equals(role, Role.RoleEnum.MANAGER)) {
                return userRepository.getUserByTeam(user.getId(), pageable);
            } else {
                throw new UnauthorizedException("User is not authorized");
            }
        }
        throw new UnauthorizedException("User is not authorized");
//        ModelMapper modelMapper = new ModelMapper();
//        User userSearch = modelMapper.map(userDTO, User.class);
//        Optional<User> userOptional = userRepository.findById(userSearch.getId());
//        if (userOptional.isEmpty()) {
//            throw new NoSuchElementException("User does not exists");
//        }
//        User user = userOptional.get();
//        List<UserRole> userRoles = user.getUserRoles();
//        //TODO: if new role how to do?
//        for (UserRole userRole : userRoles) {
//            long role = userRole.getRole().getId();
//            if (role == 1) {
//                return userRepository.findAll(pageable);
//            } else if (role == 2) {
//                return userRepository.getUserByTeam(user.getId(), pageable);
//            } else {
//                List<User> userList = Collections.singletonList(userRepository.findById(userDTO.getId()).get());
//                return new PageImpl<>(userList);
//            }
//        }
//        throw new IllegalArgumentException("Invalid role");
    }

    @Override
    public User updateUser(UserDTO user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<UserDTO, User>() {
            @Override
            protected void configure() {
                map().setUpdatedBy(user.getRequestedByEmail());
            }
        });
        User userEntity = modelMapper.map(user, User.class);
        userEntity.setUpdatedDate(new Date());
        return userRepository.save(userEntity);
    }

    @Override
    public User saveUserDate(User user) {
        return userRepository.save(user);
    }

    @Override
    public User changeStatus(Long id, ChangeUserStatusDTO statusDTO) {
        User user = userRepository.findById(id).get();
        user.setStatus(statusDTO.isStatus());
        user.setUpdatedBy(statusDTO.getRequestedByEmail());
        user.setUpdatedDate(new Date());
        return userRepository.save(user);
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

    @Override
    public ByteArrayInputStream exportExcelUser() {
        List<User> users = userRepository.findAll();

        ByteArrayInputStream in = ExcelHelper.exportToExcel(users);
        return in;
    }

    @Override
    public void saveExcel(MultipartFile file) {
        try {
            List<User> listUser = ExcelHelper.excelToUsers(file.getInputStream());
            List<UserRole> listUserRole = new ArrayList<>();
            List<UserTeam> listUserTeams = new ArrayList<>();
            List<User> listUserSaved = userRepository.saveAll(listUser);
            for(User user: listUserSaved){
                Team team = teamRepository.findTeamByteamName(user.getTeam_alias());
                Role role = roleRepository.findRoleByName(user.getRole_alias());
                if (team != null) {
                    UserTeam userTeam = new UserTeam(user, team);
                    listUserTeams.add(userTeam);
                }
                if(role != null){
                    UserRole userRole = new UserRole(user, role);
                    listUserRole.add(userRole);
                }
            }
            userTeamRepository.saveAll(listUserTeams);
            userRoleRepository.saveAll(listUserRole);


        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public Page<User> searchUser(String keyword, Pageable pageable) {
        return userRepository.searchUser(keyword, pageable);
    }
}
