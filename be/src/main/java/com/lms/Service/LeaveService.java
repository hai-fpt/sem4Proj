package com.lms.service;

import com.lms.dto.LeaveDTO;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Leave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface LeaveService {

    Page<Leave> getAllLeaves(Pageable pageable);

    Optional<Leave> findLeaveById(Long id) throws NotFoundByIdException;

    Leave createLeave(LeaveDTO leave);

    Leave updateLeave(Long id, LeaveDTO leave) throws NotFoundByIdException;

    void deleteLeave(Long id);
}
