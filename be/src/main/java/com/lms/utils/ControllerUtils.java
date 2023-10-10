package com.lms.utils;

import com.lms.models.Team;
import com.lms.models.User;
import com.lms.service.UserServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ControllerUtils {
    private final UserServiceImpl userService;

    public ControllerUtils(UserServiceImpl userService) {
        this.userService = userService;
    }

    public boolean validateRequestedUser(String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.isPresent();
    }

    public boolean validateRequestedUser(Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.isPresent();
    }

    public Pageable sortPage(Pageable pageable, String field) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, field)
        );
    }
}
