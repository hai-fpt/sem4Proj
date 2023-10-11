package com.lms.repository;

import com.lms.models.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
	List<FileStorage> findAllByLeaveRequest_Id(Long id);

	Integer countAllByLeaveRequest_Id(Long id);
}
