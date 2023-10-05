package com.lms.Controller;

import com.lms.DTO.DateRangeDTO;
import com.lms.DTO.UserDTO;
import com.lms.Models.User;
import com.lms.Models.UserTeam;
import com.lms.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/user")
    public ResponseEntity<Page<User>> getAllUsers(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<User> users = userServiceImpl.getAllUsers(pageable);
        return ResponseEntity.ok(users);
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
		return ResponseEntity.notFound().build();
	}

//    @GetMapping("/user/teams/{team}")
//    public ResponseEntity<List<User>> getUserByTeam(@PathVariable("team") String teamName) {
//        List<User> users = userServiceImpl.getUserByTeam(teamName);
//        return ResponseEntity.ok(users);
//    }

//    @GetMapping("/user/lead/{lead}")
//    public ResponseEntity<List<User>> getUserByTeamLead(@PathVariable("lead") String teamLead) {
//        List<User> users = userServiceImpl.getUserByTeamLead(teamLead);
//        return ResponseEntity.ok(users);
//    }

    @GetMapping("/user/user-between-dates")
    public ResponseEntity<Page<User>> getUserBetweenDates(@RequestBody DateRangeDTO dateRange, @PageableDefault(size = 10)Pageable pageable) {
        Page<User> users = userServiceImpl.getUserBetweenDates(dateRange.getStartDate(), dateRange.getEndDate(), pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/team")
    public ResponseEntity<Page<UserTeam>> getUserTeam(@RequestBody UserDTO userDTO, @PageableDefault(size = 10)Pageable pageable) {
        Page<UserTeam> userTeams = userServiceImpl.getUserTeamByUser(userDTO, pageable);
        return ResponseEntity.ok(userTeams);
    }

    @GetMapping("/user/role")
    public ResponseEntity<Page<User>> getUserByRole(@RequestBody UserDTO userDTO, Pageable pageable) {
        Page<User> users = userServiceImpl.getUserByRole(userDTO, pageable);
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
        user.setId(id);
        User updateUser = userServiceImpl.updateUser(user);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
