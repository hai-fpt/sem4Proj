package com.lms.service;

import com.lms.dto.Holiday;
import com.lms.dto.projection.HolidayProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.repository.HolidayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private final HolidayRepository holidayRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Page<com.lms.models.Holiday> getAllHolidays(Pageable pageable) {
        return holidayRepository.findAll(pageable);
    }

    @Override
    public Optional<com.lms.models.Holiday> findHolidayById(Long id) {
        return holidayRepository.findById(id);
    }

    @Override
    public com.lms.models.Holiday createHoliday(Holiday holiday) throws DuplicateException {
        String holidayName = holiday.getName();
        Date holidayFromDate = holiday.getFromDate();
        Date holidayToDate = holiday.getToDate();
        com.lms.models.Holiday holidayByNameAndFromDateAndToDate = holidayRepository.findHolidayByNameAndFromDateAndToDate(holidayName, holidayFromDate, holidayToDate);
        if (!Objects.isNull(holidayByNameAndFromDateAndToDate)) {
            throw new DuplicateException("Holiday duplicate with: " + holiday);
        }
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Holiday holidayEntity = modelMapper.map(holiday, com.lms.models.Holiday.class);
        return holidayRepository.save(holidayEntity);
    }

    @Override
    public com.lms.models.Holiday updateHoliday(Long id, Holiday holiday) throws NotFoundByIdException, DuplicateException {
        String holidayName = holiday.getName();
        Date holidayFromDate = holiday.getFromDate();
        Date holidayToDate = holiday.getToDate();
        com.lms.models.Holiday holidayByIdNotAndNameAndFromDateAndToDate = holidayRepository.findHolidayByIdNotAndNameAndFromDateAndToDate(id, holidayName, holidayFromDate, holidayToDate);
        if (!Objects.isNull(holidayByIdNotAndNameAndFromDateAndToDate)) {
            throw new DuplicateException("Holiday duplicate with: " + holiday);
        }
        Optional<com.lms.models.Holiday> holidayOtp = holidayRepository.findById(id);
        if(holidayOtp.isEmpty()) {
                throw new NotFoundByIdException("Holiday not found with id: " + id);
        }
        com.lms.models.Holiday holidayEntity = holidayOtp.get();
        holidayEntity.setName(holiday.getName());
        holidayEntity.setFromDate(holiday.getFromDate());
        holidayEntity.setToDate(holiday.getToDate());
        holidayEntity.setDescription(holiday.getDescription());
        return holidayRepository.save(holidayEntity);
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }

    @Override
    public Page<com.lms.models.Holiday> getAllHolidaysByYear(int year, Pageable pageable) {
        return holidayRepository.findByYear(year, pageable);
    }
}
