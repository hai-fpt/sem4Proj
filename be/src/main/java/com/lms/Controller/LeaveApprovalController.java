package com.lms.controller;

import com.lms.dto.LeaveApproval;
import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.models.User;
import com.lms.models.UserLeave;
import com.lms.service.*;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LeaveApprovalController {
    private final LeaveApprovalService leaveApprovalService;
    private final UserLeaveService userLeaveService;
    private final UserService userService;
    private final ControllerUtils controllerUtils;

    public LeaveApprovalController(LeaveApprovalService leaveApprovalService, UserLeaveService userLeaveService, UserService userService, ControllerUtils controllerUtils) {
        this.leaveApprovalService = leaveApprovalService;
        this.userLeaveService = userLeaveService;
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @PutMapping("/leave/status_update")
    @Operation(summary = "Update leave approval tickets",
            description = "Payload include user leave approval id, changed status, user id")
    public ResponseEntity<LeaveApprovalProjection> updateLeaveApprovalStatus(@RequestBody LeaveApproval leaveApprovalDTO) {
        Optional<UserLeave> userLeaveOptional = userLeaveService.getUserLeaveById(leaveApprovalDTO.getId());
        if (userLeaveOptional.isEmpty()) {
            throw new NullPointerException("Leave ticket does not exists");
        }
        if (!controllerUtils.validateRequestedUser(leaveApprovalDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + leaveApprovalDTO.getRequestedByEmail() + " not found");
        }
        LeaveApprovalProjection leaveApproval = leaveApprovalService.updateLeaveApprovalStatus(leaveApprovalDTO);
        return ResponseEntity.ok(leaveApproval);
    }

    @GetMapping("/leave/approval/{id}")
    @Operation(summary = "Get leave approvals by manager id", description = "Payload include manager id as param")
    public ResponseEntity<Page<LeaveApprovalProjection>> getLeaveApprovalByManagerId(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NullPointerException("User Id not found");
        }
        Pageable sortedPageable = controllerUtils.sortPage(pageable, "updatedDate");
        Page<LeaveApprovalProjection> leaveApprovals = leaveApprovalService.getLeaveApproveByManagerId(id, sortedPageable);
        return ResponseEntity.ok(leaveApprovals);
    }
}
