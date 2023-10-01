package com.example.lms.Controller;

import com.example.lms.DTO.UserDTO;
import com.example.lms.Models.User;
import com.example.lms.Service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Login {
    private final UserServiceImpl userServiceImpl;

    public Login(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<User> loginCreate(@RequestBody UserDTO user) {
        user.setRank(UserDTO.RankEnum.EMPLOYEE);
        User newUser = userServiceImpl.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}
