package com.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApproval {
    private Long id;
    private Long managerId;
    private ApprovalStatus status;
    private String description;
    private String updatedBy;

}
