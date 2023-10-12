package com.lms.service;

import com.lms.dto.Holiday;
import com.lms.dto.projection.HolidayProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.repository.HolidayRepository;
import com.lms.utils.ProjectionMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    public Page<HolidayProjection> getAllHolidayProjection(Pageable pageable) {
        return holidayRepository.findAllProjectedBy(pageable);
    }

    @Override
    public Optional<com.lms.models.Holiday> findHolidayById(Long id) {
        return holidayRepository.findById(id);
    }

    @Override
    public com.lms.models.Holiday createHoliday(Holiday holiday) throws DuplicateException {
        String holidayName = holiday.getName();
        LocalDateTime holidayFromDate = holiday.getFromDate();
        LocalDateTime holidayToDate = holiday.getToDate();
        com.lms.models.Holiday holidayByNameAndFromDateAndToDate = holidayRepository.findHolidayByNameAndFromDateAndToDate(holidayName, holidayFromDate, holidayToDate);
        if (!Objects.isNull(holidayByNameAndFromDateAndToDate)) {
            throw new DuplicateException("Holiday duplicate with: " + holidayName);
        }
        ModelMapper modelMapper = new ModelMapper();
        com.lms.models.Holiday holidayEntity = modelMapper.map(holiday, com.lms.models.Holiday.class);
        return holidayRepository.save(holidayEntity);
    }

    @Override
    public com.lms.models.Holiday updateHoliday(Long id, Holiday holiday) throws NotFoundByIdException, DuplicateException {
        String holidayName = holiday.getName();
        LocalDateTime holidayFromDate = holiday.getFromDate();
        LocalDateTime holidayToDate = holiday.getToDate();
        com.lms.models.Holiday holidayByIdNotAndNameAndFromDateAndToDate = holidayRepository.findHolidayByIdNotAndNameAndFromDateAndToDate(id, holidayName, holidayFromDate, holidayToDate);
        if (!Objects.isNull(holidayByIdNotAndNameAndFromDateAndToDate)) {
            throw new DuplicateException("Holiday duplicate with: " + holidayName);
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
    public Page<HolidayProjection> getAllHolidaysByYear(int year, Pageable pageable) {
//        List<com.lms.models.Holiday> holidays = holidayRepository.findByYear(year);
//        ModelMapper modelMapper = new ModelMapper();
//        List<Holiday> holidayList = new ArrayList<>();
//        for (com.lms.models.Holiday holiday : holidays) {
//            Holiday holiday1 = modelMapper.map(holiday, Holiday.class);
//            holidayList.add(holiday1);
//        }
//        return new PageImpl<>(holidayList, pageable, holidayList.size());
        //FIX TO PROJECTION
        List<com.lms.models.Holiday> holidays = holidayRepository.findByYear(year);
        List<HolidayProjection> holidayProjections = new ArrayList<>();
        for (com.lms.models.Holiday holiday : holidays) {
            HolidayProjection holidayProjection = ProjectionMapper.mapToHolidayProjection(holiday);
            holidayProjections.add(holidayProjection);
        }
        return new PageImpl<>(holidayProjections, pageable, holidayProjections.size());
    }
}
