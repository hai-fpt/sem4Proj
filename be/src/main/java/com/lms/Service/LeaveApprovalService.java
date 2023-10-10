package com.lms.service;

import com.lms.dto.LeaveApprovalDTO;
import com.lms.models.LeaveApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LeaveApprovalService {
    Optional<LeaveApproval> getLeaveApprovalById(Long id);
    LeaveApproval updateLeaveApprovalStatus(LeaveApprovalDTO leaveApprovalDTO);

    Page<LeaveApproval> getLeaveApproveByManagerId(Long id, Pageable pageable);
}
