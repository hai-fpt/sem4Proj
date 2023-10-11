package com.lms.utils;

import com.lms.dto.projection.UserLeaveProjection;
import com.lms.models.Holiday;
import com.lms.models.UserLeave;
import com.lms.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Component
public class DateCalculation {
    public long calculateWeekendsBetween(LocalDate fromDate, LocalDate toDate) {
        long weekends = 0;
        LocalDate current = fromDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        while (current.isBefore(toDate) || current.isEqual(toDate)) {
            if (current.getDayOfWeek() == DayOfWeek.SATURDAY || current.getDayOfWeek() == DayOfWeek.SUNDAY) {
                weekends++;
            }
            current = current.plusDays(1);
        }
        return weekends;
    }

    public int holidaysCompensation(List<UserLeaveProjection> userLeaves, Page<Holiday> holidays, LocalDateTime fromDate, LocalDateTime toDate) {
        int compensation = 0;
        for (Holiday holiday : holidays) {
            LocalDate holidayStart = holiday.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate holidayEnd = holiday.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (holidayStart.getYear() == fromDate.getYear() && holidayEnd.getYear() == toDate.getYear()) {
                for (UserLeaveProjection leave : userLeaves) {
                    LocalDate leaveFrom = leave.getFromDate().toLocalDate();
                    LocalDate leaveTo = leave.getToDate().toLocalDate();

                    if (!leaveFrom.isAfter(holidayEnd) && !leaveTo.isBefore(holidayStart)) {
                        LocalDate overlapStart = leaveFrom.isAfter(holidayStart) ? leaveFrom : holidayStart;
                        LocalDate overlapEnd = leaveTo.isBefore(holidayEnd) ? leaveTo : holidayEnd;

                        long overlappingDays = ChronoUnit.DAYS.between(overlapStart, overlapEnd);
                        compensation += (int) overlappingDays;
                    }
                }
            }
        }
        return compensation;
    }

    public float calculateTotalDaysOff(List<UserLeaveProjection> userLeaves, LocalDateTime fromDate, LocalDateTime toDate) {
        float totalDaysOff = 0;
        for (UserLeaveProjection leave : userLeaves) {
            LocalDateTime leaveFrom = leave.getFromDate();
            LocalDateTime leaveTo = leave.getToDate();

            if (leaveFrom.isAfter(toDate) || leaveTo.isBefore(fromDate)) {
                continue;
            }

            LocalDateTime rangeFrom = leaveFrom.isAfter(fromDate) ? leaveFrom : fromDate;
            LocalDateTime rangeTo = leaveTo.isBefore(toDate) ? leaveTo : toDate;

            long leaveDuration = ChronoUnit.DAYS.between(rangeFrom.toLocalDate(), rangeTo.toLocalDate());
            totalDaysOff += leaveDuration;
            long weekendsBetween = calculateWeekendsBetween(rangeFrom.toLocalDate(), rangeTo.toLocalDate());
            totalDaysOff -= weekendsBetween;
        }
        return 0;
    }

    public float calculateUsedDaysOff(List<UserLeaveProjection> userLeaves, LocalDateTime fromDate, LocalDateTime toDate) {
        float usedDaysOff = 0;
        for (UserLeaveProjection request : userLeaves) {
            LocalDateTime fromDateRequest = request.getFromDate();
            LocalDateTime toDateRequest = request.getToDate();

            //If the request is made to end within the year
            if (fromDateRequest.getYear() == toDateRequest.getYear()) {
                if (fromDateRequest.getYear() == fromDate.getYear()) {
                    if (fromDateRequest.toLocalDate().isEqual(toDateRequest.toLocalDate())) {
                        Duration duration = Duration.between(fromDateRequest, toDateRequest);
                        long hourDiff = duration.toHours();

                        if (hourDiff <= 4) {
                            usedDaysOff += 0.5;
                        } else {
                            usedDaysOff += 1;
                        }

                    } else {
                        long daysBetween = ChronoUnit.DAYS.between(fromDateRequest.toLocalDate(), toDateRequest.toLocalDate());
                        usedDaysOff += daysBetween;
                        long weekendBetween = calculateWeekendsBetween(fromDateRequest.toLocalDate(), toDateRequest.toLocalDate());
                        usedDaysOff -= weekendBetween;
                    }
                }
                //If the request is made at the end of the year and bleeds into the next
            } else {
                if (fromDateRequest.getYear() == fromDate.getYear()) {
                    LocalDateTime yearEnd = LocalDateTime.of(fromDate.getYear(), 12, 31, 23, 59, 59);
                    long daysBetween = ChronoUnit.DAYS.between(fromDateRequest.toLocalDate(), yearEnd.toLocalDate());
                    usedDaysOff += daysBetween;
                    long weekendBetween = calculateWeekendsBetween(fromDateRequest.toLocalDate(), yearEnd.toLocalDate());
                    usedDaysOff -= weekendBetween;
                } else if (toDateRequest.getYear() == fromDate.getYear()) {
                    LocalDateTime yearStart = LocalDateTime.of(fromDate.getYear(), 1, 1, 0, 0, 0);
                    long daysBetween = ChronoUnit.DAYS.between(yearStart.toLocalDate(), toDateRequest.toLocalDate());
                    usedDaysOff += daysBetween;
                    long weekendBetween = calculateWeekendsBetween(yearStart.toLocalDate(), toDateRequest.toLocalDate());
                    usedDaysOff -= weekendBetween;
                }
            }
        }
        return usedDaysOff;
    }

    public long calculateSingleUserLeaveDaysOff(UserLeave userLeaveProjection, Page<Holiday> holidays) {
        LocalDateTime fromDate = userLeaveProjection.getFromDate();
        LocalDateTime toDate = userLeaveProjection.getToDate();
        if (fromDate.toLocalDate().isEqual(toDate.toLocalDate())) {
            return 1;
        }
        long daysOff = ChronoUnit.DAYS.between(fromDate.toLocalDate(), toDate.toLocalDate());

        for (Holiday holiday : holidays) {
            LocalDateTime holidayFrom = holiday.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime holidayTo = holiday.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            if (fromDate.isBefore(holidayTo) && toDate.isAfter(holidayFrom)) {
                LocalDateTime overlapFrom = fromDate.isBefore(holidayFrom) ? holidayFrom : fromDate;
                LocalDateTime overlapTo = toDate.isAfter(holidayTo) ? holidayTo : toDate;
                long overlap = ChronoUnit.DAYS.between(overlapFrom.toLocalDate(), overlapTo.toLocalDate());

                daysOff -= overlap;
            }
        }
        return daysOff;
    }
}
