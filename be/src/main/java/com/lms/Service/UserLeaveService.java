package com.lms.service;

import com.lms.dto.DateRangeDTO;
import com.lms.dto.UserDTO;
import com.lms.dto.UserLeaveCancelDTO;
import com.lms.dto.UserLeaveDTO;
import com.lms.models.User;
import com.lms.models.UserLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserLeaveService {
    Optional<UserLeave> getUserLeaveById(Long id);
    UserLeave createUserLeave(UserLeaveDTO userLeaveDTO);

    UserLeave cancelLeave(UserLeaveCancelDTO userLeaveCancelDTO);

    Page<UserLeave> getUserLeaveByRole(User user, Pageable pageable);

    Page<UserLeave> getUserLeaveByFromDate(DateRangeDTO dateRangeDTO, Pageable pageable);

    Page<UserLeave> getUserLeaveByUser(User user, Pageable pageable);

    ByteArrayInputStream exportExcelUserLeave();

    Page<UserLeave> getUserLeaveByDateRange(DateRangeDTO date, Pageable pageable);

    Page<UserLeave> getUserLeaveByMonth(DateRangeDTO dateRangeDTO, Pageable pageable);

    List<UserLeave> getUserLeaveByIdAndStatus(Long id, int status);
}
