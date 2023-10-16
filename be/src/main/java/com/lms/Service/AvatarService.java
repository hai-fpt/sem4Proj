package com.lms.service;

import com.lms.dto.AvatarInfo;
import com.lms.models.Avatar;
import com.lms.models.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AvatarService {
    void init();
    AvatarInfo saveToStorage(MultipartFile file, String subdir, String updatedBy) throws IOException;

    Avatar saveToDatabase(AvatarInfo file, User user);

    Resource getStorageFile(Long id);

    List<AvatarInfo> getDefaultFiles();

    void updateDefaultUserAvatar(String name, Long userId);

    List<String> getAvatarDefaults();

    Resource getAvatarDefault(String fileName);
}
