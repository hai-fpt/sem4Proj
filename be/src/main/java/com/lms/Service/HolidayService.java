package com.lms.service;

import com.lms.dto.HolidayDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface HolidayService{
    Page<Holiday> getAllHolidays(Pageable pageable);

    Optional<Holiday> findHolidayById(Long id) throws NotFoundByIdException;

    Holiday createHoliday(HolidayDTO holiday);

    Holiday updateHoliday(Long id, HolidayDTO holiday) throws NotFoundByIdException;

    void deleteHoliday(Long id);
}
