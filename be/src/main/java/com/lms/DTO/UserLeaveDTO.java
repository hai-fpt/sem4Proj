package com.lms.DTO;

import com.lms.Models.Leave;
import com.lms.Models.User;

import java.util.Date;
import java.util.List;

public class UserLeaveDTO {
    private Long id;
    private User user;
    private Leave leave;
    private Date fromDate;
    private Date toDate;
    private Integer status;
    private Date createdDate;
    private Date updatedDate;
    private Date updatedBy;

    private List<String> teamLeads;

    public List<String> getTeamLeads() {
        return teamLeads;
    }

    public void setTeamLeads(List<String> teamLeads) {
        this.teamLeads = teamLeads;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Leave getLeave() {
        return leave;
    }

    public void setLeave(Leave leave) {
        this.leave = leave;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
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

    public Date getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Date updatedBy) {
        this.updatedBy = updatedBy;
    }
}
