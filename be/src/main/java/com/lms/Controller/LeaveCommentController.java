package com.lms.controller;

import com.lms.dto.LeaveCommentInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.LeaveComment;
import com.lms.service.LeaveCommentService;
import com.lms.service.UserLeaveService;
import com.lms.utils.ControllerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class LeaveCommentController {

	@Autowired
	UserLeaveService userLeaveService;

	@Autowired
	ControllerUtils controllerUtils;

	@Autowired
	LeaveCommentService commentService;

	@PostMapping()
	public ResponseEntity<LeaveCommentInfo> addComment(@RequestBody LeaveCommentInfo comment) throws NotFoundByIdException {
		validateInputData(comment);
		LeaveComment leaveComment = commentService.addComment(comment);
		return ResponseEntity.status(HttpStatus.CREATED).body(leaveComment.toLeaveCommentInfo());
	}

	@PutMapping()
	public ResponseEntity<LeaveCommentInfo> editComment(@RequestBody LeaveCommentInfo comment) throws NotFoundByIdException {
		validateInputData(comment);
		LeaveComment leaveComment = commentService.editComment(comment);
		return ResponseEntity.status(HttpStatus.OK).body(leaveComment.toLeaveCommentInfo());
	}

	@DeleteMapping()
	public ResponseEntity<String> deleteComment(@RequestBody Long leaveId) {
		commentService.deleteCommentById(leaveId);
		return ResponseEntity.status(HttpStatus.OK).body("Comment has been deleted!");
	}

	@DeleteMapping("/deleteAllByLeaveId")
	public ResponseEntity<String> deleteAllByLeaveId(@RequestBody Long leaveId) {
		commentService.deleteAllCommentByLeaveId(leaveId);
		return ResponseEntity.status(HttpStatus.OK).body("Comment has been deleted!");
	}

	private void validateInputData(LeaveCommentInfo comment) {
		if (comment == null || StringUtils.isEmpty(comment.getComment()) || StringUtils.isEmpty(comment.getAuthor()))
			throw new NullPointerException("Invalid input params");

		if (!controllerUtils.validateRequestedUser(comment.getAuthor())) {
			throw new NullPointerException("Email for: " + comment.getAuthor() + " not found");
		}
		if (StringUtils.isNotEmpty(comment.getUpdatedBy()) &&!controllerUtils.validateRequestedUser(comment.getUpdatedBy())) {
			throw new NullPointerException("Email for: " + comment.getUpdatedBy() + " not found");
		}
	}

}
