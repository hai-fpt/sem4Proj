package com.lms.controller;

import com.lms.dto.Leave;
import com.lms.dto.projection.LeaveProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.service.LeaveService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private final LeaveService leaveService;

    @Autowired
    private final ControllerUtils controllerUtils;

    public LeaveController(LeaveService leaveService, ControllerUtils controllerUtils) {
        this.leaveService = leaveService;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<LeaveProjection>> getAllLeaves(@PageableDefault(page = 0, size = 10)Pageable pageable){
        Pageable sorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );
        Page<LeaveProjection> leaves = leaveService.getAllLeaves(sorted);
        return ResponseEntity.ok(leaves);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveProjection> getLeaveById(@PathVariable("id") Long id){
        if (Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        Optional<com.lms.models.Leave> leaveOptional = leaveService.findLeaveById(id);
        com.lms.models.Leave leave = leaveOptional.get();
        LeaveProjection leaveProjection = ProjectionMapper.mapToLeaveProjection(leave);
        return ResponseEntity.status(HttpStatus.OK).body(leaveProjection);
    }

    @PostMapping()
    public ResponseEntity<LeaveProjection> createLeave(@RequestBody Leave leave) throws DuplicateException {
        if (Objects.isNull(leave)){
            throw new NullPointerException(INVALID_PAYLOAD);
        }
        if (leave.getName() == null || leave.getName().isEmpty()){
            throw new NullPointerException(INVALID_NAME);
        }
        if (Objects.isNull(leave.getAffectsDaysOff())){
           leave.setAffectsDaysOff(true);
        }
        com.lms.models.Leave newLeave = leaveService.createLeave(leave);
        LeaveProjection leaveProjection = ProjectionMapper.mapToLeaveProjection(newLeave);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveProjection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveProjection> updateLeave(@PathVariable("id") Long id, @RequestBody Leave leave) throws DuplicateException, NotFoundByIdException {
        if(Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        if (Objects.isNull(leave)){
            throw new NullPointerException(INVALID_PAYLOAD);
        }
        if(leave.getName() == null || leave.getName().isEmpty()){
            throw new NullPointerException(INVALID_NAME);
        }
        if (Objects.isNull(leave.getAffectsDaysOff())){
            leave.setAffectsDaysOff(true);
        }
        com.lms.models.Leave updateLeave = leaveService.updateLeave(id, leave);
        LeaveProjection leaveProjection = ProjectionMapper.mapToLeaveProjection(updateLeave);
        return ResponseEntity.ok(leaveProjection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLeave(@PathVariable("id") Long id){
        if(Objects.isNull(id) || id < 0){
            throw new NullPointerException(INVALID_ID);
        }
        leaveService.deleteLeave(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
