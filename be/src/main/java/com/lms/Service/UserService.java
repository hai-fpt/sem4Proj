package com.lms.service;

import com.lms.dto.ChangeUserStatusDTO;
import com.lms.dto.RoleDTO;
import com.lms.dto.UserDTO;
import com.lms.models.User;
import com.lms.models.UserTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(Pageable pageable);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    Page<User> getUserCreatedBetweenDates(Date starDate, Date endDate, Pageable pageable);

    Page<UserTeam> getUserTeamByUser(User user, Pageable pageable);

    Page<User> getUserByRole(User user, Pageable pageable);

    User createUser(UserDTO user);

    User updateUser(UserDTO user);

    User saveUserDate(User user);

    User changeStatus(Long id, ChangeUserStatusDTO statusDTO);

    void deleteUser(Long id);

    void addUserRole(UserDTO userDTO, RoleDTO roleDTO);

    void removeUserRole(UserDTO userDTO, RoleDTO roleDTO);

    ByteArrayInputStream exportExcelUser();

    void saveExcel(MultipartFile file);
    Page<User> searchUser(String keyword, Pageable pageable);
}
