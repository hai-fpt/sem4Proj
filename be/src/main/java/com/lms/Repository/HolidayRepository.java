package com.lms.repository;

import com.lms.dto.projection.HolidayProjection;
import com.lms.models.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Holiday findHolidayByNameAndFromDateAndToDate (String name, LocalDateTime fromDate, LocalDateTime toDate);

    Holiday findHolidayByIdNotAndNameAndFromDateAndToDate (Long id, String name, LocalDateTime fromDate, LocalDateTime toDate);

    Page<HolidayProjection> findAllProjectedBy(Pageable pageable);
    @Query("select h from Holiday h where extract(year from h.fromDate) = :year ")
    List<Holiday> findByYear(@Param("year") int year);
}
