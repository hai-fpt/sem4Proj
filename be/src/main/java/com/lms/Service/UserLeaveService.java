package com.lms.service;

import com.lms.dto.ApprovalStatus;
import com.lms.dto.DateRange;
import com.lms.dto.UserLeaveCancel;
import com.lms.dto.UserLeave;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserLeaveService {
    Optional<com.lms.models.UserLeave> getUserLeaveById(Long id);
    UserLeaveProjection createUserLeave(UserLeave userLeave) throws IOException;

    UserLeaveProjection cancelLeave(UserLeaveCancel userLeaveCancel);

    Page<UserLeaveProjection> getUserLeaveByRole(User user, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByFromDate(DateRange dateRange, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByUser(User user, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByDateRange(DateRange date, Pageable pageable);

    Page<UserLeaveProjection> getUserLeaveByMonth(DateRange dateRange, Pageable pageable);

    List<UserLeaveProjection> getUserLeaveByIdAndStatusAndType(Long id, ApprovalStatus status, boolean affects);
}
