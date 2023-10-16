package com.lms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime dateOfBirth;

    private String email;

    private String phone;

    private String university;

    private String universityCode;

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime universityGraduateDate;

    private String skills;

    private RankEnum rank;

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime joinedDate;

    private String department;


    private Boolean status;

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    private LocalDateTime resignedDate;

    private String updatedBy;

    private List<Team> teams = new ArrayList<>();

    private List<UserRole> userRoles = new ArrayList<>();

}
