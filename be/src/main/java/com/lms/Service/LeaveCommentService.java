package com.lms.service;

import com.lms.dto.LeaveCommentInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.LeaveComment;

import java.util.List;

public interface LeaveCommentService {

	LeaveComment addComment(LeaveCommentInfo leaveComment) throws NotFoundByIdException;

	LeaveComment editComment(LeaveCommentInfo leaveComment) throws NotFoundByIdException;

	List<LeaveComment> getAllCommentByLeaveId(Long leaveId);

	void deleteCommentById(Long id);

	void deleteAllCommentByLeaveId(Long leaveId);
}
