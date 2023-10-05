package com.lms.Service;

import com.lms.DTO.UserLeaveDTO;
import com.lms.Models.UserLeave;

public interface UserLeaveService {
    UserLeave createUserLeave(UserLeaveDTO userLeaveDTO);

}
