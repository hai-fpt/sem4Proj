package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;
@Entity
@Table(name = "users")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_date")
    private Date updatedDate;

    @PrePersist
    private void createdDatePre() {
        this.createdDate = new Date();
        this.updatedDate = new Date();
    }

    @Column(name = "updated_by")
    private String updatedBy;

    @Transient
    private String experience_date;

    @Transient
    private String working_time;

    @Transient
    private String team_alias;

    @Transient
    private String role_alias;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLeave> userLeaves = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();
}
