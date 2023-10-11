package com.lms.controller;

import com.lms.dto.Holiday;
import com.lms.dto.projection.HolidayProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import com.lms.service.HolidayService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/holiday")
public class HolidayController {

    @Autowired
    private final HolidayService holidayService;

    @Autowired
    private final ControllerUtils controllerUtils;

    public HolidayController(HolidayService holidayService, ControllerUtils controllerUtils) {
        this.holidayService = holidayService;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity<Page<com.lms.models.Holiday>> getAllHolidays(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<com.lms.models.Holiday> holidays = holidayService.getAllHolidays(sorted);
        return ResponseEntity.ok(holidays);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HolidayProjection> getHolidayById(@PathVariable("id") Long id) throws NotFoundByIdException {
        if (Objects.isNull(id) || id < 0 ) {
                throw new NullPointerException("Id invalid");
        }
        Optional<com.lms.models.Holiday> holidayOptional = holidayService.findHolidayById(id);
        com.lms.models.Holiday holiday = holidayOptional.get();
        HolidayProjection holidayProjection = ProjectionMapper.mapToHolidayProjection(holiday);
        return ResponseEntity.status(HttpStatus.OK).body(holidayProjection);
    }

    @PostMapping()
    public ResponseEntity<HolidayProjection> createHoliday(@RequestBody Holiday holidayDTO) throws DuplicateException {
        if (Objects.isNull(holidayDTO)) {
            throw new NullPointerException("Holiday Invalid");
        }
        if(holidayDTO.getName().isEmpty()) {
            throw new NullPointerException("Name invalid");
        }
        if(holidayDTO.getFromDate() == null) {
            throw new NullPointerException("FromDate invalid");
        }
        if(holidayDTO.getToDate() == null){
            throw new NullPointerException("ToDate invalid");
        }
        if(holidayDTO.getFromDate().compareTo(holidayDTO.getToDate()) > 0){
            throw new NullPointerException("FromDate invalid");
        }
        com.lms.models.Holiday newHoliday = holidayService.createHoliday(holidayDTO);
        HolidayProjection holidayProjection = ProjectionMapper.mapToHolidayProjection(newHoliday);
        return  ResponseEntity.status(HttpStatus.CREATED).body(holidayProjection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HolidayProjection> updateHoliday(@PathVariable("id") Long id, @RequestBody Holiday holidayDTO) throws NotFoundByIdException, DuplicateException {
        if(Objects.isNull(holidayDTO)) {
            throw new NullPointerException("Holiday invalid");
        }
        if (Objects.isNull(id) || id < 0) {
            throw new NullPointerException("Id invalid");
        }
        if(holidayDTO.getName().isEmpty()) {
            throw new NullPointerException("Name invalid");
        }
        if(holidayDTO.getFromDate() == null ) {
            throw new NullPointerException("FromDate invalid");
        }
        if(holidayDTO.getToDate() == null) {
            throw new NullPointerException("ToDate invalid");
        }
        if(holidayDTO.getFromDate().compareTo(holidayDTO.getToDate()) > 0) {
            throw new NullPointerException("Date invalid");
        }
            com.lms.models.Holiday holiday = holidayService.updateHoliday(id, holidayDTO);
            HolidayProjection holidayProjection = ProjectionMapper.mapToHolidayProjection(holiday);
            return ResponseEntity.status(HttpStatus.OK).body(holidayProjection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteHoliday(@PathVariable("id") Long id) {
        if (Objects.isNull(id) || id < 0) {
            throw new NullPointerException("Id invalid");
        }
        holidayService.deleteHoliday(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
