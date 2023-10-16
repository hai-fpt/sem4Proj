package com.lms.controller;

import com.lms.dto.LeaveCommentInfo;
import com.lms.dto.projection.LeaveCommentProjection;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.LeaveComment;
import com.lms.service.LeaveCommentService;
import com.lms.service.UserLeaveService;
import com.lms.utils.ControllerUtils;
import com.lms.utils.ProjectionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/comment")
public class LeaveCommentController {

	@Autowired
	UserLeaveService userLeaveService;

	@Autowired
	ControllerUtils controllerUtils;

	@Autowired
	LeaveCommentService commentService;


	@GetMapping()
	public ResponseEntity<List<LeaveCommentProjection>> getCommentById(@RequestParam Long id) throws NotFoundByIdException {
		List<LeaveComment> comments = commentService.getAllCommentByLeaveId(id);
		List<LeaveCommentProjection> projections = new ArrayList<>();
		for (LeaveComment comment : comments) {
			LeaveCommentProjection projection = ProjectionMapper.mapToLeaveCommentProjection(comment);
			projections.add(projection);
		}
		projections.sort(Comparator.comparing(LeaveCommentProjection::getCreatedDate));
		return ResponseEntity.ok(projections);
	}

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
		return ResponseEntity.status(HttpStatus.OK).body(COMMENT_DELETE_SUCCESS);
	}

	@DeleteMapping("/deleteAllByLeaveId")
	public ResponseEntity<String> deleteAllByLeaveId(@RequestBody Long leaveId) {
		commentService.deleteAllCommentByLeaveId(leaveId);
		return ResponseEntity.status(HttpStatus.OK).body(COMMENT_DELETE_SUCCESS);
	}

	private void validateInputData(LeaveCommentInfo comment) {
		if (comment == null || StringUtils.isEmpty(comment.getComment()) || StringUtils.isEmpty(comment.getAuthor()))
			throw new NullPointerException(INVALID_PAYLOAD);

		if (!controllerUtils.validateRequestedUser(comment.getAuthor())) {
			throw new NullPointerException(EMAIL_NOT_EXISTS);
		}
		if (StringUtils.isNotEmpty(comment.getUpdatedBy()) &&!controllerUtils.validateRequestedUser(comment.getUpdatedBy())) {
			throw new NullPointerException(EMAIL_NOT_EXISTS);
		}
	}

}
