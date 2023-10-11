package com.lms.dto.projection;


public interface TeamProjection {
    Long getId();
    String getTeamName();

    String getDescription();

    ManagerProjection getManager();
}
