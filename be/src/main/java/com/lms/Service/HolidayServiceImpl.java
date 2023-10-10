package com.lms.service;

import com.lms.dto.HolidayDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Holiday;
import com.lms.repository.HolidayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Autowired
    public HolidayServiceImpl(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    @Override
    public Page<Holiday> getAllHolidays(Pageable pageable) {
        return holidayRepository.findAll(pageable);
    }

    @Override
    public Optional<Holiday> findHolidayById(Long id) {
        return holidayRepository.findById(id);
    }

    @Override
    public Holiday createHoliday(HolidayDTO holiday) {
        ModelMapper modelMapper = new ModelMapper();
        Holiday holidayEntity = modelMapper.map(holiday, Holiday.class);
        return holidayRepository.save(holidayEntity);
    }

    @Override
    public Holiday updateHoliday(Long id, HolidayDTO holidayDTO) throws NotFoundByIdException {
        Optional<Holiday> holidayOtp = holidayRepository.findById(id);
        if(holidayOtp.isEmpty()) {
                throw new NotFoundByIdException("Not Found By Id Holiday");
        }
        Holiday holidayEntity = holidayOtp.get();
        holidayEntity.setName(holidayDTO.getName());
        holidayEntity.setFromDate(holidayDTO.getFromDate());
        holidayEntity.setToDate(holidayDTO.getToDate());
        holidayEntity.setDescription(holidayDTO.getDescription());
        return holidayRepository.save(holidayEntity);
    }

    @Override
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }
}
