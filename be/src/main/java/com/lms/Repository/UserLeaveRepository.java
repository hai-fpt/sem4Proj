package com.lms.repository;

import com.lms.models.User;
import com.lms.models.UserLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserLeaveRepository extends JpaRepository<UserLeave, Long> {
    Page<UserLeave> findUserLeaveByUser(User user, Pageable pageable);

    Page<UserLeave> findUserLeaveByFromDate(Date date, Pageable pageable);

    @Query("select ul from UserLeave ul " +
            "join ul.user u " +
            "join u.userTeams ut " +
            "join ut.team t " +
            "where t.manager.id = :id")
    Page<UserLeave> getUserLeaveByTeam(@Param("id") Long id, Pageable pageable);

    @Query("select ul from UserLeave ul where :date between ul.fromDate and ul.toDate")
    Page<UserLeave> findUserLeaveByDate(Date date, Pageable pageable);

    @Query(value = "select * from user_leave ul " +
            "where extract(month from ul.from_date) " +
            "= extract(month from cast(:date as timestamp)) " +
            "or extract(month from ul.to_date) " +
            "= extract(month from cast(:date as timestamp))"
            , nativeQuery = true)
    Page<UserLeave> findUserLeaveByMonth(@Param("date") Date date, Pageable pageable);

    List<UserLeave> findUserLeaveByUserIdAndStatus(Long id, int status);
}
