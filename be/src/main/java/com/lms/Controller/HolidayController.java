package com.lms.controller;

import com.lms.dto.HolidayDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Holiday;
import com.lms.service.HolidayServiceImpl;
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

    private final HolidayServiceImpl holidayServiceImpl;

    private final ControllerUtils controllerUtils;

    @Autowired
    public HolidayController(HolidayServiceImpl holidayServiceImpl, ControllerUtils controllerUtils) {
        this.holidayServiceImpl = holidayServiceImpl;
        this.controllerUtils = controllerUtils;
    }

    @GetMapping()
    public ResponseEntity getAllHolidays(@PageableDefault(page = 0, size = 10)Pageable pageable) {
        Pageable sorted = controllerUtils.sortPage(pageable, "updatedDate");
        Page<Holiday> holidays = holidayServiceImpl.getAllHolidays(sorted);
        return ResponseEntity.ok(holidays);
    }


    @GetMapping("/{id}")
    public ResponseEntity getHolidayById(@PathVariable("id") Long id) {
        if (Objects.isNull(id) || id < 0 ) {
            String message = "Id Invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Optional<Holiday> holiday = holidayServiceImpl.findHolidayById(id);
        return holiday.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity createHoliday(@RequestBody HolidayDTO holidayDTO) {
        if (Objects.isNull(holidayDTO)) {
            String message = "Holiday Invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getName().isEmpty()) {
            String message = "Name invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getFromDate() == null) {
            String message = "FromDate invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getToDate() == null){
            String message = "ToDate invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getFromDate().compareTo(holidayDTO.getToDate()) > 0){
            String message = "Date invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        Holiday newHoliday = holidayServiceImpl.createHoliday(holidayDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(newHoliday);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateHoliday(@PathVariable("id") Long id, @RequestBody HolidayDTO holidayDTO) throws NotFoundByIdException {
        if(Objects.isNull(holidayDTO)) {
            String message = "Holiday invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (Objects.isNull(id) || id < 0) {
            String message = "Id invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getName().isEmpty()) {
            String message = "Name invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getFromDate() == null ) {
            String message = "FromDate invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getToDate() == null) {
            String message = "ToDate invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if(holidayDTO.getFromDate().compareTo(holidayDTO.getToDate()) > 0) {
            String message = "Date invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
            Holiday holiday = holidayServiceImpl.updateHoliday(id, holidayDTO);
            return ResponseEntity.status(HttpStatus.OK).body(holiday);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteHoliday(@PathVariable("id") Long id) {
        if (Objects.isNull(id) || id < 0) {
            String message = "Id invalid";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        holidayServiceImpl.deleteHoliday(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
