package com.lms.service;

import com.lms.dto.Leave;
import com.lms.dto.projection.LeaveProjection;
import com.lms.exception.DuplicateException;
import com.lms.exception.NotFoundByIdException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LeaveService {

    Page<LeaveProjection> getAllLeaves(Pageable pageable);

    Optional<com.lms.models.Leave> findLeaveById(Long id);

    com.lms.models.Leave createLeave(Leave leave) throws DuplicateException;

    com.lms.models.Leave updateLeave(Long id, Leave leave) throws NotFoundByIdException, DuplicateException;

    void deleteLeave(Long id);

}
