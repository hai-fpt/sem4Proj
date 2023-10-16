package com.lms.controller;

import com.lms.dto.*;
import com.lms.dto.projection.LeaveManagerProjection;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Leave;
import com.lms.models.User;
import com.lms.service.*;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lms.utils.Constants.*;

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
    public ResponseEntity<UserLeaveProjection> createUserLeave(@RequestBody UserLeave userLeaveDTO) throws Exception {
        if (!controllerUtils.validateRequestedUser(userLeaveDTO.getUpdatedBy())) {
            throw new NullPointerException(EMAIL_NOT_EXISTS);
        }
        if (!controllerUtils.validateRequestedUser(userLeaveDTO.getUser().getId())) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Optional<Leave> optionalLeave = leaveService.findLeaveById(userLeaveDTO.getLeave().getId());
        if (optionalLeave.isEmpty()) {
            throw new NullPointerException(LEAVE_NOT_EXISTS);
        }
        if (userLeaveDTO.getFromDate().isAfter(userLeaveDTO.getToDate())) {
            throw new Exception(INVALID_FROM_TO_DATE);
        }
        userLeaveDTO.setStatus(ApprovalStatus.PENDING);
        UserLeaveProjection userLeave = userLeaveService.createUserLeave(userLeaveDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(userLeave);
    }

    @PutMapping("/user_leave/cancel")
    public ResponseEntity<UserLeaveProjection> cancelLeave(@RequestBody UserLeaveCancel userLeaveCancel) throws NotFoundByIdException {
        if (!controllerUtils.validateRequestedUser(userLeaveCancel.getUpdatedBy())) {
            throw new NullPointerException(EMAIL_NOT_EXISTS);
        }
        Optional<com.lms.models.UserLeave> userLeaveOptional = userLeaveService.getUserLeaveById(userLeaveCancel.getId());
        if (userLeaveOptional.isEmpty()) {
            throw new NullPointerException(LEAVE_REQUEST_NOT_EXISTS);
        }
        UserLeaveProjection userLeave = userLeaveService.cancelLeave(userLeaveCancel);
        return ResponseEntity.ok(userLeave);
    }

    @GetMapping("/user_leave/role")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByRole(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByRole(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/user")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByUser(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userService.getUserById(id).get();
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByUser(user, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/from")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByFromDate(@RequestParam @DateTimeFormat(pattern = JSON_VIEW_DATE_FORMAT) LocalDateTime fromDateStr, @PageableDefault(size = 10) Pageable pageable) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(fromDateStr);
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByFromDate(dateRange, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/date")
    @RolesAllowed({"ADMIN", "MANAGER"})
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByDateRange(@RequestParam @DateTimeFormat(pattern = JSON_VIEW_DATE_FORMAT) LocalDateTime today, @PageableDefault(size = 10) Pageable pageable) {
        DateRange dateRange = new DateRange();
        dateRange.setSingleDate(today);
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByDateRange(dateRange, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/user_leave/self")
    public ResponseEntity<List<SelfLeave>> getSelfLeave(@RequestParam Long id) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        List<SelfLeave> selfLeaves = userLeaveService.getSelfLeave(id);
        return ResponseEntity.ok(selfLeaves);
    }

    @GetMapping("/user_leave")
    public ResponseEntity<UserLeaveProjection> getUserLeaveById(@RequestParam Long id) {
        Optional<com.lms.models.UserLeave> userLeave = userLeaveService.getUserLeaveById(id);
        if (userLeave.isEmpty()) {
            throw new NullPointerException(LEAVE_REQUEST_NOT_EXISTS);
        }
        UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(userLeave.get());
        return ResponseEntity.ok(projection);
    }

    @GetMapping("/user_leave/managers")
    public ResponseEntity<List<LeaveManagerProjection>> getUserLeaveManagers(@RequestParam Long id) {
        List<LeaveManagerProjection> projections = userLeaveService.getUserLeaveManagers(id);
        return ResponseEntity.ok(projections);
    }
}
