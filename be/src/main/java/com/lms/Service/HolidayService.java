package com.lms.service;

import com.lms.dto.Holiday;
import com.lms.dto.projection.HolidayProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HolidayService{
    Page<com.lms.models.Holiday> getAllHolidays(Pageable pageable);

    Optional<com.lms.models.Holiday> findHolidayById(Long id) throws NotFoundByIdException;

    com.lms.models.Holiday createHoliday(Holiday holiday) throws DuplicateException;

    com.lms.models.Holiday updateHoliday(Long id, Holiday holiday) throws NotFoundByIdException, DuplicateException;

    void deleteHoliday(Long id);

    Page<com.lms.models.Holiday> getAllHolidaysByYear(int year, Pageable pageable);
}
