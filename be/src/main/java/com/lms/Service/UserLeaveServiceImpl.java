package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.User;
import com.lms.dto.UserLeave;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.exception.UnauthorizedException;
import com.lms.models.*;
import com.lms.models.LeaveApproval;
import com.lms.models.Role;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import com.lms.utils.DateCalculation;
import com.lms.utils.ProjectionMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class UserLeaveServiceImpl implements UserLeaveService {
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final EmailServiceImpl emailService;
    private final HolidayService holidayService;
    private final FilesStorageService filesStorageService;
    private final DateCalculation dateCalculation;


    public UserLeaveServiceImpl(UserRepository userRepository, UserLeaveRepository userLeaveRepository,
                                LeaveApprovalRepository leaveApprovalRepository, EmailServiceImpl emailService,
                                HolidayService holidayService, FilesStorageServiceImpl filesStorageServiceImpl, DateCalculation dateCalculation) {
        this.userRepository = userRepository;
        this.userLeaveRepository = userLeaveRepository;
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.emailService = emailService;
        this.holidayService = holidayService;
        this.filesStorageService = filesStorageServiceImpl;
        this.dateCalculation = dateCalculation;
    }

    @Override
    public Optional<com.lms.models.UserLeave> getUserLeaveById(Long id) {
        return userLeaveRepository.findById(id);
    }

    @Override
    public UserLeaveProjection createUserLeave(UserLeave userLeave) throws IOException {
        //Assume got a list of team lead
        ModelMapper modelMapper = new ModelMapper();
        ModelMapper modelMapper1 = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<com.lms.models.UserLeave, UserLeave>() {
            @Override
            protected void configure() {
                skip(destination.getTeamLeads());
            }
        });

        com.lms.models.User user = userRepository.findById(userLeave.getUser().getId()).get();
        User userDTO = modelMapper1.map(user, User.class);
        userLeave.setUser(userDTO);
        com.lms.models.UserLeave userLeaveEntity = modelMapper.map(userLeave, com.lms.models.UserLeave.class);
        userLeaveEntity.setUpdatedBy(userLeave.getRequestedByEmail());

        com.lms.models.UserLeave savedUserLeave = userLeaveRepository.save(userLeaveEntity);
        UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(savedUserLeave);

        List<Long> teamLeads = userLeave.getTeamLeads();
        if (!teamLeads.isEmpty()) {
            List<LeaveApproval> leaveApprovals = new ArrayList<>();
            LocalDateTime currentDate = LocalDateTime.now();
            String requestedEmail = userLeave.getRequestedByEmail();
            for (Long lead : teamLeads) {
                LeaveApproval leaveApproval = new LeaveApproval(
                        userLeaveEntity,
                        lead,
                        currentDate,
                        currentDate,
                        requestedEmail
                );
                leaveApprovals.add(leaveApproval);
            }
            leaveApprovalRepository.saveAll(leaveApprovals);
            //Insert attachment
            if (userLeave.getAttachments() != null && userLeave.getAttachments().length > 0) {
                filesStorageService.saveToDatabase(
                        filesStorageService.saveToStorage(
                                userLeave.getAttachments(),
                                savedUserLeave.getId().toString(),
                                savedUserLeave.getUpdatedBy()), savedUserLeave);
            }
        } else {
            throw new NullPointerException("At least 1 team lead must exists");
        }

        //Email
        //TODO: FIX THIS AND FINISH PROPERLY
        //TODO: Add email send when fully approved and when cancelled
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");

        LeaveRequest leaveRequestEmail = new LeaveRequest();
        leaveRequestEmail.setRequester(userDTO);
        leaveRequestEmail.setFromDate(userLeave.getFromDate().format(formatter));
        leaveRequestEmail.setToDate(userLeave.getToDate().format(formatter));
        leaveRequestEmail.setReason(userLeave.getReason());
        List<String> sendTo = new ArrayList<>();
        sendTo.add("hainvh@mz.co.kr");
        leaveRequestEmail.setSendTos(sendTo);
        try {
            emailService.sendRequest(leaveRequestEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projection;
    }

    @Override
    public UserLeaveProjection cancelLeave(UserLeaveCancel userLeaveCancel) {
        com.lms.models.UserLeave userLeave = userLeaveRepository.findById(userLeaveCancel.getId()).get();
        userLeave.setUpdatedBy(userLeaveCancel.getRequestedByEmail());
        userLeave.setUpdatedDate(LocalDateTime.now());
        userLeave.setStatus(ApprovalStatus.CANCELLED);

        com.lms.models.User user = userRepository.findById(userLeave.getUser().getId()).get();
        ModelMapper modelMapper = new ModelMapper();
        User userDTO = modelMapper.map(user, User.class);

        LeaveProcess leaveProcess = new LeaveProcess();
        leaveProcess.setStatus(userLeave.getStatus());
        leaveProcess.setProcessBy(userDTO);
        List<String> sendTo = new ArrayList<>();
        sendTo.add("hainvh@mz.co.kr");
        leaveProcess.setSendTos(sendTo);
        try {
            emailService.sendApproval(leaveProcess);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        com.lms.models.UserLeave saved = userLeaveRepository.save(userLeave);
        UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(saved);
        return projection;
    }

    @Override
    public Page<UserLeaveProjection> getUserLeaveByRole(com.lms.models.User user, Pageable pageable) {
        Page<com.lms.models.Holiday> holidays = holidayService.getAllHolidays(Pageable.unpaged());
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles.isEmpty()) {
            throw new NullPointerException("This user does not have any roles");
        }
        userRoles.sort(Comparator.comparing(userRole -> userRole.getRole().getName()));
        for (UserRole userRole : userRoles) {
            Role.RoleEnum role = userRole.getRole().getName();
            if (Objects.equals(role, Role.RoleEnum.ADMIN)) {
                List<com.lms.models.UserLeave> userLeaves = userLeaveRepository.findAll();
                List<UserLeaveProjection> userLeaveProjections = new ArrayList<>();
                for (com.lms.models.UserLeave userLeave : userLeaves) {
                    userLeave.setDaysOff(dateCalculation.calculateSingleUserLeaveDaysOff(userLeave, holidays));
                    UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(userLeave);
                    userLeaveProjections.add(projection);
                }
                return new PageImpl<>(userLeaveProjections, pageable, userLeaveProjections.size());
            } else if (Objects.equals(role, Role.RoleEnum.MANAGER)) {
                List<com.lms.models.UserLeave> userLeaves = userLeaveRepository.getUserLeaveByTeam(user.getId());
                List<UserLeaveProjection> userLeaveProjections = new ArrayList<>();
                for (com.lms.models.UserLeave userLeave : userLeaves) {
                    userLeave.setDaysOff(dateCalculation.calculateSingleUserLeaveDaysOff(userLeave, holidays));
                    UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(userLeave);
                    userLeaveProjections.add(projection);
                }
                return new PageImpl<>(userLeaveProjections, pageable, userLeaveProjections.size());
            } else {
                throw new UnauthorizedException("User is not authorized");
            }
        }
        throw new UnauthorizedException("User is not authorized");
    }

    @Override
    public Page<UserLeaveProjection> getUserLeaveByUser(com.lms.models.User user, Pageable pageable) {
        return userLeaveRepository.findUserLeaveByUser(user, pageable);
    }

    @Override
    public Page<UserLeaveProjection> getUserLeaveByFromDate(DateRange dateRange, Pageable pageable) {
        return userLeaveRepository.findUserLeaveByFromDate(dateRange.getStartDate(), pageable);
    }

    @Override
    public Page<UserLeaveProjection> getUserLeaveByDateRange(DateRange dateRange, Pageable pageable) {
        LocalDateTime date = dateRange.getSingleDate();
        return userLeaveRepository.findUserLeaveByDate(date, pageable);
    }

    @Override
    public Page<UserLeaveProjection> getUserLeaveByMonth(DateRange dateRange, Pageable pageable) {
        return userLeaveRepository.findUserLeavesBetweenDates(dateRange.getStartDate(), dateRange.getEndDate(), pageable);
    }

    @Override
    public List<UserLeaveProjection> getUserLeaveByIdAndStatusAndType(Long id, ApprovalStatus status, boolean affects) {
        return userLeaveRepository.findUserLeaveByUserIdAndStatusAndAndLeave_AffectsDaysOff(id, status, affects);
    }

}
