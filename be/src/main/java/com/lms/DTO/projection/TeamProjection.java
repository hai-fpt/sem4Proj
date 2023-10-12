package com.lms.dto.projection;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface TeamProjection {
    Long getId();
    String getTeamName();

    String getDescription();

    ManagerProjection getManager();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getCreatedDate();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUpdatedDate();

    String getUpdatedBy();

//    List<UserTeamUserProjection> getUserTeams();
}
