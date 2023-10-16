package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.projection.LeaveManagerProjection;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserLeaveService {
    Optional<com.lms.models.UserLeave> getUserLeaveById(Long id);
    UserLeaveProjection createUserLeave(UserLeave userLeave) throws IOException, NotFoundByIdException;

    UserLeaveProjection cancelLeave(UserLeaveCancel userLeaveCancel) throws NotFoundByIdException;

    Page<UserLeaveProjection> getUserLeaveByRole(User user, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByFromDate(DateRange dateRange, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByUser(User user, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByDateRange(DateRange date, Pageable pageable);

    DashboardCalendar getUserLeaveRequestList(Long id, DateRange dateRange);

    List<UserLeaveProjection> getUserLeaveByIdAndStatusAndType(Long id, ApprovalStatus status, boolean affects);

    List<SelfLeave> getSelfLeave(Long id);

    List<LeaveManagerProjection> getUserLeaveManagers(Long id);
}
