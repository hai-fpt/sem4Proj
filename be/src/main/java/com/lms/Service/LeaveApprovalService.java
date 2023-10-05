package com.lms.Service;

import com.lms.DTO.LeaveApprovalDTO;
import com.lms.Models.LeaveApproval;

public interface LeaveApprovalService {
    LeaveApproval updateLeaveApprovalStatus(LeaveApprovalDTO leaveApprovalDTO);
}
