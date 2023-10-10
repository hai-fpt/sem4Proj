package com.lms.controller;

import com.lms.dto.DateRangeDTO;
import com.lms.models.Holiday;
import com.lms.models.UserLeave;
import com.lms.service.*;
import com.lms.utils.Constants;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final UserLeaveService userLeaveService;
    private final UserService userService;
    private final ControllerUtils controllerUtils;

    @Autowired
    public DashboardController(UserLeaveService userLeaveService, UserService userService, ControllerUtils controllerUtils) {
        this.userLeaveService = userLeaveService;
        this.userService = userService;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping("/dashboard/calendar")
    @Operation(summary = "List user leave in month",
            description = "Payload is current date as singleDate")
    public ResponseEntity<Page<UserLeave>> getUserLeaveByMonth(@RequestBody DateRangeDTO dateRangeDTO, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updated_date");
        Page<UserLeave> userLeaves = userLeaveService.getUserLeaveByMonth(dateRangeDTO, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    //TODO: change to work for multiple years?
    @GetMapping("/dashboard/days_off/{id}")
    public ResponseEntity<Integer> getUserRemainingDaysOff(@RequestParam("id") Long id) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }

        int daysOffDefault = Constants.DEFAULT_DAYS_OFF;

        List<UserLeave> userLeaves = userLeaveService.getUserLeaveByIdAndStatus(id, 2);

        int currentDaysOff = daysOffDefault - userLeaves.size();

        return ResponseEntity.ok(currentDaysOff);
    }

    //TODO: add holidy when db has it
}
