package com.lms.controller;

import com.lms.dto.DateRangeDTO;
import com.lms.dto.UserLeaveCancelDTO;
import com.lms.dto.UserLeaveDTO;
import com.lms.models.Leave;
import com.lms.models.User;
import com.lms.models.UserLeave;
import com.lms.service.*;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserLeaveController {
    private final UserLeaveService userLeaveService;
    private final ControllerUtils controllerUtils;
    private final UserService userService;
    private final LeaveService leaveService;

    @Autowired
    public UserLeaveController(UserLeaveService userLeaveService, ControllerUtils controllerUtils, UserService userService, LeaveService leaveService) {
        this.userLeaveService = userLeaveService;
        this.controllerUtils = controllerUtils;
        this.userService = userService;
        this.leaveService = leaveService;
    }

    @PostMapping("/user_leave")
    @Operation(summary = "Create leave ticket",
            description = "For user, only id is required. For leave, only id is required. fromDate toDate YYYY-MM-DD")
    public ResponseEntity<UserLeave> createUserLeave(@RequestBody UserLeaveDTO userLeaveDTO) {
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
        userLeaveDTO.setStatus(1);
        UserLeave userLeave = userLeaveService.createUserLeave(userLeaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userLeave);
    }

    @PutMapping("/user_leave/cancel")
    public ResponseEntity<UserLeave> cancelLeave(@RequestBody UserLeaveCancelDTO userLeaveCancelDTO) {
        if (!controllerUtils.validateRequestedUser(userLeaveCancelDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + userLeaveCancelDTO.getRequestedByEmail() + " does not exists");
        }
        Optional<UserLeave> userLeaveOptional = userLeaveService.getUserLeaveById(userLeaveCancelDTO.getId());
        if (userLeaveOptional.isEmpty()) {
            throw new NullPointerException("Leave ticket with Id " + userLeaveCancelDTO.getId() + " does not exists");
        }
        UserLeave userLeave = userLeaveService.cancelLeave(userLeaveCancelDTO);
        return ResponseEntity.ok(userLeave);
    }

    @GetMapping("/user_leave/role")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByRole(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByRole(userDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeave>> getUserLeaveByRole(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByRole(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/user")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByUser(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByUser(userDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeave>> getUserLeaveByUser(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByUser(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/from")
//    public ResponseEntity<Page<UserLeave>> getUserLeaveByFromDate(@RequestBody UserLeaveDTO userLeaveDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByFromDate(userLeaveDTO, pageable);
//        return ResponseEntity.ok(userLeaves);
//    }
    public ResponseEntity<Page<UserLeave>> getUserLeaveByFromDate(@RequestBody DateRangeDTO dateRangeDTO, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByFromDate(dateRangeDTO, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/date")
    public ResponseEntity<Page<UserLeave>> getUserLeaveByDateRange(@RequestBody DateRangeDTO dateRangeDTO, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByDateRange(dateRangeDTO, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/export")
    public ResponseEntity<Resource> getFile() {
        String filename = "users_leave.xlsx";
        InputStreamResource file = new InputStreamResource(userLeaveService.exportExcelUserLeave());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
