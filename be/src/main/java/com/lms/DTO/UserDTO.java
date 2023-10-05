package com.lms.DTO;

import com.lms.Models.Team;
import com.lms.Models.UserRole;

import java.util.*;

public class UserDTO {

    private Long id;

    private String name;

    private Date date_of_birth;

    private String email;

    private String phone;

    private String university;

    private String university_code;

    private Date university_graduate_date;

    private String skills;

    public enum RankEnum {
        SENIOR_MANAGER,
        MANAGER,
        ASSISTANT_MANAGER,
        EMPLOYEE
    }

    private RankEnum rank;

    private Date joined_date;

    private String department;

    private String team;

    private boolean status;

    private Date resigned_date;

    private Date createdDate;

    private Date updatedDate;

    private String updatedBy;

    private String experience_date;

    private String working_time;

    private Set<Team> teams = new HashSet<>();

    private List<UserRole> userRoles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getUniversity_code() {
        return university_code;
    }

    public void setUniversity_code(String university_code) {
        this.university_code = university_code;
    }

    public Date getUniversity_graduate_date() {
        return university_graduate_date;
    }

    public void setUniversity_graduate_date(Date university_graduate_date) {
        this.university_graduate_date = university_graduate_date;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public RankEnum getRank() {
        return rank;
    }

    public void setRank(RankEnum rank) {
        this.rank = rank;
    }

    public Date getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(Date joined_date) {
        this.joined_date = joined_date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getResigned_date() {
        return resigned_date;
    }

    public void setResigned_date(Date resigned_date) {
        this.resigned_date = resigned_date;
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

    public String getExperience_date() {
        return experience_date;
    }

    public void setExperience_date(String experience_date) {
        this.experience_date = experience_date;
    }

    public String getWorking_time() {
        return working_time;
    }

    public void setWorking_time(String working_time) {
        this.working_time = working_time;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}
