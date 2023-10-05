package com.lms.Controller;

import com.lms.DTO.UserLeaveDTO;
import com.lms.Models.UserLeave;
import com.lms.Service.UserLeaveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserLeaveController {
    private final UserLeaveServiceImpl userLeaveService;

    @Autowired
    public UserLeaveController(UserLeaveServiceImpl userLeaveService) {
        this.userLeaveService = userLeaveService;
    }

    @PostMapping("/user_leave")
    public ResponseEntity<UserLeave> createUserLeave(@RequestBody UserLeaveDTO userLeaveDTO) {
        userLeaveDTO.setStatus(1);
        UserLeave userLeave = userLeaveService.createUserLeave(userLeaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userLeave);
    }
}
