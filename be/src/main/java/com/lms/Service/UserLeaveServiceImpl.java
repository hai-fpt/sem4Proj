package com.example.lms.Service;

import com.example.lms.DTO.UserLeaveDTO;
import com.example.lms.Models.LeaveApproval;
import com.example.lms.Models.UserLeave;
import com.example.lms.Repository.LeaveApprovalRepository;
import com.example.lms.Repository.UserLeaveRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserLeaveServiceImpl implements UserLeaveService {
    private final UserLeaveRepository userLeaveRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;

    public UserLeaveServiceImpl(UserLeaveRepository userLeaveRepository, LeaveApprovalRepository leaveApprovalRepository) {
        this.userLeaveRepository = userLeaveRepository;
        this.leaveApprovalRepository = leaveApprovalRepository;
    }

    @Override
    public UserLeave createUserLeave(UserLeaveDTO userLeaveDTO) {
        //Assume got a list of team lead with TeamServiceImpl.getTeamLeadByUsers()
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<UserLeave, UserLeaveDTO>() {
            @Override
            protected void configure() {
                skip(destination.getTeamLeads());
            }
        });
        List<String> teamLeads = userLeaveDTO.getTeamLeads();
        UserLeave userLeaveEntity = modelMapper.map(userLeaveDTO, UserLeave.class);
        UserLeave savedUserLeave = userLeaveRepository.save(userLeaveEntity);
        if (!teamLeads.isEmpty()) {
            for (String lead : teamLeads) {
                LeaveApproval leaveApproval = new LeaveApproval(
                        userLeaveEntity,
                        lead,
                        new Date(),
                        userLeaveEntity.getUser().getName()
                );
                leaveApprovalRepository.save(leaveApproval);
            }
        }
        return savedUserLeave;
    }
}
