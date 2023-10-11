package com.lms.controller;

import com.lms.dto.ChangeUserStatus;
import com.lms.dto.DateRange;
import com.lms.dto.ResponseMessageDTO;
import com.lms.dto.User;
import com.lms.dto.projection.UserProjection;
import com.lms.helper.ExcelHelper;
import com.lms.models.UserTeam;
import com.lms.service.UserService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
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

import java.sql.SQLException;
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
    public ResponseEntity<Page<UserProjection>> getAllUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserProjection> projections = userServiceImpl.getAllUsers(sorted);
        return ResponseEntity.ok(projections);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserProjection> getUserById(@PathVariable("id") Long id) {
        Optional<com.lms.models.User> user = userServiceImpl.getUserById(id);
        if (user.isPresent()) {
            com.lms.models.User foundUser = user.get();
            UserProjection projection = ProjectionMapper.mapToUserProjection(foundUser);
            return ResponseEntity.ok(projection);
        }
        throw new NullPointerException("User with id " + id + " does not exists");
    }

    @GetMapping("/user/user_between_dates")
    @Operation(summary = "List users between 2 dates", description = "Payload include startDate and endDate, format: YYYY-MM-DD")
    public ResponseEntity<Page<UserProjection>> getUserCreatedBetweenDates(@RequestBody DateRange dateRange, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<UserProjection> users = userServiceImpl.getUserCreatedBetweenDates(dateRange.getStartDate(), dateRange.getEndDate(), sorted);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/team/{id}")
    @Operation(summary = "Retrieve team associated with provided user Id")
    public ResponseEntity<Page<UserTeam>> getUserTeam(@PathVariable("id") Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        com.lms.models.User user = userServiceImpl.getUserById(id).get();
        Page<UserTeam> userTeams = userServiceImpl.getUserTeamByUser(user, sorted);
        return ResponseEntity.ok(userTeams);
    }

    @GetMapping("/user/role")
    @Operation(summary = "Retrieve users according to logged in user's role",
            description = "Payload include id, which would be the logged in user's id")
    public ResponseEntity<Page<UserProjection>> getUserByRole(@RequestParam Long id, @PageableDefault(size = 10) Pageable pageable) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        com.lms.models.User user = userServiceImpl.getUserById(id).get();
        Page<UserProjection> users = userServiceImpl.getUserByRole(user, sorted);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<UserProjection> createUser(@RequestBody User user) {
        user.setRank(User.RankEnum.EMPLOYEE);
        UserProjection newUser = userServiceImpl.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserProjection> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        if (!controllerUtils.validateRequestedUser(user.getRequestedByEmail())) {
            throw new NullPointerException("Email " + user.getRequestedByEmail() + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        user.setId(id);
        UserProjection updateUser = userServiceImpl.updateUser(user);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/user/status/{id}")
    @Operation(summary = "Change the user status", description = "Payload include bool status, and email of logged in user requestedByEmail")
    public ResponseEntity<UserProjection> changeStatus(@PathVariable("id") Long id, @RequestBody ChangeUserStatus statusDTO) {
        if (!controllerUtils.validateRequestedUser(id)) {
            throw new NullPointerException("User with id " + id + " does not exists");
        }
        if (!controllerUtils.validateRequestedUser(statusDTO.getRequestedByEmail())) {
            throw new NullPointerException("Email " + statusDTO.getRequestedByEmail() + " does not exists");
        }
        UserProjection updatedUser = userServiceImpl.changeStatus(id, statusDTO);
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

        //Validate excel
        if (ExcelHelper.hasExcelFormat(file)) {
            try {
                userServiceImpl.saveExcel(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessageDTO(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + " " + "because" + " " + e.getMessage() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessageDTO(message));
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDTO(message));
    }

    @GetMapping("/user/search")
    public ResponseEntity<Page<UserProjection>> searchUser(@RequestParam(value = "keyword") String keyword, @PageableDefault(size = 10) Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        return ResponseEntity.ok(userServiceImpl.searchUser(keyword, sorted));
    }
}
