package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.projection.LeaveApprovalProjection;
import com.lms.exception.InvalidReceiverException;
import com.lms.exception.NotFoundByIdException;
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
import java.util.ArrayList;
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
    private final ConfigurationService configurationService;

    private final NotificationService notificationService;


    public LeaveApprovalServiceImpl(LeaveApprovalRepository leaveApprovalRepository,
                                    UserLeaveRepository userLeaveRepository, UserRepository userRepository,
                                    EmailService emailService, ConfigurationService configurationService, NotificationServiceImpl notificationServiceImpl) {
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.userLeaveRepository = userLeaveRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.configurationService = configurationService;
        this.notificationService = notificationServiceImpl;
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
    public Optional<com.lms.models.LeaveApproval> getLeaveApprovalByManagerId(Long id, Long managerId) {
        return leaveApprovalRepository.findByIdAndManagerId(id, managerId);
    }

    @Override
    public LeaveApproval updateLeaveApprovalStatus(LeaveApproval leaveApprovalDTO) throws NotFoundByIdException {
        Optional<com.lms.models.LeaveApproval> leaveApprovalOptional = leaveApprovalRepository.findByIdAndManagerId(leaveApprovalDTO.getId(), leaveApprovalDTO.getManagerId());
        com.lms.models.LeaveApproval leaveApproval = leaveApprovalOptional.get();
        leaveApproval.setStatus(leaveApprovalDTO.getStatus());
        leaveApproval.setUpdatedBy(leaveApprovalDTO.getUpdatedBy());
        com.lms.models.LeaveApproval savedEntity = leaveApprovalRepository.save(leaveApproval);
        com.lms.models.User user = leaveApproval.getManagerId();

        User userDTO = new User();
        userDTO.setName(user.getName());
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

        //push notification
        List<NotificationInfo> notiInfos = new ArrayList<>();
        NotificationInfo notificationInfo = new NotificationInfo();
        com.lms.models.User manager = leaveApproval.getManagerId();
        UserLeave userLeave = leaveApproval.getUserLeave();
        com.lms.models.User employee = userLeave.getUser();

        notificationInfo.setSenderId(manager.getId());
        notificationInfo.setSenderName(manager.getName());
        notificationInfo.setSenderEmail(manager.getEmail());
        notificationInfo.setReceiverId(employee.getId());
        notificationInfo.setReceiverName(employee.getName());
        notificationInfo.setReceiverEmail(employee.getEmail());
        notificationInfo.setStatus(leaveApproval.getStatus());
        notificationInfo.setLeaveFrom(userLeave.getFromDate());
        notificationInfo.setLeaveTo(userLeave.getToDate());
        notificationService.pushNotification(notiInfos);

//        update final status to user_leave(approved/rejected)
        userLeaveUpdate(leaveApproval);

        return leaveApprovalDTO;
    }

    @Override
    public void userLeaveUpdate(com.lms.models.LeaveApproval leaveApproval) throws NotFoundByIdException {
        Page<com.lms.models.LeaveApproval> approvals = leaveApprovalRepository.findByUserLeave_Id(leaveApproval.getUserLeave().getId(), PageRequest.of(0, 10));
        List<Long> managerIds =
                approvals.stream().map(approval -> approval.getManagerId().getId()).collect(Collectors.toList());
        List<com.lms.models.User> managers = userRepository.findAllById(managerIds);
        ModelMapper modelMapper = new ModelMapper();
        List<User> managerDTOs = managers.stream()
                .map(manager -> {
                    manager.setUserRoles(null);
                    User managerDTO = modelMapper.map(manager, User.class);
                    return managerDTO;
                })
                .collect(Collectors.toList());
        if (leaveApproval.getStatus().equals(ApprovalStatus.APPROVED)) {
            for (com.lms.models.LeaveApproval approval : approvals) {
                if (approval.getStatus() != ApprovalStatus.APPROVED) {
                    break;
                }
                Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveApproval.getUserLeave().getId());
                if (userLeave.isPresent()) {
                    UserLeave updatedLeave = userLeave.get();
                    updatedLeave.setStatus(ApprovalStatus.APPROVED);
                    UserLeave savedUserLeave = userLeaveRepository.save(updatedLeave);

                    //push notification for overall approve
                    List<NotificationInfo> notiInfos = new ArrayList<>();
                    NotificationInfo notificationInfo = new NotificationInfo();
                    com.lms.models.User manager = leaveApproval.getManagerId();
                    com.lms.models.User employee = savedUserLeave.getUser();

                    notificationInfo.setSenderId(manager.getId());
                    notificationInfo.setSenderName(manager.getName());
                    notificationInfo.setSenderEmail(manager.getEmail());
                    notificationInfo.setReceiverId(employee.getId());
                    notificationInfo.setReceiverName(employee.getName());
                    notificationInfo.setReceiverEmail(employee.getEmail());
                    notificationInfo.setStatus(leaveApproval.getStatus());
                    notificationInfo.setLeaveFrom(savedUserLeave.getFromDate());
                    notificationInfo.setLeaveTo(savedUserLeave.getToDate());
                    notificationService.pushNotification(notiInfos);

                    //Email for overall approved
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
                UserLeave savedUserLeave = userLeaveRepository.save(updatedLeave);

                //push notification for overall reject
                List<NotificationInfo> notiInfos = new ArrayList<>();
                NotificationInfo notificationInfo = new NotificationInfo();
                com.lms.models.User manager = leaveApproval.getManagerId();
                com.lms.models.User employee = savedUserLeave.getUser();

                notificationInfo.setSenderId(manager.getId());
                notificationInfo.setSenderName(manager.getName());
                notificationInfo.setSenderEmail(manager.getEmail());
                notificationInfo.setReceiverId(employee.getId());
                notificationInfo.setReceiverName(employee.getName());
                notificationInfo.setReceiverEmail(employee.getEmail());
                notificationInfo.setStatus(leaveApproval.getStatus());
                notificationInfo.setLeaveFrom(savedUserLeave.getFromDate());
                notificationInfo.setLeaveTo(savedUserLeave.getToDate());
                notificationService.pushNotification(notiInfos);

                //Email for overall approved
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
