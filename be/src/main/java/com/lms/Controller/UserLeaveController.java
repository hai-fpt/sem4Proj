package com.lms.controller;

import com.lms.dto.ApprovalStatus;
import com.lms.dto.DateRange;
import com.lms.dto.UserLeaveCancel;
import com.lms.dto.UserLeave;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.models.Leave;
import com.lms.models.User;
import com.lms.service.*;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserLeaveController {
    private final UserLeaveService userLeaveService;
    private final ControllerUtils controllerUtils;
    private final UserService userService;
    private final LeaveService leaveService;

    @Autowired
    public UserLeaveController(UserLeaveService userLeaveService, ControllerUtils controllerUtils,
                               UserService userService, LeaveService leaveService) {
        this.userLeaveService = userLeaveService;
        this.controllerUtils = controllerUtils;
        this.userService = userService;
        this.leaveService = leaveService;
    }

    @PostMapping("/user_leave")
    @Operation(summary = "Create leave ticket",
            description = "For user, only id is required. For leave, only id is required. fromDate toDate YYYY-MM-DD")
    public ResponseEntity<UserLeaveProjection> createUserLeave(@RequestBody UserLeave userLeaveDTO) throws IOException {
        if (!controllerUtils.validateRequestedUser(userLeaveDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + userLeaveDTO.getRequestedByEmail() + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(userLeaveDTO.getUser().getId())) {
            throw new NullPointerException("User with id " + userLeaveDTO.getUser().getId() + " does not exists");
        }
        Optional<Leave> optionalLeave = leaveService.findLeaveById(userLeaveDTO.getLeave().getId());
        if (optionalLeave.isEmpty()) {
            throw new NullPointerException("Leave with id " + userLeaveDTO.getLeave().getId() + " does not exists");
        }
        userLeaveDTO.setStatus(ApprovalStatus.PENDING);
//        com.lms.models.UserLeave userLeave = userLeaveService.createUserLeave(userLeaveDTO);
        UserLeaveProjection userLeave = userLeaveService.createUserLeave(userLeaveDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userLeave);
    }

    @PutMapping("/user_leave/cancel")
    public ResponseEntity<UserLeaveProjection> cancelLeave(@RequestBody UserLeaveCancel userLeaveCancel) {
        if (!controllerUtils.validateRequestedUser(userLeaveCancel.getRequestedByEmail())) {
            throw new NullPointerException("Email " + userLeaveCancel.getRequestedByEmail() + " does not exists");
        }
        Optional<com.lms.models.UserLeave> userLeaveOptional = userLeaveService.getUserLeaveById(userLeaveCancel.getId());
        if (userLeaveOptional.isEmpty()) {
            throw new NullPointerException("Leave ticket with Id " + userLeaveCancel.getId() + " does not exists");
        }
        UserLeaveProjection userLeave = userLeaveService.cancelLeave(userLeaveCancel);
        return ResponseEntity.ok(userLeave);
    }

    @GetMapping("/user_leave/role")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByRole(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByRole(userDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByRole(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByRole(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/user")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByUser(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByUser(userDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByUser(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByUser(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/from")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByFromDate(@RequestBody UserLeaveDTO userLeaveDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByFromDate(userLeaveDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByFromDate(@RequestBody DateRange dateRange, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByFromDate(dateRange, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/date")
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByDateRange(@RequestBody DateRange dateRange, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByDateRange(dateRange, sorted);
        return ResponseEntity.ok(userLeaves);
    }

}
