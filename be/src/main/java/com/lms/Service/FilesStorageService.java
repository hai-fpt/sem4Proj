package com.lms.service;

import com.lms.dto.FileInfo;
import com.lms.models.FileStorage;
import com.lms.models.UserLeave;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface FilesStorageService {
	void init();

	List<FileInfo> saveToStorage(MultipartFile[] file, String subdirectory, String updatedBy) throws IOException;

	List<FileStorage> saveToDatabase(List<FileInfo> files, UserLeave leaveRequest);

	Resource getStorageFile(Long id, String filename);

	List<FileInfo> getFilesByRequestId(Long id);

	boolean delete(Long id, String filename);

	void deleteAll();

	Stream<Path> loadAllById(Long id);
}
