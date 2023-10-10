package com.lms.service;

import com.lms.dto.LeaveProcess;
import com.lms.dto.LeaveRequest;
import com.lms.exception.InvalidReceiverException;

import javax.mail.MessagingException;

public interface EmailService {

	void sendRequest(LeaveRequest leaveRequestEmail) throws InvalidReceiverException, MessagingException;

	void sendApproval(LeaveProcess process) throws InvalidReceiverException, MessagingException;

}
