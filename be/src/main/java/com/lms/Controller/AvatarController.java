package com.lms.controller;

import com.lms.dto.AvatarInfo;
import com.lms.models.User;
import com.lms.service.AvatarService;
import com.lms.service.ConfigurationService;
import com.lms.service.UserService;
import com.lms.utils.ControllerUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lms.utils.Constants.*;

@RestController
@RequestMapping("/api/avatar")
public class AvatarController {
    private final AvatarService avatarService;
    private final ControllerUtils controllerUtils;
    private final UserService userService;
    private final ConfigurationService configurationService;

    public AvatarController(AvatarService avatarService, ControllerUtils controllerUtils, UserService userService, ConfigurationService configurationService) {
        this.avatarService = avatarService;
        this.controllerUtils = controllerUtils;
        this.userService = userService;
        this.configurationService = configurationService;
    }

    private final String baseAddress = "https://localhost:9000";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @RequestParam("userId") Long userId,
                                               @RequestParam("updatedBy") String updatedBy) throws IOException {
        if (userId == null || file == null)
            throw new NullPointerException(INVALID_PAYLOAD);
        if (!controllerUtils.validateRequestedUser(updatedBy)) {
            throw new NullPointerException(EMAIL_NOT_EXISTS);
        }
        String message = "";
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        AvatarInfo savedAvatar = avatarService.saveToStorage(file, Long.toString(userId), updatedBy);
        avatarService.saveToDatabase(savedAvatar, user.get());
        message = FILE_UPLOAD_SUCCESS;
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/default")
    public ResponseEntity<List<AvatarInfo>> getDefaultAvatar() {
        List<AvatarInfo> avatarInfos = avatarService.getDefaultFiles();
        return ResponseEntity.status(HttpStatus.OK).body(avatarInfos);
    }

    @GetMapping("/default/display")
    public ResponseEntity<List<String>> getAvatarDefault() {
        List<String> resources = avatarService.getAvatarDefaults();
        if (resources.isEmpty()) {
            throw new RuntimeException("No default files found");
        }
        List<String> urls = resources.stream().map(file -> baseAddress + "/api/" + file).collect(Collectors.toList());
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/default/{fileName:.+}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String fileName) throws IOException {
        Resource avatar = avatarService.getAvatarDefault(fileName);
        byte[] imageBytes = Files.readAllBytes(avatar.getFile().toPath());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes, httpHeaders, 200);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("id") Long id) throws IOException{
        if (id == null) throw new NullPointerException(INVALID_PAYLOAD);
        Resource file = avatarService.getStorageFile(id);
        byte[] imageBytes = Files.readAllBytes(file.getFile().toPath());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(imageBytes, httpHeaders, 200);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Void> updateDefaultUserAvatar(@PathVariable Long userId, @RequestParam String fileName) {
        if (!controllerUtils.validateRequestedUser(userId)) {
            throw new NullPointerException(USER_NOT_EXISTS);
        }
        avatarService.updateDefaultUserAvatar(fileName, userId);
        return ResponseEntity.ok().build();
    }
}
