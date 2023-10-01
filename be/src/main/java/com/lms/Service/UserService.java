package com.example.lms.Service;

import com.example.lms.DTO.UserDTO;
import com.example.lms.Models.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<User> getAllUsers();

    Optional<User> getUserById(Long id);


    List<User> getUserBetweenDates(Date starDate, Date endDate);

    User createUser(UserDTO user);

    User updateUser(UserDTO user);

    void deleteUser(Long id);
}
