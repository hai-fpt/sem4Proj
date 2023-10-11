package com.lms.service;

import com.lms.dto.LeaveCommentInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.LeaveComment;
import com.lms.models.UserLeave;
import com.lms.repository.LeaveCommentRepository;
import com.lms.repository.UserLeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveCommentServiceImpl implements LeaveCommentService {

	@Autowired
	LeaveCommentRepository commentRepository;

	@Autowired
	UserLeaveRepository userLeaveRepository;

	@Override
	public LeaveComment addComment(LeaveCommentInfo leaveComment) throws NotFoundByIdException {
		Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveComment.getLeaveRequestId());
		if (userLeave.isEmpty()) {
			throw new NotFoundByIdException("Can not find leave request for id: " + leaveComment.getLeaveRequestId());
		}
		return commentRepository.save(new LeaveComment(null, leaveComment.getComment(), leaveComment.getAuthor(),
				userLeave.get(), leaveComment.getUpdatedBy()));
	}

	@Override
	public LeaveComment editComment(LeaveCommentInfo leaveComment) throws NotFoundByIdException {
		Optional<UserLeave> userLeave = userLeaveRepository.findById(leaveComment.getLeaveRequestId());
		if (userLeave.isEmpty()) {
			throw new NotFoundByIdException("Can not find leave request for id: " + leaveComment.getLeaveRequestId());
		}
		return commentRepository.save(new LeaveComment(leaveComment.getId(), leaveComment.getComment(), leaveComment.getAuthor(),
				userLeave.get(), leaveComment.getUpdatedBy()));
	}

	@Override
	public List<LeaveComment> getAllCommentByLeaveId(Long leaveId) {
		return commentRepository.findAllByLeaveRequest_Id(leaveId);
	}

	@Override
	public void deleteCommentById(Long id) {
		commentRepository.deleteById(id);
	}

	@Override
	public void deleteAllCommentByLeaveId(Long leaveId) {
		commentRepository.deleteAllByLeaveRequest_Id(leaveId);
	}
}
