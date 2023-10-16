package com.lms.repository;

import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.models.LeaveApproval;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    Page<LeaveApproval> findByUserLeave_Id(Long id, Pageable pageable);

    @Query("select la from LeaveApproval la where la.managerId.id = :id order by la.updatedDate DESC ")
    List<LeaveApproval> findAllByManagerId(@Param("id") Long id);

    @Query("select la from LeaveApproval la where la.userLeave.id = :id and la.managerId.id = :managerId")
    Optional<LeaveApproval> findByIdAndManagerId(Long id, Long managerId);

    List<LeaveApprovalProjection> findAllByUserLeaveId(Long id);

    @Query("select la from LeaveApproval la where " +
            "(lower(la.userLeave.user.name) like %:keyword% " +
            "or lower(la.userLeave.user.email) like %:keyword%)")
    List<LeaveApprovalProjection> searchLeaveApproval(@Param("keyword") String keyword);

    @Query(nativeQuery = true,
    value = "select la.* from leave_approval la join user_leave ul on ul.id = la.user_leave_id " +
            "where la.manager_id = :id " +
            "and extract(month from ul.from_date) = extract(month from cast(:dateParam as date))")
    List<LeaveApproval> getLeaveApprovalByMonth(@Param("id") Long id, @Param("dateParam") LocalDateTime date);

    @Query(nativeQuery = true,
    value = "select la.* from leave_approval la join user_leave ul on ul.id = la.user_leave_id " +
            "where la.manager_id = :id " +
            "and cast(:date as date) between ul.from_date and ul.to_date " +
            "or la.manager_id = :id and cast(:date as date) = cast(ul.from_date as date) " +
            "or la.manager_id = :id and cast(:date as date) = cast(ul.to_date as date)")
    List<LeaveApproval> getLeaveApprovalByDay(@Param("id") Long id, @Param("date") LocalDateTime date);
}
