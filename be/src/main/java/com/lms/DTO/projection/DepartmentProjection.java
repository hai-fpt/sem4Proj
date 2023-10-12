package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface DepartmentProjection {
    Long getId();

    String getName();

    User getManager();

    interface User {
        Long getId();

        String getName();
    }
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getCreatedDate();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUpdatedDate();

    String getUpdatedBy();
}
