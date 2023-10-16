package com.lms.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.lms.dto.RankEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements UserDetails {

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RankEnum rank;

    @Column(name = "joined_date")
    private LocalDateTime joinedDate;

    private String department;


    @Column(nullable = false)
    private Boolean status;

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

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserLeave> userLeaves = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.PERSIST)
    private Avatar avatar;

    public String getExperienceDateAsString() {
        return getPeriod(getUniversityGraduateDate());
    }

    public String getWorkingTimeAsString() {
        return getPeriod(getJoinedDate());
    }

    public <T> String getPeriod(T date){
        LocalDateTime joined = (LocalDateTime) date;
        if(date != null){
            Period time = Period.between(joined.toLocalDate(), LocalDate.now());
            return time.getYears() + " Years, " + time.getMonths() + " Months";
        }
        return null;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    @Transient
    public String getPassword() {
        return null;
    }

    @Override
    @Transient
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}
