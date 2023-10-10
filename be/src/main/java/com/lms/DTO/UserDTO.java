package com.lms.dto;

import com.lms.models.Team;
import com.lms.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private String requestedByEmail;

    private Set<Team> teams = new HashSet<>();

    private List<UserRole> userRoles = new ArrayList<>();

}
