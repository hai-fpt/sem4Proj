package com.lms.dto.projection;

public interface DepartmentProjection {
    Long getId();

    String getName();

    User getManager();

    interface User {
        Long getId();

        String getName();
    }
}
