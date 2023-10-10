package com.lms.dto;

import com.lms.models.Leave;
import com.lms.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLeaveDTO {
    private Long id;
    private User user;
    private Leave leave;
    private Date fromDate;
    private Date toDate;
    private Integer status;
    private String reason;
    private String requestedByEmail;
    private List<Long> informTo;
    private List<Long> teamLeads;

}
