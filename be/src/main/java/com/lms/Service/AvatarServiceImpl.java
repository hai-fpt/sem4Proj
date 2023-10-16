package com.lms.service;

import com.lms.dto.AvatarInfo;
import com.lms.models.Avatar;
import com.lms.models.User;
import com.lms.repository.AvatarRepository;
import com.lms.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AvatarServiceImpl implements AvatarService{
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;

    public AvatarServiceImpl(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    private final Path root = Paths.get("avatar");
    private final Path rootDefault = Paths.get("avatar/default");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for avatars");
        }
    }

    @Override
    public AvatarInfo saveToStorage(MultipartFile file, String subdir, String updatedBy) throws IOException {
        String subdirPath = this.root + "/" + subdir;
        Path path = Paths.get(subdirPath);
        if (!Files.exists(path) && !Files.isDirectory(path)) {
            Files.createDirectories(path);
        }
        try (Stream<Path> pathStream = Files.list(path)) {
            pathStream.forEach(existingFile -> {
                try {
                    Files.delete(existingFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        Path filePath = path.resolve(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new AvatarInfo(file.getOriginalFilename(), path.toString(), updatedBy);
    }

    @Override
    @Transactional
    public Avatar saveToDatabase(AvatarInfo file, User user) {
        Optional<Avatar> existingAvatar = avatarRepository.findByUser(user);
        if (existingAvatar.isPresent()) {
            Avatar oldAvatar = existingAvatar.get();
            oldAvatar.setName(file.getName());
            oldAvatar.setPath(file.getPath());
            oldAvatar.setUpdatedBy(file.getUpdatedBy());
            return avatarRepository.save(oldAvatar);
        } else {
            Avatar avatar = new Avatar(
                    file.getName(),
                    file.getPath(),
                    user,
                    file.getUpdatedBy()
            );

            return avatarRepository.save(avatar);
        }
    }

    @Override
    public Resource getStorageFile(Long id) {
        Path path = root.resolve(id.toString());
        if (Files.isDirectory(path)) {
            try (Stream<Path> files = Files.list(path)) {
                Optional<Path> firstFile = files.findFirst();
                if (firstFile.isPresent()) {
                    Path filePath = firstFile.get();
                    Resource resource = new UrlResource(filePath.toUri());
                    if (resource.exists() || resource.isReadable()) {
                        return resource;
                    } else {
                        throw new RuntimeException("Could not read file");
                    }
                } else {
                    throw new RuntimeException("No file found");
                }
            } catch (IOException e) {
                throw new RuntimeException("Error accessing folder");
            }
        } else {
            throw new RuntimeException("User folder not found");
        }
    }

    @Override
    public List<AvatarInfo> getDefaultFiles() {
        try {
            List<AvatarInfo> list = new ArrayList<>();
            Path folderPath = rootDefault;
            if (Files.isDirectory(folderPath)) {
                try (Stream<Path> pathStream = Files.list(folderPath)) {
                    pathStream.forEach(path -> {
                        String fileName = path.getFileName().toString();
                        Path relative = folderPath.relativize(path);
                        String avatarPath = "avatar/default/" + relative.toString();
                        AvatarInfo avatarInfo = new AvatarInfo(fileName, avatarPath);
                        list.add(avatarInfo);
                    });
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateDefaultUserAvatar(String name, Long userId) {
        Path defaultAvatarPath = Paths.get(this.root + "/default/" + name);
        if (!Files.exists(defaultAvatarPath) || !Files.isRegularFile(defaultAvatarPath)) {
            throw new RuntimeException("Chosen file not found");
        }
        String userFolderPath = this.root + "/" + userId;
        Path userPath = Paths.get(userFolderPath);
        if (!Files.exists(userPath) && !Files.isDirectory(userPath)) {
            try {
                Files.createDirectories(userPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create");
            }
        }
        try (Stream<Path> userFiles = Files.list(userPath)) {
            userFiles.forEach(existingFile -> {
                try {
                    Files.delete(existingFile);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to delete");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error");
        }
        Path userAvatarPath = Paths.get(userFolderPath + "/" + name);
        try {
            Files.copy(defaultAvatarPath, userAvatarPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to copy");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Avatar> existingAvatar = avatarRepository.findByUser(user);
        if (existingAvatar.isPresent()) {
            Avatar avatar = existingAvatar.get();
            avatar.setName(name);
            avatar.setPath("avatar/" + userId);
            avatar.setUpdatedBy(user.getEmail());
            avatarRepository.save(avatar);
        } else {
            Avatar newAvatar = new Avatar(name, "avatar/" + userId, user, user.getEmail());
            avatarRepository.save(newAvatar);
        }
    }

    @Override
    public List<String> getAvatarDefaults() {
        Path rootDefault = this.rootDefault;
        if (Files.isDirectory(rootDefault)) {
            try (Stream<Path> files = Files.list(rootDefault)) {
                return files.map(filePath -> filePath.toString()).collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException("Could not access folder " + rootDefault);
            }
        } else {
            throw new RuntimeException("Default folder not found");
        }
    }

    @Override
    public Resource getAvatarDefault(String fileName) {
        Path path = rootDefault.resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Could not read");
        }
        return resource;
    }
}
