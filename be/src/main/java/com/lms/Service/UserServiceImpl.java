package com.lms.service;

import com.lms.dto.ChangeUserStatus;
import com.lms.dto.Role;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.UnauthorizedException;
import com.lms.helper.ExcelHelper;
import com.lms.models.*;
import com.lms.dto.User;
import com.lms.repository.*;
import com.lms.utils.ProjectionMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private final ExcelHelper excelHelper;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(ExcelHelper excelHelper, UserRepository userRepository, TeamRepository teamRepository, UserTeamRepository userTeamRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.excelHelper = excelHelper;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.userTeamRepository = userTeamRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Page<UserProjection> getAllUsers(Pageable pageable) {
        return userRepository.findAllProjectedBy(pageable);
    }

    @Override
    public Optional<com.lms.models.User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserProjection> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserProjection createUser(User user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<User, com.lms.models.User>() {
            @Override
            protected void configure() {
                map().setUpdatedBy(user.getRequestedByEmail());
            }
        });
        com.lms.models.User userEntity = modelMapper.map(user, com.lms.models.User.class);

        Optional<com.lms.models.Role> roleExists = roleRepository.findById(3L);
        com.lms.models.User userSaved = userRepository.save(userEntity);
        UserProjection projection = ProjectionMapper.mapToUserProjection(userSaved);
        if (roleExists.isPresent()) {
            com.lms.models.Role role = roleExists.get();
            UserRole userRole = new UserRole(userSaved, role);
            userRoleRepository.save(userRole);
        }

        Team team = teamRepository.findTeamByTeamName(user.getTeam());
        if (team != null) {
            UserTeam userTeam = new UserTeam(userEntity, team);
            userTeamRepository.save(userTeam);
        }
        return projection;
    }

    @Override
    public Page<UserProjection> getUserCreatedBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
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
    public Page<UserTeam> getUserTeamByUser(com.lms.models.User user, Pageable pageable) {
        UserTeam userTeam = userTeamRepository.getUserTeamByUser(user);
        if (userTeam == null || userTeam.getTeam() == null) {
            throw new NullPointerException("User " + user.getId() + " is not in any team");
        }
        return userTeamRepository.getUserTeamByTeam(userTeam.getTeam(), pageable);
    }

    @Override
    public Page<UserProjection> getUserByRole(com.lms.models.User user, Pageable pageable) {
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles.isEmpty()) {
            throw new NullPointerException("This user does not have any roles");
        }
        userRoles.sort(Comparator.comparing(userRole -> userRole.getRole().getName()));
        for (UserRole userRole : userRoles) {
            com.lms.models.Role.RoleEnum role = userRole.getRole().getName();
            if (Objects.equals(role, com.lms.models.Role.RoleEnum.ADMIN)) {
                return userRepository.findAllProjectedBy(pageable);
            } else if (Objects.equals(role, com.lms.models.Role.RoleEnum.MANAGER)) {
                return userRepository.getUserByTeam(user.getId(), pageable);
            } else {
                throw new UnauthorizedException("User is not authorized");
            }
        }
        throw new UnauthorizedException("User is not authorized");
    }

    @Override
    public UserProjection updateUser(User user) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<User, com.lms.models.User>() {
            @Override
            protected void configure() {
                map().setUpdatedBy(user.getRequestedByEmail());
            }
        });
        com.lms.models.User userEntity = modelMapper.map(user, com.lms.models.User.class);
        userEntity.setUpdatedDate(LocalDateTime.now());
        com.lms.models.User updated = userRepository.save(userEntity);
        return ProjectionMapper.mapToUserProjection(updated);
    }

    @Override
    public UserProjection changeStatus(Long id, ChangeUserStatus statusDTO) {
        com.lms.models.User user = userRepository.findById(id).get();
        user.setStatus(statusDTO.isStatus());
        user.setUpdatedBy(statusDTO.getRequestedByEmail());
        user.setUpdatedDate(LocalDateTime.now());
        com.lms.models.User updated = userRepository.save(user);
        return ProjectionMapper.mapToUserProjection(updated);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addUserRole(User userDTO, Role roleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.User user = modelMapper.map(userDTO, com.lms.models.User.class);
        com.lms.models.Role role = modelMapper.map(roleDTO, com.lms.models.Role.class);
        UserRole userRole = new UserRole(user, role);
        user.getUserRoles().add(userRole);
        role.getUserRoles().add(userRole);
        userRoleRepository.save(userRole);
    }

    @Override
    @Transactional
    public void removeUserRole(User userDTO, Role roleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.User user = modelMapper.map(userDTO, com.lms.models.User.class);
        com.lms.models.Role role = modelMapper.map(roleDTO, com.lms.models.Role.class);
        UserRole userRole = new UserRole(user, role);
        user.getUserRoles().remove(userRole);
        role.getUserRoles().remove(userRole);
        userRoleRepository.delete(userRole);
    }

    @Override
    public ByteArrayInputStream exportExcelUser() {
        List<com.lms.models.User> users = userRepository.findAll();

        ByteArrayInputStream in = excelHelper.exportToExcel(users);
        return in;
    }

    @Override
    //throws FormatException, DataNotFoundException, SQLException
    public void saveExcel(MultipartFile file) {
        try {
            List<com.lms.models.User> listUser = excelHelper.excelToUsers(file.getInputStream());
            userRepository.saveAll(listUser);

        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public Page<UserProjection> searchUser(String keyword, Pageable pageable) {
        return userRepository.searchUser(keyword, pageable);
    }
}
