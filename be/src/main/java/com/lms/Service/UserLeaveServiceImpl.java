package com.lms.service;

import com.lms.dto.*;
import com.lms.exception.UnauthorizedException;
import com.lms.helper.ExcelHelper;
import com.lms.models.*;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;

@Service
public class UserLeaveServiceImpl implements UserLeaveService {
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final EmailServiceImpl emailService;


    public UserLeaveServiceImpl(UserRepository userRepository, UserLeaveRepository userLeaveRepository, LeaveApprovalRepository leaveApprovalRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.userLeaveRepository = userLeaveRepository;
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.emailService = emailService;
    }

    @Override
    public Optional<UserLeave> getUserLeaveById(Long id) {
        return userLeaveRepository.findById(id);
    }

    @Override
    public UserLeave createUserLeave(UserLeaveDTO userLeaveDTO) {
        //Assume got a list of team lead
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<UserLeave, UserLeaveDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTeamLeads());
            }
        });

        User user = userRepository.findById(userLeaveDTO.getUser().getId()).get();
        userLeaveDTO.setUser(user);
        UserLeave userLeaveEntity = modelMapper.map(userLeaveDTO, UserLeave.class);
        userLeaveEntity.setUpdatedBy(userLeaveDTO.getRequestedByEmail());

        UserLeave savedUserLeave = userLeaveRepository.save(userLeaveEntity);

        List<Long> teamLeads = userLeaveDTO.getTeamLeads();
        if (!teamLeads.isEmpty()) {
            List<LeaveApproval> leaveApprovals = new ArrayList<>();
            Date currentDate = new Date();
            String userName = userLeaveDTO.getRequestedByEmail();
            for (Long lead : teamLeads) {
                LeaveApproval leaveApproval = new LeaveApproval(
                        userLeaveEntity,
                        lead,
                        currentDate,
                        currentDate,
                        userName
                );
                leaveApprovals.add(leaveApproval);
            }
            leaveApprovalRepository.saveAll(leaveApprovals);
        } else {
            throw new NullPointerException("At least 1 team lead must exists");
        }

        //Email
        //TODO: FIX THIS AND FINISH PROPERLY
        //TODO: Add email send when fully approved and when cancelled
        ModelMapper modelMapper1 = new ModelMapper();
        UserDTO userDTO = modelMapper1.map(user, UserDTO.class);

        LeaveRequest leaveRequestEmail = new LeaveRequest();
        leaveRequestEmail.setRequester(userDTO);
        leaveRequestEmail.setFromDate(String.valueOf(userLeaveDTO.getFromDate()));
        leaveRequestEmail.setToDate(String.valueOf(userLeaveDTO.getToDate()));
        leaveRequestEmail.setReason(userLeaveDTO.getReason());
        List<String> sendTo = new ArrayList<>();
        sendTo.add("hainvh@mz.co.kr");
        leaveRequestEmail.setSendTos(sendTo);
        try {
            emailService.sendRequest(leaveRequestEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedUserLeave;
    }

    @Override
    public UserLeave cancelLeave(UserLeaveCancelDTO userLeaveCancelDTO) {
        UserLeave userLeave = userLeaveRepository.findById(userLeaveCancelDTO.getId()).get();
        userLeave.setUpdatedBy(userLeaveCancelDTO.getRequestedByEmail());
        userLeave.setUpdatedDate(new Date());
        userLeave.setStatus(4);

        User user = userRepository.findById(userLeave.getId()).get();
        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        LeaveRequestEmail leaveRequestEmail = new LeaveRequestEmail();
        leaveRequestEmail.setRequester(userDTO);
        //Add the rest of the info, send cancel email
        return userLeaveRepository.save(userLeave);
    }

    @Override
    public Page<UserLeave> getUserLeaveByRole(User user, Pageable pageable) {
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles.isEmpty()) {
            throw new NullPointerException("This user does not have any roles");
        }
        userRoles.sort(Comparator.comparing(userRole -> userRole.getRole().getName()));
        for (UserRole userRole : userRoles) {
            Role.RoleEnum role = userRole.getRole().getName();
            if (Objects.equals(role, Role.RoleEnum.ADMIN)) {
                return userLeaveRepository.findAll(pageable);
            } else if (Objects.equals(role, Role.RoleEnum.MANAGER)) {
                return userLeaveRepository.getUserLeaveByTeam(user.getId(), pageable);
            } else {
                throw new UnauthorizedException("User is not authorized");
            }
        }
        throw new UnauthorizedException("User is not authorized");
    }

    @Override
    public Page<UserLeave> getUserLeaveByUser(User user, Pageable pageable) {
        return userLeaveRepository.findUserLeaveByUser(user, pageable);
    }

    @Override
    public Page<UserLeave> getUserLeaveByFromDate(DateRangeDTO dateRangeDTO, Pageable pageable) {
        return userLeaveRepository.findUserLeaveByFromDate(dateRangeDTO.getStartDate(), pageable);
    }

    @Override
    public Page<UserLeave> getUserLeaveByDateRange(DateRangeDTO dateRangeDTO, Pageable pageable) {
        Date date = dateRangeDTO.getSingleDate();
        return userLeaveRepository.findUserLeaveByDate(date, pageable);
    }

    @Override
    public Page<UserLeave> getUserLeaveByMonth(DateRangeDTO dateRangeDTO, Pageable pageable) {
        Date date = dateRangeDTO.getSingleDate();
        return userLeaveRepository.findUserLeaveByMonth(date, pageable);
    }

    @Override
    public List<UserLeave> getUserLeaveByIdAndStatus(Long id, int status) {
        return userLeaveRepository.findUserLeaveByUserIdAndStatus(id, status);
    }

    public ByteArrayInputStream exportExcelUserLeave() {
        List<UserLeave> leaves = userLeaveRepository.findAll();
        ByteArrayInputStream in = ExcelHelper.exportToExcel(leaves);
        return in;
    }
}
