package com.lms.repository;

import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.models.LeaveApproval;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Page<LeaveApproval> findByUserLeave_Id(Long id, Pageable pageable);

    Page<LeaveApprovalProjection> findAllByManagerId(Long id, Pageable pageable);

    @Query("select la from LeaveApproval la where la.userLeave.id = :id and la.managerId.id = :managerId")
    Optional<LeaveApproval> findByIdAndManagerId(Long id, Long managerId);

    List<LeaveApprovalProjection> findAllByUserLeaveId(Long id);
}
