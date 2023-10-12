package com.lms.repository;

import com.lms.models.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
	List<FileStorage> findAllByLeaveRequestId(Long id);

	Integer countAllByLeaveRequestId(Long id);
}
