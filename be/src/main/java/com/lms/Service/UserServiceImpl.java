package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.Role;
import com.lms.dto.User;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.exception.UnauthorizedException;
import com.lms.helper.ExcelHelper;
import com.lms.models.*;
import com.lms.models.Team;
import com.lms.repository.*;
import com.lms.utils.ProjectionMapper;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
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
import java.util.stream.Collectors;

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
        com.lms.models.User userEntity = modelMapper.map(user, com.lms.models.User.class);
        userEntity.setUpdatedBy(user.getUpdatedBy());

        Optional<com.lms.models.Role> roleExists = roleRepository.findById(3L);
        com.lms.models.User userSaved = userRepository.save(userEntity);
        UserProjection projection = ProjectionMapper.mapToUserProjection(userSaved);
        List<UserRole> userRoles = new ArrayList<>();
        if (roleExists.isPresent()) {
            com.lms.models.Role role = roleExists.get();
            UserRole userRole = new UserRole(userSaved, role);
            userRoles.add(userRole);
        }
        userRoleRepository.saveAll(userRoles);
        List<Long> teamIds = user.getTeams().stream().map(com.lms.dto.Team::getId).collect(Collectors.toList());

        List<Team> teams = teamRepository.findAllById(teamIds);
        List<UserTeam> userTeams = new ArrayList<>();

        for (Team team : teams) {
            UserTeam userTeam = new UserTeam(userEntity, team);
            userTeams.add(userTeam);
        }
        try {
            userTeamRepository.saveAll(userTeams);
        } catch (HibernateError ex) {
            throw new HibernateException("User and team pair already exists");
        }

        return projection;
    }

    @Override
    public Page<UserProjection> getUserCreatedBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return userRepository.getUserByCreatedDateBetween(startDate, endDate, pageable);
    }

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
        com.lms.models.User userEntity = userRepository.findById(user.getId()).get();
        userEntity.setName(user.getName());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setEmail(user.getEmail());
        userEntity.setPhone(user.getPhone());
        userEntity.setUniversity(user.getUniversity());
        userEntity.setUniversityCode(user.getUniversityCode());
        userEntity.setUniversityGraduateDate(user.getUniversityGraduateDate());
        userEntity.setSkills(user.getSkills());
        userEntity.setRank(user.getRank());
        userEntity.setJoinedDate(user.getJoinedDate());
        userEntity.setDepartment(user.getDepartment());
        userEntity.setStatus(user.isStatus());
        userEntity.setResignedDate(user.getResignedDate());
        userEntity.setStatus(user.isStatus());
        userEntity.setUpdatedBy(user.getUpdatedBy());
        userEntity.setUpdatedDate(LocalDateTime.now());

        List<UserTeam> userTeams = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();

        for (com.lms.dto.Team team : user.getTeams()) {
            for (UserTeam existTeam : userEntity.getUserTeams()) {
                if (Objects.equals(team.getId(), existTeam.getTeam().getId())) {
                    continue;
                }
                Team teamEntity = teamRepository.findById(team.getId()).get();
                UserTeam userTeam = new UserTeam(userEntity, teamEntity);
                userTeams.add(userTeam);
            }
        }
        try {
            userTeamRepository.saveAll(userTeams);
        } catch (HibernateError ex) {
            throw new HibernateException("User and team pair already exists");
        }
        Set<com.lms.models.Role.RoleEnum> newRoleNames = new HashSet<>(user.getUserRoles().stream()
                .map(role -> role.getRole().getName())
                .collect(Collectors.toList()));
        List<UserRole> existingUserRoles = userRoleRepository.findByUser(userEntity);
        List<UserRoleKey> userRolesToDelete = new ArrayList<>();
        for (UserRole exists : existingUserRoles) {
            com.lms.models.Role.RoleEnum roleId = exists.getRole().getName();
            if (newRoleNames.contains(roleId)) {
                newRoleNames.remove(roleId);
            } else {
                userRolesToDelete.add(exists.getId());
            }
        }
        for (com.lms.models.Role.RoleEnum roleId : newRoleNames) {
            com.lms.models.Role role = roleRepository.findByName(roleId);
            if (role != null) {
                UserRole newUserRole = new UserRole(userEntity, role);
                userRoles.add(newUserRole);
            }
        }
        userRoleRepository.deleteAllById(userRolesToDelete);
        userRoleRepository.saveAll(userRoles);

        userRepository.save(userEntity);
        return ProjectionMapper.mapToUserProjection(userEntity);
    }

    @Override
    public UserProjection changeStatus(Long id, ChangeUserStatus statusDTO) {
        com.lms.models.User user = userRepository.findById(id).get();
        user.setStatus(statusDTO.isStatus());
        user.setUpdatedBy(statusDTO.getUpdatedBy());
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
            if(listUser.size() > 0){
                userRepository.saveAll(listUser);
            }else {
                throw new RuntimeException("you need additional data ");
            }
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    public Page<UserProjection> searchUser(String keyword, Pageable pageable) {
        return userRepository.searchUser(keyword, pageable);
    }

    @Override
    public com.lms.models.User updateMyProflie(Long id, MyProfile myProfile) throws NotFoundByIdException {
        Optional<com.lms.models.User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundByIdException("User not found with id: " +id);
        }
        com.lms.models.User userEntity = userOptional.get();
        userEntity.setName(myProfile.getName());
        userEntity.setPhone(myProfile.getPhone());
        userEntity.setSkills(myProfile.getSkills());
        return userRepository.save(userEntity);
    }

    @Override
    public List<com.lms.models.Role.RoleEnum> getRolesOfUser(Long id) {
        return userRepository.getRolesOfUser(id);
    }
}
