package com.lms.repository;

import com.lms.dto.ApprovalStatus;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.models.User;
import com.lms.models.UserLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface UserLeaveRepository extends JpaRepository<UserLeave, Long> {
    Page<UserLeaveProjection> findAllProjectedBy(Pageable pageable);

    List<UserLeave> findUserLeaveByUser(User user);

    Page<UserLeaveProjection> findUserLeaveByFromDate(LocalDateTime date, Pageable pageable);

    @Query("select ul from UserLeave ul " +
            "join ul.user u " +
            "join u.userTeams ut " +
            "join ut.team t " +
            "where t.manager.id = :id")
    Page<UserLeaveProjection> getUserLeaveByTeam(@Param("id") Long id, Pageable pageable);

    @Query("select ul from UserLeave ul " +
            "join ul.user u " +
            "join u.userTeams ut " +
            "join ut.team t " +
            "where t.manager.id = :id")
    List<UserLeave> getUserLeaveByTeam(@Param("id") Long id);

    @Query("select ul from UserLeave ul where :date between ul.fromDate and ul.toDate")
    Page<UserLeaveProjection> findUserLeaveByDate(LocalDateTime date, Pageable pageable);

    @Query("SELECT ul FROM UserLeave ul WHERE ul.fromDate <= :endDate AND ul.toDate >= :startDate")
    Page<UserLeaveProjection> findUserLeavesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, Pageable pageable);

    @Query("select ul from UserLeave ul where ul.fromDate <= :endDate and ul.toDate >= :startDate and ul.user.id = :id")
    List<UserLeaveProjection> findUserLeaveBetweenDatesByUserId(@Param("id") Long id, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<UserLeaveProjection> findUserLeaveByUserIdAndStatusAndAndLeave_AffectsDaysOff(Long id, ApprovalStatus status, boolean affect);
}
