package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime startDate;
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime endDate;
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime singleDate;
}
