package com.lms.service;

import com.lms.dto.LeaveApproval;
import com.lms.dto.projection.LeaveApprovalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LeaveApprovalService {
    LeaveApprovalProjection getLeaveApprovalById(Long id);
    LeaveApprovalProjection updateLeaveApprovalStatus(LeaveApproval leaveApproval);

    void userLeaveUpdate(com.lms.models.LeaveApproval leaveApproval);

    Page<LeaveApprovalProjection> getLeaveApproveByManagerId(Long id, Pageable pageable);
}
