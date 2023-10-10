package com.lms.controller;

import com.lms.dto.LeaveDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Leave;
import com.lms.service.LeaveServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {
    private final LeaveServiceImpl leaveServiceImpl;

    private final ControllerUtils controllerUtils;

    @Autowired
    public LeaveController(LeaveServiceImpl leaveServiceImpl, ControllerUtils controllerUtils) {
        this.leaveServiceImpl = leaveServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<Leave>> getAllLeaves(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<Leave> leaves = leaveServiceImpl.getAllLeaves(sorted);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/{id}")
    public ResponseEntity getLeaveById(@PathVariable("id") Long id){
        if (Objects.isNull(id) || id < 0){
                String message = "Id invalid";
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Optional<Leave> leave = leaveServiceImpl.findLeaveById(id);
        return leave.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity createLeave(@RequestBody LeaveDTO leave){
        if (Objects.isNull(leave)){
            String message = "Leave invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (leave.getName().isEmpty()){
            String message = "Name invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Leave newLeave = leaveServiceImpl.createLeave(leave);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLeave);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLeave(@PathVariable("id") Long id, @RequestBody LeaveDTO leave) throws NotFoundByIdException {
        if(Objects.isNull(id) || id < 0){
            String message = "Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (Objects.isNull(leave)){
            String message = "Leave invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(leave.getName().isEmpty()){
            String message = "Name invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Leave updateLeave = leaveServiceImpl.updateLeave(id, leave);
        return ResponseEntity.ok(updateLeave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLeave(@PathVariable("id") Long id){
        if(Objects.isNull(id) || id < 0){
            String message = "Id invalid";
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        leaveServiceImpl.deleteLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
