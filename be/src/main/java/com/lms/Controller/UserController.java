package com.example.lms.Controller;

import com.example.lms.DTO.DateRangeDTO;
import com.example.lms.DTO.UserDTO;
import com.example.lms.Models.User;
import com.example.lms.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userServiceImpl.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        Optional<User> user = userServiceImpl.getUserById(id);
        if (user.isPresent()) {
            User foundUser = user.get();
            Date currentDate = new Date();
            Date gradDate = foundUser.getCreatedDate();
            Date joinDate = foundUser.getJoined_date();

            long expDate = currentDate.getTime() - gradDate.getTime();
            int expYears = (int) (expDate / (1000L * 60 * 60 * 24 * 365));
            int expMonths = (int) ((expDate % (1000L * 60 * 60 * 24 * 365)) / (1000L * 60 * 60 * 24 * 30));
            foundUser.setExperience_date(expYears + " Years, " + expMonths + " Months");

            long workDate = currentDate.getTime() - joinDate.getTime();
            int workYears = (int) (workDate / (1000L * 60 * 60 * 24 * 365));
            int workMonths = (int) ((workDate % (1000L * 60 * 60 * 24 * 365)) / (1000L * 60 * 60 * 24 * 30));
            foundUser.setWorking_time(workYears + " Years, " + workMonths + " Months");

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
    public ResponseEntity<List<User>> getUserBetweenDates(@RequestBody DateRangeDTO dateRange) {
        List<User> users = userServiceImpl.getUserBetweenDates(dateRange.getStartDate(), dateRange.getEndDate());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user) {
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
