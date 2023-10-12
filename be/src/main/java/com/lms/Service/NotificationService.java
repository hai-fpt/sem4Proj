package com.lms.service;

import com.lms.dto.NotificationInfo;
import com.lms.exception.NotFoundByIdException;

import java.util.List;

public interface NotificationService {

	List<NotificationInfo> getNotificationByReceiverId(Long userId) throws NotFoundByIdException;

	List<NotificationInfo> pushNotification(List<NotificationInfo> notificationInfos) throws NotFoundByIdException;

	NotificationInfo setWatchedById(Long id) throws NotFoundByIdException;

}
