package com.lms.controller;

import com.lms.dto.RankEnum;
import com.lms.dto.User;
import com.lms.dto.projection.UserProjection;
import com.lms.security.TokenVerifier;
import com.lms.service.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class Login {
    private final UserServiceImpl userServiceImpl;
    private final TokenVerifier tokenVerifier;

    public Login(UserServiceImpl userServiceImpl, TokenVerifier tokenVerifier) {
        this.userServiceImpl = userServiceImpl;
        this.tokenVerifier = tokenVerifier;
    }

    @PostMapping
    public ResponseEntity<UserProjection> loginCreate(@RequestBody User user) {
        Optional<UserProjection> existingUser = userServiceImpl.getUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            UserProjection userProjection = existingUser.get();
            return ResponseEntity.status(HttpStatus.OK).body(userProjection);
        }
        user.setRank(RankEnum.EMPLOYEE);
        UserProjection newUser = userServiceImpl.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @GetMapping
    public ResponseEntity<String> accessWithToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractJwtToken(authorizationHeader);
        if (token != null && tokenVerifier.verifyJwt(token)) {
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
