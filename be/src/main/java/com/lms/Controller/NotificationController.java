package com.lms.controller;

import com.lms.dto.NotificationInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	@Autowired
	NotificationService notificationService;

	@PutMapping()
	public ResponseEntity<NotificationInfo> setWatchedById(@RequestBody Long notificationId) throws NotFoundByIdException {
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.setWatchedById(notificationId));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<List<NotificationInfo>> getAllByUserId(@PathVariable("userId") Long userId) throws NotFoundByIdException {
		return ResponseEntity.status(HttpStatus.OK).body(notificationService.getNotificationByReceiverId(userId));
	}

}
