package com.lms.dto.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lms.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

public interface UserProjection {
    Long getId();
    String getName();
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getDateOfBirth();
    String getEmail();
    String getPhone();
    String getUniversity();
    String getUniversityCode();
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getUniversityGraduateDate();
    String getSkills();
    User.RankEnum getRank();
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime getJoinedDate();
    String getDepartment();
    boolean isStatus();
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
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
}
