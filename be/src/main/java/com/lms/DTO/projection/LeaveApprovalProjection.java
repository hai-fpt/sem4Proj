package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.dto.ApprovalStatus;
import com.lms.models.UserLeave;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface LeaveApprovalProjection {
    Long getId();
    UserLeaveProjection getUserLeave();

    UserProjection getManagerId();

    ApprovalStatus getStatus();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getCreatedDate();

//    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUpdatedDate();

    String getUpdatedBy();
}
