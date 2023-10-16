package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelfLeave {
    Long id;
    String title;
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)

    LocalDateTime start;
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)

    LocalDateTime end;

    ApprovalStatus status;
}
