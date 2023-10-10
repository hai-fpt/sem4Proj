package com.lms.controller;

import com.lms.dto.ChangeUserStatusDTO;
import com.lms.dto.DateRangeDTO;
import com.lms.dto.ResponseMessageDTO;
import com.lms.dto.UserDTO;
import com.lms.helper.ExcelHelper;
import com.lms.models.User;
import com.lms.models.UserTeam;
import com.lms.service.UserService;
import com.lms.service.UserServiceImpl;
import com.lms.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userServiceImpl;
    private final ControllerUtils controllerUtils;

    @Autowired
    public UserController(UserService userServiceImpl, ControllerUtils controllerUtils) {
        this.userServiceImpl = userServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<User> users = userServiceImpl.getAllUsers(pageable);
        List<User> updatedUser = new ArrayList<>();
        for (User user : users) {
            if (user.getJoined_date() != null) {
                Period workingTime = Period.between(new java.sql.Date(user.getJoined_date().getTime()).toLocalDate(), LocalDate.now());
                user.setWorking_time(workingTime.getYears() + " Years, " + workingTime.getMonths() + " Months");
            }
            if (user.getUniversity_graduate_date() != null) {
                Period expTime = Period.between(new java.sql.Date(user.getUniversity_graduate_date().getTime()).toLocalDate(), LocalDate.now());
                user.setExperience_date(expTime.getYears() + " Years, " + expTime.getMonths() + " Months");
            }
//            userServiceImpl.saveUserDate(user);
            updatedUser.add(user);
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        return ResponseEntity.ok(new PageImpl<>(updatedUser, sorted, users.getTotalElements()));
//        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userServiceImpl.getUserById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            if (foundUser.getJoined_date() != null) {
                Period workingTimes = Period.between(new java.sql.Date(foundUser.getJoined_date().getTime()).toLocalDate(), LocalDate.now());
                //TODO should return 2 fields depends (workingYear, workingMonth)
                foundUser.setWorking_time(workingTimes.getYears() + " Years, " + workingTimes.getMonths() + " Months");
            }
            if (foundUser.getUniversity_graduate_date() != null) {
                Period experienceTimes = Period.between(new java.sql.Date(foundUser.getUniversity_graduate_date().getTime()).toLocalDate(), LocalDate.now());
                //TODO should return 2 fields depends (expYear, expMonth)
                foundUser.setExperience_date(experienceTimes.getYears() + " Years, " + experienceTimes.getMonths() + " Months");
            }
            return ResponseEntity.ok(foundUser);
        }
        throw new NullPointerException("User with id " + id + " does not exists");
    }

    @GetMapping("/user/user_between_dates")
    @Operation(summary = "List users between 2 dates", description = "Payload include startDate and endDate, format: YYYY-MM-DD")
    public ResponseEntity<Page<User>> getUserCreatedBetweenDates(@RequestBody DateRangeDTO dateRange, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<User> users = userServiceImpl.getUserCreatedBetweenDates(dateRange.getStartDate(), dateRange.getEndDate(), sorted);
        return ResponseEntity.ok(users);
    }

    //    @GetMapping("/user/team")
//    public ResponseEntity<Page<UserTeam>> getUserTeam(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<UserTeam> userTeams = userServiceImpl.getUserTeamByUser(userDTO, pageable);
//        return ResponseEntity.ok(userTeams);
//    }
    @GetMapping("/user/team/{id}")
    @Operation(summary = "Retrieve team associated with provided user Id")
    public ResponseEntity<Page<UserTeam>> getUserTeam(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userServiceImpl.getUserById(id).get();
        Page<UserTeam> userTeams = userServiceImpl.getUserTeamByUser(user, sorted);
        return ResponseEntity.ok(userTeams);
    }

    @GetMapping("/user/role")
    @Operation(summary = "Retrieve users according to logged in user's role",
            description = "Payload include id, which would be the logged in user's id")
//    public ResponseEntity<Page<User>> getUserByRole(@RequestBody UserDTO userDTO, @PageableDefault(size = 10) Pageable pageable) {
//        Page<User> users = userServiceImpl.getUserByRole(userDTO, pageable);
//        return ResponseEntity.ok(users);
//    }
    public ResponseEntity<Page<User>> getUserByRole(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        User user = userServiceImpl.getUserById(id).get();
        Page<User> users = userServiceImpl.getUserByRole(user, sorted);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
        user.setRank(UserDTO.RankEnum.EMPLOYEE);
        User newUser = userServiceImpl.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO user) {
        if (!controllerUtils.validateRequestedUser(user.getRequestedByEmail())) {
            throw new NullPointerException("Email " + user.getRequestedByEmail() + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        user.setId(id);
        User updateUser = userServiceImpl.updateUser(user);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/user/status/{id}")
    @Operation(summary = "Change the user status", description = "Payload include bool status, and email of logged in user requestedByEmail")
    public ResponseEntity<User> changeStatus(@PathVariable("id") Long id, @RequestBody ChangeUserStatusDTO statusDTO) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(statusDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + statusDTO.getRequestedByEmail() + " does not exists");
        }
        User updatedUser = userServiceImpl.changeStatus(id, statusDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/export")
    public ResponseEntity<Resource> getFile() {
        String filename = "users.xlsx";
        InputStreamResource file = new InputStreamResource(userServiceImpl.exportExcelUser());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("/user/import")
    public ResponseEntity<ResponseMessageDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                userServiceImpl.saveExcel(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDTO(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "reason" + " " + e.getMessage() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessageDTO(message));
            }
        }

        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDTO(message));
    }

    @GetMapping("/user/search")
    public ResponseEntity<Page<User>> searchUser(@RequestParam(value = "keyword") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        return ResponseEntity.ok(userServiceImpl.searchUser(keyword, sorted));
    }
}
