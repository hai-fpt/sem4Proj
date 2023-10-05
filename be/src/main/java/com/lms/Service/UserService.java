package com.lms.Service;

import com.lms.DTO.RoleDTO;
import com.lms.DTO.UserDTO;
import com.lms.Models.User;
import com.lms.Models.UserTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface UserService {
    Page<User> getAllUsers(Pageable pageable);

    Optional<User> getUserById(Long id);

    Page<User> getUserBetweenDates(Date starDate, Date endDate, Pageable pageable);

    Page<UserTeam> getUserTeamByUser(UserDTO userDTO, Pageable pageable);

    Page<User> getUserByRole(UserDTO userDTO, Pageable pageable);

    User createUser(UserDTO user);

    User updateUser(UserDTO user);

    void deleteUser(Long id);

    void addUserRole(UserDTO userDTO, RoleDTO roleDTO);

    void removeUserRole(UserDTO userDTO, RoleDTO roleDTO);
}
