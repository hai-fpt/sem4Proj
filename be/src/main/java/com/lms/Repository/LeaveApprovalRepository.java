package com.lms.repository;

import com.lms.models.LeaveApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Page<LeaveApproval> findByUserLeave_Id(Long id, Pageable pageable);

    Optional<LeaveApproval> findByManagerId(Long id);

    Page<LeaveApproval> findAllByManagerId(Long id, Pageable pageable);
}
