package com.lms.controller;

import com.lms.dto.FileInfo;
import com.lms.models.UserLeave;
import com.lms.service.FilesStorageService;
import com.lms.service.UserLeaveService;
import com.lms.utils.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/file")
public class FileStorageController {

	@Autowired
	FilesStorageService storageService;

	@Autowired
	UserLeaveService userLeaveService;

	@Autowired
	ControllerUtils controllerUtils;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile[] files,
											 @RequestParam("requestId") Long requestId,
											 @RequestParam("updatedBy") String updatedBy) throws IOException {
		if (requestId == null || files == null || files.length == 0)
			throw new NullPointerException(INVALID_PAYLOAD);
		if (!controllerUtils.validateRequestedUser(updatedBy)) {
			throw new NullPointerException(EMAIL_NOT_EXISTS);
		}
		String message = "";
		Optional<UserLeave> leaveRequest = userLeaveService.getUserLeaveById(requestId);
		if (leaveRequest.isEmpty()) {
			throw new FileNotFoundException(LEAVE_REQUEST_NOT_EXISTS);
		}
		List<FileInfo> savedFiles = storageService.saveToStorage(files, Long.toString(requestId), updatedBy);
		storageService.saveToDatabase(savedFiles, leaveRequest.get());
		message = FILE_UPLOAD_SUCCESS;
		return ResponseEntity.status(HttpStatus.CREATED).body(message);
	}

	@GetMapping("/storage/{id}")
	public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable Long id) {
		if (id == null) throw new NullPointerException(INVALID_ID);

		List<FileInfo> fileInfos = storageService.loadAllById(id).map(path -> {
			String filename = path.getFileName().toString();
			String url =
					MvcUriComponentsBuilder.fromMethodName(FileStorageController.class, "getFile", id, filename).build().toString();
			return new FileInfo(filename, url, null);
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/storage/{id}/{filename:.+}")
	public ResponseEntity<Resource> getFile(@PathVariable Long id, @PathVariable String filename) {
		if (id == null || filename == null) throw new NullPointerException(INVALID_PAYLOAD);
		Resource file = storageService.getStorageFile(id, filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@GetMapping("/database/{id}")
	public ResponseEntity<List<FileInfo>> getFiles(@PathVariable Long id) {
		if (id == null) throw new NullPointerException(INVALID_PAYLOAD);
		List<FileInfo> files = storageService.getFilesByRequestId(id);
		return ResponseEntity.status(HttpStatus.OK).body(files);
	}

	@DeleteMapping("/{id}/{filename:.+}")
	public ResponseEntity<String> deleteFile(@PathVariable Long id, @PathVariable String filename) {
		if (id == null || filename == null) throw new NullPointerException(INVALID_PAYLOAD);
		String message = "";
		boolean existed = storageService.delete(id, filename);
		if (existed) {
			message = FILE_DELETE_SUCCESS + ": " + filename;
			return ResponseEntity.status(HttpStatus.OK).body(message);
		}
		message = FILE_NOT_EXISTS;
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
}
