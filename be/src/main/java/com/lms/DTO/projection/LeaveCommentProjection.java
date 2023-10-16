package com.lms.dto.projection;

import java.time.LocalDateTime;

public interface LeaveCommentProjection {
    Long getId();

    String getAuthor();

    String getComment();

    LocalDateTime getCreatedDate();
}
