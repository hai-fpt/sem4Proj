package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.LocalDateTime;
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

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(nullable = false)
    private String email;

    private String phone;

    private String university;

    @Column(name = "university_code")
    private String universityCode;

    @Column(name = "university_graduate_date")
    private LocalDateTime universityGraduateDate;

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

    @Column(name = "joined_date")
    private LocalDateTime joinedDate;

    private String department;


    @Column(nullable = false)
    private boolean status;

    @Column(name = "resigned_date")
    private LocalDateTime resignedDate;

    @Column(nullable = false, updatable = false, name = "created_date")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Transient
    private String experienceDate;

    @Transient
    private String workingTime;

    @Transient
    private List<String> team_alias;

    @Transient
    private List<String> role_alias;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLeave> userLeaves = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    public Period getPeriodTime(LocalDateTime date) {
        return Period.between(date.toLocalDate(), LocalDate.now());
    }
    public String getExperienceDateAsString() {
        LocalDateTime university_graduate_date= getUniversityGraduateDate();
        if(university_graduate_date == null){
            return null;
        }
        return getPeriodTime(university_graduate_date).getYears() + " Years, " + getPeriodTime(university_graduate_date).getMonths() + " Months";
    }

    public String getWorkingTimeAsString(){
        LocalDateTime joinedDate = getJoinedDate();
        if(joinedDate == null){
            return null;
        }
        return getPeriodTime(joinedDate).getYears() + " Years, " + getPeriodTime(joinedDate).getMonths() + " Months";
    }
}
