package com.lms.service;

import com.lms.dto.FileInfo;
import com.lms.models.FileStorage;
import com.lms.models.UserLeave;
import com.lms.repository.FileStorageRepository;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  @Autowired
  FileStorageRepository fileStorageRepository;

  @Autowired
  ConfigurationService configurationService;

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public List<FileInfo> saveToStorage(MultipartFile[] files, String subdirectory, String requestedByEmail) throws IOException {
    Integer limit = configurationService.getConfiguration().getLimitAttachment();
    Integer numberOfFiles = fileStorageRepository.countAllByLeaveRequest_Id(Long.valueOf(subdirectory));
    if (limit != null && (numberOfFiles >= limit || numberOfFiles + files.length > limit)) {
      throw new FileSizeLimitExceededException("illegal state: maximal count (" + limit + ") exceeded", numberOfFiles, 5);
    }
    String subdirectoryPath = this.root + "/" + subdirectory;
    Path path = Paths.get(subdirectoryPath);
    if (!Files.exists(path) && !Files.isDirectory(path)) {
      Files.createDirectory(path);
    }
    List<FileInfo> savedFiles = new ArrayList<>();
    Arrays.stream(files).forEach(file -> {
      Path filePath = path.resolve(Objects.requireNonNull(file.getOriginalFilename()));
      try {
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      savedFiles.add(new FileInfo(file.getOriginalFilename(), path.toString(), requestedByEmail));
    });
    return savedFiles;
  }

  @Override
  public List<FileStorage> saveToDatabase(List<FileInfo> files, UserLeave leaveRequest) {
    List<FileStorage> fileStorages = files.stream().map(savedFile ->
            new FileStorage(
                    savedFile.getName(),
                    savedFile.getPath(),
                    leaveRequest,
                    savedFile.getUpdatedBy())
    ).collect(Collectors.toList());
    return fileStorageRepository.saveAll(fileStorages);
  }

  @Override
  public Resource getStorageFile(Long id, String filename) {
    try {
      Path file = root.resolve(id + "/" + filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public List<FileInfo> getFilesByRequestId(Long id) {
    List<FileStorage> dbFiles = fileStorageRepository.findAllByLeaveRequest_Id(id);
    return dbFiles.stream().map(fileStorage ->
            new FileInfo(
                    fileStorage.getName(),
                    fileStorage.getPath(),
                    fileStorage.getUpdatedBy())
    ).collect(Collectors.toList());
  }

  @Override
  public boolean delete(Long id, String filename) {
    try {
      Path file = root.resolve(id + "/" + filename);
      return Files.deleteIfExists(file);
    } catch (IOException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAllById(Long id) {
    try {
      Path folder = root.resolve(id.toString());
      return Files.walk(folder, 1).filter(path -> !path.equals(folder)).map(folder::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

}
