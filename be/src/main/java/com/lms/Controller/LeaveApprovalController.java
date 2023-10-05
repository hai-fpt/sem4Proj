package com.lms.Controller;

import com.lms.DTO.LeaveApprovalDTO;
import com.lms.Models.LeaveApproval;
import com.lms.Service.LeaveApprovalServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class LeaveApprovalController {
    private final LeaveApprovalServiceImpl leaveApprovalService;

    public LeaveApprovalController(LeaveApprovalServiceImpl leaveApprovalService) {
        this.leaveApprovalService = leaveApprovalService;
    }

    @PutMapping("/leave/status_update")
    public ResponseEntity<LeaveApproval> updateLeaveApprovalStatus(@RequestBody LeaveApprovalDTO leaveApprovalDTO) {
        leaveApprovalDTO.setUpdatedDate(new Date());
        LeaveApproval leaveApproval = leaveApprovalService.updateLeaveApprovalStatus(leaveApprovalDTO);
        return ResponseEntity.ok(leaveApproval);
    }
}
