package com.lms.dto.projection;

import com.lms.dto.ApprovalStatus;

public interface LeaveManagerProjection {
    Long getId();

    String getName();

    ApprovalStatus getStatus();
}
