package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lms.dto.ApprovalStatus;
import com.lms.models.UserLeave;
import org.springframework.beans.factory.annotation.Value;

public interface LeaveApprovalProjection {
    Long getId();
    UserLeaveProjection getUserLeave();

    Long getManagerId();

    ApprovalStatus getStatus();
}
