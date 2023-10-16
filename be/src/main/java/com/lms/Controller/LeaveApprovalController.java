package com.lms.controller;

import com.lms.dto.LeaveApproval;
import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.User;
import com.lms.models.UserLeave;
import com.lms.service.*;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/manager")
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
    public ResponseEntity<LeaveApproval> updateLeaveApprovalStatus(@RequestBody LeaveApproval leaveApprovalDTO) throws NotFoundByIdException {
        Optional<UserLeave> userLeaveOptional = userLeaveService.getUserLeaveById(leaveApprovalDTO.getId());
        if (userLeaveOptional.isEmpty()) {
            throw new NullPointerException(LEAVE_REQUEST_NOT_EXISTS);
        }
        Optional<com.lms.models.LeaveApproval> leaveApprovalOptional = leaveApprovalService.getLeaveApprovalByManagerId(leaveApprovalDTO.getId(), leaveApprovalDTO.getManagerId());
        if (leaveApprovalOptional.isEmpty()) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        if (!controllerUtils.validateRequestedUser(leaveApprovalDTO.getUpdatedBy())) {
            throw new NullPointerException(EMAIL_NOT_EXISTS);
        }
        LeaveApproval leaveApproval = leaveApprovalService.updateLeaveApprovalStatus(leaveApprovalDTO);
        return ResponseEntity.ok(leaveApproval);
    }

    @GetMapping("/leave/approval/{id}")
    @Operation(summary = "Get leave approvals by manager id", description = "Payload include manager id as param")
    public ResponseEntity<Page<LeaveApprovalProjection>> getLeaveApprovalByManagerId(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Pageable sortedPageable = controllerUtils.sortPage(pageable, "updatedDate");
        Page<LeaveApprovalProjection> leaveApprovals = leaveApprovalService.getLeaveApproveByManagerId(id, sortedPageable);
        return ResponseEntity.ok(leaveApprovals);
    }

    @GetMapping("leave/apporval/search")
    public ResponseEntity<Page<LeaveApprovalProjection>> searchLeaveApproval(@RequestParam(value = "keyword") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        return ResponseEntity.ok(leaveApprovalService.searchLeaveApproval(keyword, sorted));
    }

    @GetMapping("/leave/approval/month")
    public ResponseEntity<Page<LeaveApprovalProjection>> getLeaveApprovalByMonth(@RequestParam("id") Long id,
                                                                                 @RequestParam("date") @DateTimeFormat(pattern = JSON_VIEW_DATE_FORMAT) LocalDateTime date,
                                                                                 @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<LeaveApprovalProjection> projections = leaveApprovalService.getLeaveApprovalByMonth(id, date, sorted);
        return ResponseEntity.ok(projections);
    }

    @GetMapping("/leave/approval/day")
    public ResponseEntity<Page<LeaveApprovalProjection>> getLeaveApprovalByDay(@RequestParam("id") Long id,
                                                                               @RequestParam("date") @DateTimeFormat(pattern = JSON_VIEW_DATE_FORMAT) LocalDateTime date,
                                                                               @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<LeaveApprovalProjection> projections = leaveApprovalService.getLeaveApprovalByDate(id, date, sorted);
        return ResponseEntity.ok(projections);
    }
}
