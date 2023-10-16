package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.UserTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserProjection> getAllUsers(Pageable pageable);

    Optional<com.lms.models.User> getUserById(Long id);

    Optional<UserProjection> getUserByEmail(String email);

    Page<UserProjection> getUserCreatedBetweenDates(LocalDateTime starDate, LocalDateTime endDate, Pageable pageable);

    Page<UserTeam> getUserTeamByUser(com.lms.models.User user, Pageable pageable);

    Page<UserProjection> getUserByRole(com.lms.models.User user, Pageable pageable);

    UserProjection createUser(User user);

    UserProjection updateUser(User user);

    UserProjection changeStatus(Long id, ChangeUserStatus statusDTO);

    void deleteUser(Long id);

    void addUserRole(User user, Role role);

    void removeUserRole(User user, Role role);

    ByteArrayInputStream exportExcelUser();

    void saveExcel(MultipartFile file);
    Page<UserProjection> searchUser(String keyword, Pageable pageable);

    com.lms.models.User updateMyProflie(Long id, MyProfile myProfile) throws NotFoundByIdException;

    List<com.lms.models.Role.RoleEnum> getRolesOfUser(Long id);

    void deleteUserTeamByUserId(Long id);
}
