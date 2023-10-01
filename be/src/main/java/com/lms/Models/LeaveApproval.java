package com.example.lms.Models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "leave_approval")
public class LeaveApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_leave_id")
    private UserLeave userLeave;

    @Column(name = "manager_id")
    private String managerId;

    //Status: 0: Pending, 1: Approved, 2: Rejected
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserLeave getUserLeave() {
        return userLeave;
    }

    public void setUserLeave(UserLeave userLeave) {
        this.userLeave = userLeave;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
