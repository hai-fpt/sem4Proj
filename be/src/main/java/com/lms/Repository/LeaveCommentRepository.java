package com.lms.repository;

import com.lms.models.FileStorage;
import com.lms.models.LeaveComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveCommentRepository extends JpaRepository<LeaveComment, Long> {

	List<LeaveComment> findAllByLeaveRequest_Id(Long id);

	void deleteAllByLeaveRequest_Id(Long id);
}
