package com.lms.Service;

import com.lms.DTO.LeaveRequestEmail;
import com.lms.DTO.UserLeaveDTO;

import javax.mail.MessagingException;

public interface EmailService {

	void send(LeaveRequestEmail leaveRequest) throws MessagingException;
}
