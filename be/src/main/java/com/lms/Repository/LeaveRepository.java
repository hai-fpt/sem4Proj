package com.lms.repository;

import com.lms.dto.projection.LeaveProjection;
import com.lms.models.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Leave findLeaveByName(String name);

    Leave findLeaveByIdNotAndName(Long id, String name);

    Page<LeaveProjection> findAllProjectedBy(Pageable pageable);
}
