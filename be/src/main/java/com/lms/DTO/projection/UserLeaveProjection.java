package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.dto.ApprovalStatus;
import com.lms.dto.DaysOff;
import com.lms.dto.FileInfo;
import com.lms.models.FileStorage;
import com.lms.models.Leave;

import java.time.LocalDateTime;
import java.util.List;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface UserLeaveProjection {
	Long getId();

	UserProjection getUser();

	Leave getLeave();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getFromDate();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getToDate();

	ApprovalStatus getStatus();

    String getReason();

    String getRejectedReason();

    List<FileStorage> getAttachedFiles();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getCreatedDate();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUpdatedDate();

    String getUpdatedBy();

    Long getDaysOff();
}
