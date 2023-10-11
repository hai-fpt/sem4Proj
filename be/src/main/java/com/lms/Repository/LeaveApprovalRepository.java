package com.lms.repository;

import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.models.LeaveApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Page<LeaveApprovalProjection> findByUserLeave_Id(Long id, Pageable pageable);

    Page<LeaveApprovalProjection> findAllByManagerId(Long id, Pageable pageable);
}
