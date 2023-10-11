package com.lms.controller;

import com.lms.dto.ApprovalStatus;
import com.lms.dto.DateRange;
import com.lms.dto.DaysOff;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.models.Holiday;
import com.lms.models.User;
import com.lms.service.*;
import com.lms.utils.Constants;
import com.lms.utils.ControllerUtils;
import com.lms.utils.DateCalculation;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final UserLeaveService userLeaveService;
    private final UserService userService;
    private final HolidayService holidayService;
    private final ControllerUtils controllerUtils;
    private final DateCalculation dateCalculation;
    private final ConfigurationService configurationService;

    @Autowired
    public DashboardController(UserLeaveService userLeaveService, UserService userService, HolidayService holidayService, ControllerUtils controllerUtils, DateCalculation dateCalculation, ConfigurationService configurationService) {
        this.userLeaveService = userLeaveService;
        this.userService = userService;
        this.holidayService = holidayService;
        this.controllerUtils = controllerUtils;
        this.dateCalculation = dateCalculation;
        this.configurationService = configurationService;
    }

    @GetMapping("/dashboard/calendar")
    @Operation(summary = "List user leave in month",
            description = "Payload is current first of month as startDate, end of month as endDate")
    public ResponseEntity<Page<UserLeaveProjection>> getUserLeaveByMonth(@RequestBody DateRange dateRange, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByMonth(dateRange, sorted);
        return ResponseEntity.ok(userLeaves);
    }

    @GetMapping("/dashboard/days_off/{id}")
    @Operation(summary = "Remaining day off",
            description = "Payload is id of current user, and the start and beginning of the year")
    public ResponseEntity<DaysOff> getUserRemainingDaysOff(@PathVariable("id") Long id,
                                                           @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") String fromDateStr,
                                                           @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") String toDateStr) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        LocalDateTime fromDate = LocalDateTime.parse(fromDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime toDate = LocalDateTime.parse(toDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        DaysOff daysOff = new DaysOff();
        daysOff.setDefaultDaysOff(Constants.DEFAULT_DAYS_OFF);

        float daysOffDefault = daysOff.getDefaultDaysOff();

        User user = userService.getUserById(id).get();
        Period workTime = Period.between(user.getJoinedDate().toLocalDate(), LocalDate.now());
        int milestoneYear = configurationService.getConfiguration().getMilestoneYear();
        if (milestoneYear != fromDate.getYear()) {
            milestoneYear -= fromDate.getYear() - milestoneYear;
        }
        float extraDays = (float) workTime.getYears() / milestoneYear;
        if (extraDays >= 1) {
            daysOffDefault += Math.floor(extraDays);
        }
        //TODO: change this after holiday dates are changed to LocalDateTime
        //TODO: change to projection after refactor
        List<UserLeaveProjection> userLeaves = userLeaveService.getUserLeaveByIdAndStatusAndType(id, ApprovalStatus.APPROVED, true);
        Page<Holiday> holidays = holidayService.getAllHolidays(Pageable.unpaged());
        int compensationForHoliday = dateCalculation.holidaysCompensation(userLeaves, holidays, fromDate, toDate);
        float usedDaysOff = dateCalculation.calculateUsedDaysOff(userLeaves, fromDate, toDate);
        daysOff.setUsedDaysOff(usedDaysOff);
        daysOff.setRemainingDaysOff((daysOffDefault - usedDaysOff) + compensationForHoliday);
        return ResponseEntity.ok(daysOff);
    }

    @GetMapping("/dashboard/holiday")
    public ResponseEntity<Page<Holiday>> getAllHolidays(@RequestParam int year, Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "fromDate");
        Page<Holiday> holidays = holidayService.getAllHolidaysByYear(year, sorted);
        return ResponseEntity.ok(holidays);
    }

    @GetMapping("/dashboard/request")
    public ResponseEntity<Page<UserLeaveProjection>> getMyRequests(@RequestParam long id, Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        User user = userService.getUserById(id).get();
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserLeaveProjection> requests = userLeaveService.getUserLeaveByUser(user, sorted);
        return ResponseEntity.ok(requests);
    }
}
