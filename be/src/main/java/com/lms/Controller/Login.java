package com.lms.Controller;

import com.lms.DTO.UserDTO;
import com.lms.Models.User;
import com.lms.Security.TokenVerifier;
import com.lms.Service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class Login {
    private final UserServiceImpl userServiceImpl;

    public Login(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    public ResponseEntity<User> loginCreate(@RequestBody UserDTO user) {
        //if user exist
        user.setRank(UserDTO.RankEnum.EMPLOYEE);
        User newUser = userServiceImpl.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @GetMapping
    public ResponseEntity<String> accessWithToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        if (token != null && TokenVerifier.verifyJwt(token)) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String extractJwtToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
