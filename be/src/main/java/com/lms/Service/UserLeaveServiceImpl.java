package com.lms.service;

import com.lms.dto.*;
import com.lms.dto.User;
import com.lms.dto.UserLeave;
import com.lms.dto.projection.LeaveManagerProjection;
import com.lms.dto.projection.UserLeaveProjection;
import com.lms.dto.projection.UserProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.exception.UnauthorizedException;
import com.lms.models.LeaveApproval;
import com.lms.models.Role;
import com.lms.models.Team;
import com.lms.models.UserRole;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.LeaveRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import com.lms.utils.DateCalculation;
import com.lms.utils.ProjectionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserLeaveServiceImpl implements UserLeaveService {
    private final UserRepository userRepository;
    private final UserLeaveRepository userLeaveRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final EmailServiceImpl emailService;
    private final HolidayService holidayService;
    private final FilesStorageService filesStorageService;
    private final DateCalculation dateCalculation;
    private final NotificationService notificationService;
    private final ConfigurationService configurationService;

    public UserLeaveServiceImpl(UserRepository userRepository,
                                UserLeaveRepository userLeaveRepository,
                                LeaveRepository leaveRepository, LeaveApprovalRepository leaveApprovalRepository,
                                EmailServiceImpl emailService,
                                HolidayService holidayService,
                                FilesStorageServiceImpl filesStorageServiceImpl,
                                DateCalculation dateCalculation,
                                NotificationServiceImpl notificationServiceImpl, ConfigurationService configurationService) {
        this.userRepository = userRepository;
        this.userLeaveRepository = userLeaveRepository;
        this.leaveRepository = leaveRepository;
        this.leaveApprovalRepository = leaveApprovalRepository;
        this.emailService = emailService;
        this.holidayService = holidayService;
        this.filesStorageService = filesStorageServiceImpl;
        this.dateCalculation = dateCalculation;
        this.notificationService = notificationServiceImpl;
        this.configurationService = configurationService;
    }

    @Override
    public Optional<com.lms.models.UserLeave> getUserLeaveById(Long id) {
        return userLeaveRepository.findById(id);
    }

    @Override
    @Transactional
    public UserLeaveProjection createUserLeave(UserLeave userLeave) throws IOException, NotFoundByIdException {
        com.lms.models.User user = userRepository.findById(userLeave.getUser().getId()).get();
        com.lms.models.Leave leave = leaveRepository.findById(userLeave.getLeave().getId()).get();
        List<Team> team = userRepository.getTeamByUser(user.getId());
        List<com.lms.dto.Team> teamDTOs = new ArrayList<>();
        for (Team team1 : team) {
            com.lms.dto.Team teamDTO = new com.lms.dto.Team();
            teamDTO.setTeamName(team1.getTeamName());
            teamDTOs.add(teamDTO);
        }
        User userDTO = new User();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setTeams(teamDTOs);

        userLeave.setUser(userDTO);
        com.lms.models.UserLeave userLeaveEntity = new com.lms.models.UserLeave(
                user,
                leave,
                userLeave.getFromDate(),
                userLeave.getToDate(),
                userLeave.getStatus(),
                userLeave.getReason(),
                userLeave.getUpdatedBy(),
                userLeave.getInformTo()
        );

        com.lms.models.UserLeave savedUserLeave = userLeaveRepository.save(userLeaveEntity);
        UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(savedUserLeave);
        List<String> sendTo = new ArrayList<>();
        List<String> dearTo = new ArrayList<>();
        List<String> ccTo = new ArrayList<>();
        List<Long> teamLeads = userLeave.getTeamLeads();
        List<Long> informTos = userLeave.getInformTo();
        if (!teamLeads.isEmpty()) {
            List<LeaveApproval> leaveApprovals = new ArrayList<>();
            LocalDateTime currentDate = LocalDateTime.now();
            String requestedEmail = userLeave.getUpdatedBy();
            for (Long lead : teamLeads) {
                leaveApprovals.add(new LeaveApproval(userLeaveEntity, userRepository.findById(lead).get(), currentDate,
                        currentDate,
                        requestedEmail));
            }
            List<LeaveApproval> approvals = leaveApprovalRepository.saveAll(leaveApprovals);
            //Insert attachment
            if (userLeave.getAttachments() != null && userLeave.getAttachments().length > 0) {
                filesStorageService.saveToDatabase(
                        filesStorageService.saveToStorage(
                                userLeave.getAttachments(),
                                savedUserLeave.getId().toString(),
                                savedUserLeave.getUpdatedBy()), savedUserLeave);
            }
            //Insert Notification
            com.lms.models.User sender = savedUserLeave.getUser();
            notificationService.pushNotification(approvals.stream().map(approval -> {
                com.lms.models.User receiver = approval.getManagerId();
                NotificationInfo notificationInfo = new NotificationInfo();
                notificationInfo.setSenderId(sender.getId());
                notificationInfo.setSenderName(sender.getName());
                notificationInfo.setSenderEmail(sender.getEmail());
                notificationInfo.setReceiverId(receiver.getId());
                notificationInfo.setReceiverName(receiver.getName());
                notificationInfo.setReceiverEmail(receiver.getEmail());
                notificationInfo.setStatus(ApprovalStatus.PENDING);
                notificationInfo.setLeaveFrom(userLeaveEntity.getFromDate());
                notificationInfo.setLeaveTo(userLeaveEntity.getToDate());
                sendTo.add(approval.getManagerId().getEmail());
                dearTo.add(approval.getManagerId().getName());
                return notificationInfo;
            }).collect(Collectors.toList()));
        } else {
            throw new NullPointerException("At least 1 team lead must exists");
        }

        if (!informTos.isEmpty()) {
            List<com.lms.models.User> informToUsers = userRepository.findAllById(informTos);
            for (com.lms.models.User informTo : informToUsers) {
                ccTo.add(informTo.getEmail());
            }
        }

        //Email
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
        String link = configurationService.getHostAddress() + "leave-details/" + savedUserLeave.getId();


        LeaveRequest leaveRequestEmail = new LeaveRequest();
        leaveRequestEmail.setSubject("[" + userLeave.getUser().getDepartment() + "] Leave Request");
        leaveRequestEmail.setRequester(userDTO);
        leaveRequestEmail.setFromDate(userLeave.getFromDate().format(formatter));
        leaveRequestEmail.setToDate(userLeave.getToDate().format(formatter));
        leaveRequestEmail.setReason(userLeave.getReason());
        leaveRequestEmail.setDearTos(dearTo);
        leaveRequestEmail.setSendTos(sendTo);
        leaveRequestEmail.setCcTos(ccTo);
        leaveRequestEmail.setLink(link);
        emailService.sendRequestAsync(leaveRequestEmail);
        return projection;
    }

    @Override
    @Transactional
    public UserLeaveProjection cancelLeave(UserLeaveCancel userLeaveCancel) throws NotFoundByIdException {
        com.lms.models.UserLeave userLeave = userLeaveRepository.findById(userLeaveCancel.getId()).get();
        userLeave.setUpdatedBy(userLeaveCancel.getUpdatedBy());
        userLeave.setUpdatedDate(LocalDateTime.now());
        userLeave.setStatus(ApprovalStatus.CANCELLED);

        com.lms.models.User user = userRepository.findById(userLeave.getUser().getId()).get();
        User userDTO = new User();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        LeaveProcess leaveProcess = new LeaveProcess();
        leaveProcess.setStatus(userLeave.getStatus());
        leaveProcess.setProcessBy(userDTO);
        com.lms.models.UserLeave saved = userLeaveRepository.save(userLeave);
        //Push notification
        List<LeaveApproval> leaveApprovals = userLeave.getLeaveApprovals();
        List<Long> sendTo = new ArrayList<>();//Used to send email
        List<String> ccTo = new ArrayList<>();
        notificationService.pushNotification(leaveApprovals
                .stream()
                .map(approval ->
                {
                    com.lms.models.User sender = saved.getUser();
                    com.lms.models.User receiver = approval.getManagerId();
                    NotificationInfo notificationInfo = new NotificationInfo();
                    notificationInfo.setSenderId(sender.getId());
                    notificationInfo.setSenderName(sender.getName());
                    notificationInfo.setSenderEmail(sender.getEmail());
                    notificationInfo.setReceiverId(receiver.getId());
                    notificationInfo.setReceiverName(receiver.getName());
                    notificationInfo.setReceiverEmail(receiver.getEmail());
                    notificationInfo.setStatus(saved.getStatus());
                    notificationInfo.setLeaveFrom(saved.getFromDate());
                    notificationInfo.setLeaveTo(saved.getToDate());
                    sendTo.add(approval.getManagerId().getId());
                    return notificationInfo;
                })
                .collect(Collectors.toList()));
        if (!userLeave.getInformTo().isEmpty()) {
            List<com.lms.models.User> informToUsers = userRepository.findAllById(userLeave.getInformTo());
            for (com.lms.models.User informTo : informToUsers) {
                ccTo.add(informTo.getEmail());
            }
        }

        //Send email
        leaveProcess.setDearTos(userRepository.findAllById(sendTo).stream().map(com.lms.models.User::getName).collect(Collectors.toList()));
        leaveProcess.setSendTos(userRepository.findAllById(sendTo).stream().map(com.lms.models.User::getEmail).collect(Collectors.toList()));
        leaveProcess.setCcTos(ccTo);
        leaveProcess.setSubject("[" + user.getDepartment() + "] Cancel Leave Request");
        emailService.sendApprovalAsync(leaveProcess);
        return ProjectionMapper.mapToUserLeaveProjection(saved);
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
        List<com.lms.models.UserLeave> userLeaves = userLeaveRepository.findUserLeaveByUser(user);
        Page<com.lms.models.Holiday> holidays = holidayService.getAllHolidays(Pageable.unpaged());
        List<UserLeaveProjection> userLeaveProjections = new ArrayList<>();
        for (com.lms.models.UserLeave userLeave : userLeaves) {
            userLeave.setDaysOff(dateCalculation.calculateSingleUserLeaveDaysOff(userLeave, holidays));
            UserLeaveProjection projection = ProjectionMapper.mapToUserLeaveProjection(userLeave);
            userLeaveProjections.add(projection);
        }
        return new PageImpl<>(userLeaveProjections, pageable, userLeaveProjections.size());
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
    public DashboardCalendar getUserLeaveRequestList(Long id, DateRange dateRange) {
        List<UserLeaveProjection> userLeaveProjections = userLeaveRepository.findUserLeaveBetweenDatesByUserId(id, dateRange.getStartDate(), dateRange.getEndDate());
        List<LocalDate> dateList = new ArrayList<>();
        for (UserLeaveProjection userLeave : userLeaveProjections) {
            LocalDate fromDate = userLeave.getFromDate().toLocalDate();
            LocalDate toDate = userLeave.getToDate().toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

            for (int i = 0; i <= daysBetween; i++) {
                LocalDate date = fromDate.plusDays(1);
                dateList.add(date);
            }
        }
        Collections.sort(dateList);
        DashboardCalendar dashboardCalendar = new DashboardCalendar(dateList);
        return dashboardCalendar;
    }

    @Override
    public List<UserLeaveProjection> getUserLeaveByIdAndStatusAndType(Long id, ApprovalStatus status, boolean affects) {
        return userLeaveRepository.findUserLeaveByUserIdAndStatusAndAndLeave_AffectsDaysOff(id, status, affects);
    }

    @Override
    public List<SelfLeave> getSelfLeave(Long id) {
        com.lms.models.User user = userRepository.findById(id).get();
        List<com.lms.models.UserLeave> userLeaves = userLeaveRepository.findUserLeaveByUser(user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<SelfLeave> selfLeaves = userLeaves.stream()
                .map(userLeave -> {
                    SelfLeave selfLeave = new SelfLeave();
                    selfLeave.setId(userLeave.getId());
                    selfLeave.setTitle(userLeave.getFromDate().format(formatter) + " - " + userLeave.getToDate().format(formatter));
                    selfLeave.setStart(userLeave.getFromDate());
                    selfLeave.setEnd(userLeave.getToDate());
                    selfLeave.setStatus(userLeave.getStatus());
                    return selfLeave;
                }).collect(Collectors.toList());
        return selfLeaves;
    }

    @Override
    public List<LeaveManagerProjection> getUserLeaveManagers(Long id) {
        return userLeaveRepository.findManagerById(id);
    }
}
