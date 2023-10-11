package com.lms.service;

import com.lms.dto.ApprovalStatus;
import com.lms.dto.LeaveApproval;
import com.lms.dto.LeaveProcess;
import com.lms.dto.User;
import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.exception.InvalidReceiverException;
import com.lms.models.UserLeave;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import com.lms.utils.ProjectionMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveApprovalServiceImpl implements LeaveApprovalService{

    private final LeaveApprovalRepository leaveApprovalRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    public LeaveApprovalServiceImpl(LeaveApprovalRepository leaveApprovalRepository, UserLeaveRepository userLeaveRepository, UserRepository userRepository, EmailService emailService) {
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.userLeaveRepository = userLeaveRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public LeaveApprovalProjection getLeaveApprovalById(Long id) {
        Optional<com.lms.models.LeaveApproval> optionalLeaveApproval = leaveApprovalRepository.findById(id);
        if (optionalLeaveApproval.isEmpty()) {
            throw new NullPointerException("Leave approval does not exists");
        }
        LeaveApprovalProjection leaveApprovalProjection = ProjectionMapper.mapToLeaveApprovalProjection(optionalLeaveApproval.get());
        return leaveApprovalProjection;
    }

    @Override
    public LeaveApprovalProjection updateLeaveApprovalStatus(LeaveApproval leaveApprovalDTO) {
        Optional<com.lms.models.LeaveApproval> leaveApprovalSearch = leaveApprovalRepository.findById(leaveApprovalDTO.getId());
        com.lms.models.LeaveApproval leaveApproval = leaveApprovalSearch.get();
        leaveApproval.setStatus(leaveApprovalDTO.getStatus());
        leaveApproval.setUpdatedBy(leaveApprovalDTO.getRequestedByEmail());
        com.lms.models.LeaveApproval savedEntity = leaveApprovalRepository.save(leaveApproval);

        LeaveApprovalProjection projection = ProjectionMapper.mapToLeaveApprovalProjection(savedEntity);

        Optional<com.lms.models.User> userOptional = userRepository.findById(leaveApproval.getManagerId());
        if (userOptional.isEmpty()) {
            throw new NullPointerException("Manager does not exists");
        }
        com.lms.models.User user = userOptional.get();
        ModelMapper modelMapper = new ModelMapper();
        User userDTO = modelMapper.map(user, User.class);

        LeaveProcess leaveProcess = new LeaveProcess();
        leaveProcess.setStatus(leaveApproval.getStatus());
        leaveProcess.setProcessBy(userDTO);
        leaveProcess.setDearTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getName()));
        leaveProcess.setSendTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getEmail()));
        try {
            emailService.sendApproval(leaveProcess);
        } catch (MessagingException | InvalidReceiverException e) {
            throw new RuntimeException(e);
        }

        userLeaveUpdate(leaveApproval);

        return projection;
    }

    @Override
    public void userLeaveUpdate(com.lms.models.LeaveApproval leaveApproval) {
        Page<LeaveApprovalProjection> approvals = leaveApprovalRepository.findByUserLeave_Id(leaveApproval.getUserLeave().getId(), PageRequest.of(0, 10));
        List<Long> managerIds = approvals.stream().map(approval -> approval.getManagerId()).collect(Collectors.toList());
        List<com.lms.models.User> managers = userRepository.findAllById(managerIds);
        ModelMapper modelMapper = new ModelMapper();
        List<User> managerDTOs = managers.stream()
                .map(manager -> {
                    User managerDTO = modelMapper.map(manager, User.class);
                    return managerDTO;
                })
                .collect(Collectors.toList());
        if (leaveApproval.getStatus().equals(ApprovalStatus.APPROVED)) {
            for (LeaveApprovalProjection approval : approvals) {
                if (approval.getStatus() != ApprovalStatus.APPROVED) {
                    break;
                }
                Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveApproval.getUserLeave().getId());
                if (userLeave.isPresent()) {
                    UserLeave updatedLeave = userLeave.get();
                    updatedLeave.setStatus(ApprovalStatus.APPROVED);
                    userLeaveRepository.save(updatedLeave);
                    //TODO: Overall approve
                    LeaveProcess leaveProcess = new LeaveProcess();
                    leaveProcess.setStatus(leaveApproval.getStatus());
                    leaveProcess.setProcessBys(managerDTOs);
                    leaveProcess.setDearTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getName()));
                    leaveProcess.setSendTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getEmail()));
                    try {
                        emailService.sendApproval(leaveProcess);
                    } catch (MessagingException | InvalidReceiverException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new NullPointerException("User leave request does not exists");
                }
            }
        } else if (leaveApproval.getStatus().equals(ApprovalStatus.REJECTED)) {
            Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveApproval.getUserLeave().getId());
            if (userLeave.isPresent()) {
                UserLeave updatedLeave = userLeave.get();
                updatedLeave.setStatus(ApprovalStatus.REJECTED);
                userLeaveRepository.save(updatedLeave);
                //TODO: Overall reject
                LeaveProcess leaveProcess = new LeaveProcess();
                leaveProcess.setStatus(leaveApproval.getStatus());
                leaveProcess.setProcessBys(managerDTOs);
                leaveProcess.setDearTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getName()));
                leaveProcess.setSendTos(Collections.singletonList(leaveApproval.getUserLeave().getUser().getEmail()));
                try {
                    emailService.sendApproval(leaveProcess);
                } catch (MessagingException | InvalidReceiverException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public Page<LeaveApprovalProjection> getLeaveApproveByManagerId(Long id, Pageable pageable) {
        return leaveApprovalRepository.findAllByManagerId(id, pageable);
    }

    //TODO: click on date and show what requests there are

}
