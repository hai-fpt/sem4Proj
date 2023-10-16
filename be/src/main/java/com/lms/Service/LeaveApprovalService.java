package com.lms.service;

import com.lms.dto.LeaveApproval;
import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface LeaveApprovalService {
    LeaveApprovalProjection getLeaveApprovalById(Long id);
    Optional<com.lms.models.LeaveApproval> getLeaveApprovalByManagerId(Long id, Long managerId);
    LeaveApproval updateLeaveApprovalStatus(LeaveApproval leaveApproval) throws NotFoundByIdException;

    void userLeaveUpdate(com.lms.models.LeaveApproval leaveApproval) throws NotFoundByIdException;

    Page<LeaveApprovalProjection> getLeaveApproveByManagerId(Long id, Pageable pageable);

    Page<LeaveApprovalProjection> searchLeaveApproval(String keyword, Pageable pageable);
    Page<LeaveApprovalProjection> getLeaveApprovalByMonth(Long id, LocalDateTime dateTime, Pageable pageable);
    Page<LeaveApprovalProjection> getLeaveApprovalByDate(Long id, LocalDateTime dateTime, Pageable pageable);
}
