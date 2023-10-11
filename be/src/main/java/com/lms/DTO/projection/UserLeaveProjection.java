package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.dto.ApprovalStatus;
import com.lms.models.FileStorage;
import com.lms.models.Leave;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface UserLeaveProjection {
    Long getId();
    UserProjection getUser();

    Leave getLeave();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getFromDate();

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getToDate();

    ApprovalStatus getStatus();

    List<FileStorage> getAttachedFiles();
}
