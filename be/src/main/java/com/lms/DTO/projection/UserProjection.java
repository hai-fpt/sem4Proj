package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.dto.RankEnum;
import com.lms.models.Role;
import com.lms.models.User;
import com.lms.models.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;
import java.util.List;

import static com.lms.utils.Constants.JSON_VIEW_DATE_FORMAT;

public interface UserProjection {
    Long getId();
    String getName();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getDateOfBirth();
    String getEmail();
    String getPhone();
    String getUniversity();
    String getUniversityCode();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUniversityGraduateDate();
    String getSkills();
    RankEnum getRank();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getJoinedDate();
    String getDepartment();
    Boolean getStatus();
    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getResignedDate();

    default String getExperienceDate() {
        LocalDateTime uniGrad = getUniversityGraduateDate();
        if (uniGrad != null) {
            Period expTime = Period.between(uniGrad.toLocalDate(), LocalDate.now());
            return expTime.getYears() + " Years," + expTime.getMonths() + " Months";
        }
        return "";
    }

    default String getWorkingTime() {
        LocalDateTime joined = getJoinedDate();
        if (joined != null) {
            Period workTime = Period.between(joined.toLocalDate(), LocalDate.now());
            return workTime.getYears() + " Years, " + workTime.getMonths() + " Months";
        }
        return "";
    }

    List<UserRoleProjection> getUserRoles();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getCreatedDate();

    @JsonFormat(pattern = JSON_VIEW_DATE_FORMAT)
    LocalDateTime getUpdatedDate();

    String getUpdatedBy();

    List<UserTeamTeamProjection> getUserTeams();

    AvatarProjection getAvatar();
}
