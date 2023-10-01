package com.example.lms.Models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.*;
//TODO: Create one to many to intermediate table for user_role, and table userRole
@Entity
@Table(name = "users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date date_of_birth;

    @Column(nullable = false)
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankEnum rank;

    private Date joined_date;

    private String department;


    @Column(nullable = false)
    private boolean status;

    private Date resigned_date;

    @Column(nullable = false, updatable = false, name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @PrePersist
    private void createdDatePre() {
        this.createdDate = new Date();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Transient
    private String experience_date;

    @Transient
    private String working_time;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserLeave> userLeaves = new ArrayList<>();


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

    public List<UserTeam> getUserTeams() {
        return userTeams;
    }

    public void setUserTeams(List<UserTeam> userTeams) {
        this.userTeams = userTeams;
    }

    public List<UserLeave> getUserLeaves() {
        return userLeaves;
    }

    public void setUserLeaves(List<UserLeave> userLeaves) {
        this.userLeaves = userLeaves;
    }
}
