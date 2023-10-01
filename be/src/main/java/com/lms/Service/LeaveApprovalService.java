package com.example.lms.Service;

import com.example.lms.DTO.LeaveApprovalDTO;
import com.example.lms.Models.LeaveApproval;

public interface LeaveApprovalService {
    LeaveApproval updateLeaveApprovalStatus(LeaveApprovalDTO leaveApprovalDTO);
}
