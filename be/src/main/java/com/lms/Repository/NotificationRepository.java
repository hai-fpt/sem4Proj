package com.lms.repository;

import com.lms.models.FileStorage;
import com.lms.models.Notification;
import com.lms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByReceiverId(Long id);
}
