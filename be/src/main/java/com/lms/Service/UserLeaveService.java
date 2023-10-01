package com.example.lms.Service;

import com.example.lms.DTO.UserLeaveDTO;
import com.example.lms.Models.LeaveApproval;
import com.example.lms.Models.UserLeave;

import java.util.List;

public interface UserLeaveService {
    UserLeave createUserLeave(UserLeaveDTO userLeaveDTO);

}
