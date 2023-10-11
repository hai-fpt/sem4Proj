package com.lms.dto.projection;

import java.time.LocalDateTime;

public interface HolidayProjection {
    Long getId();
    String getName();

    String getDescription();

    LocalDateTime getFromDate();

    LocalDateTime getToDate();
}
