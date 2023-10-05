package com.lms.Repository;

import com.lms.Models.UserLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLeaveRepository extends JpaRepository<UserLeave, Long> {

}
