package com.lms.controller;

import com.lms.dto.LeaveApprovalDTO;
import com.lms.models.LeaveApproval;
import com.lms.models.User;
import com.lms.service.LeaveApprovalService;
import com.lms.service.LeaveApprovalServiceImpl;
import com.lms.service.UserService;
import com.lms.service.UserServiceImpl;
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
    private final UserService userService;
    private final ControllerUtils controllerUtils;

    public LeaveApprovalController(LeaveApprovalService leaveApprovalService, UserService userService, ControllerUtils controllerUtils) {
        this.leaveApprovalService = leaveApprovalService;
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @PutMapping("/leave/status_update")
    @Operation(summary = "Update leave approval tickets",
            description = "Payload include leave approval id, changed status, user id")
    public ResponseEntity<LeaveApproval> updateLeaveApprovalStatus(@RequestBody LeaveApprovalDTO leaveApprovalDTO) {
        Optional<LeaveApproval> leaveApprovalOptional = leaveApprovalService.getLeaveApprovalById(leaveApprovalDTO.getId());
        if (leaveApprovalOptional.isEmpty()) {
            throw new NullPointerException("Leave ticket does not exists");
        }
        if (!controllerUtils.validateRequestedUser(leaveApprovalDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + leaveApprovalDTO.getRequestedByEmail() + " not found");
        }
        LeaveApproval leaveApproval = leaveApprovalService.updateLeaveApprovalStatus(leaveApprovalDTO);
        return ResponseEntity.ok(leaveApproval);
    }

    @GetMapping("/leave/approval/{id}")
    @Operation(summary = "Get leave approvals by manager id", description = "Payload include manager id as param")
    public ResponseEntity<Page<LeaveApproval>> getLeaveApprovalByManagerId(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NullPointerException("User Id not found");
        }
        Pageable sortedPageable = controllerUtils.sortPage(pageable, "updatedDate");
        Page<LeaveApproval> leaveApprovals = leaveApprovalService.getLeaveApproveByManagerId(id, sortedPageable);
        return ResponseEntity.ok(leaveApprovals);
    }
}
