package com.lms.service;

import com.lms.dto.NotificationInfo;
import com.lms.models.Notification;
import com.lms.repository.LeaveApprovalRepository;
import com.lms.repository.NotificationRepository;
import com.lms.repository.UserLeaveRepository;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserLeaveRepository userLeaveRepository;

	@Autowired
	LeaveApprovalRepository leaveApprovalRepository;

	@Override
	public List<NotificationInfo> getNotificationByReceiverId(Long userId) {
		List<Notification> notifications = notificationRepository.findAllByReceiverId(userId);
		return notifications.stream().map(Notification::toNotificationInfo).collect(Collectors.toList());
	}

	@Override
	public List<NotificationInfo> pushNotification(List<NotificationInfo> notificationInfos) {
		List<Notification> notifications = notificationRepository.saveAllAndFlush(
				notificationInfos.stream().map(NotificationInfo::toNotification).collect(Collectors.toList()));
		return notifications.stream().map(Notification::toNotificationInfo).collect(Collectors.toList());
	}

	@Override
	public NotificationInfo setWatchedById(Long id) {
		Notification notification = notificationRepository.findById(id).get();
		notification.setWatched(true);
		return notificationRepository.save(notification).toNotificationInfo();
	}

}
