package com.example.lms.Service;

import com.example.lms.DTO.LeaveApprovalDTO;
import com.example.lms.Models.LeaveApproval;
import com.example.lms.Models.UserLeave;
import com.example.lms.Repository.LeaveApprovalRepository;
import com.example.lms.Repository.UserLeaveRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
    public LeaveApproval updateLeaveApprovalStatus(LeaveApprovalDTO leaveApprovalDTO) {
        Optional<LeaveApproval> leaveApprovalSearch = leaveApprovalRepository.findById(leaveApprovalDTO.getId());
        if (leaveApprovalSearch.isPresent()) {
            LeaveApproval leaveApproval = leaveApprovalSearch.get();
            leaveApproval.setStatus(leaveApprovalDTO.getStatus());
            leaveApproval.setUpdatedBy(leaveApprovalDTO.getUpdatedBy());
            leaveApproval.setUpdatedDate(leaveApprovalDTO.getUpdatedDate());
            LeaveApproval savedEntity = leaveApprovalRepository.save(leaveApproval);

            if (leaveApproval.getStatus() == 1) {
                List<LeaveApproval> approvals = leaveApprovalRepository.findByUserLeave_Id(leaveApproval.getUserLeave().getId());
                for (LeaveApproval approval : approvals) {
                    if (approval.getStatus() != 1) {
                        break;
                    }
                    Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveApproval.getUserLeave().getId());
                    if (userLeave.isPresent()) {
                        UserLeave updatedLeave = userLeave.get();
                        updatedLeave.setStatus(1);
                        userLeaveRepository.save(updatedLeave);
                    }
                }
            }
            return savedEntity;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found leave approval id");
    }
}
