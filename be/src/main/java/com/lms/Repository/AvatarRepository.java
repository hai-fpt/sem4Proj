package com.lms.repository;

import com.lms.models.Avatar;
import com.lms.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findAllByUserId(Long id);

    Optional<Avatar> findByUser(User user);
}
