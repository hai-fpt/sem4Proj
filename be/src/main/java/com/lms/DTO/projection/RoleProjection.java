package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.lms.models.Role;

import java.util.Date;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface RoleProjection {
    Long getId();
    Role.RoleEnum getName();
    String getDescription();
}
