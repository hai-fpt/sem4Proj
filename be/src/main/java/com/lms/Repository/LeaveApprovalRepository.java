package com.lms.Repository;

import com.lms.Models.LeaveApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Page<LeaveApproval> findByUserLeave_Id(Long id, Pageable pageable);
}
