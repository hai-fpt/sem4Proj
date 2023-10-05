package com.lms.Models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_approval")
@Getter
@Setter
public class LeaveApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_leave_id")
    private UserLeave userLeave;

    @Column(name = "manager_id")
    private String managerId;

    //Status: 1: Pending, 2: Approved, 3: Rejected
    private Integer status;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    public LeaveApproval() {

    }

    public LeaveApproval(UserLeave userLeave, String managerId, Date createdDate, String updatedBy) {
        this.userLeave = userLeave;
        this.managerId = managerId;
        this.createdDate = createdDate;
        this.updatedBy = updatedBy;
        this.status = 0;
    }

}
