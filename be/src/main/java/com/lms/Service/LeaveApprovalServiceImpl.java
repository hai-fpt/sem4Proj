package com.lms.service;

import com.lms.dto.LeaveApprovalDTO;
import com.lms.models.LeaveApproval;
import com.lms.models.User;
import com.lms.models.UserLeave;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class LeaveApprovalServiceImpl implements LeaveApprovalService{

    private final LeaveApprovalRepository leaveApprovalRepository;
    private final UserLeaveRepository userLeaveRepository;


    public LeaveApprovalServiceImpl(LeaveApprovalRepository leaveApprovalRepository, UserLeaveRepository userLeaveRepository) {
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.userLeaveRepository = userLeaveRepository;
    }

    @Override
    public Optional<LeaveApproval> getLeaveApprovalById(Long id) {
        return leaveApprovalRepository.findById(id);
    }

    @Override
    public LeaveApproval updateLeaveApprovalStatus(LeaveApprovalDTO leaveApprovalDTO) {
        Optional<LeaveApproval> leaveApprovalSearch = leaveApprovalRepository.findById(leaveApprovalDTO.getId());
        LeaveApproval leaveApproval = leaveApprovalSearch.get();
        leaveApproval.setStatus(leaveApprovalDTO.getStatus());
        leaveApproval.setUpdatedBy(leaveApprovalDTO.getRequestedByEmail());
        LeaveApproval savedEntity = leaveApprovalRepository.save(leaveApproval);

        if (leaveApproval.getStatus() == 2) {
            Page<LeaveApproval> approvals = leaveApprovalRepository.findByUserLeave_Id(leaveApproval.getUserLeave().getId(), PageRequest.of(0, 10));
            for (LeaveApproval approval : approvals) {
                if (approval.getStatus() != 2) {
                    break;
                }
                Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveApproval.getUserLeave().getId());
                if (userLeave.isPresent()) {
                    UserLeave updatedLeave = userLeave.get();
                    updatedLeave.setStatus(2);
                    userLeaveRepository.save(updatedLeave);
                } else {
                    throw new NullPointerException("User leave request does not exists");
                }
            }
        }
        return savedEntity;
    }

    @Override
    public Page<LeaveApproval> getLeaveApproveByManagerId(Long id, Pageable pageable) {
        return leaveApprovalRepository.findAllByManagerId(id, pageable);
    }

    //TODO: list user leaveApproval by id
    //TODO: click on date and show what requests there are

}
