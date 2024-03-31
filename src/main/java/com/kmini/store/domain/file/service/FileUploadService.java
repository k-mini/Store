package com.kmini.store.domain.file.service;

import com.kmini.store.domain.file.dao.UserResourceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final UserResourceManager userResourceManager;

    @Transactional
    public String storeUserFiles(String username, List<MultipartFile> multipartFiles) {
        return userResourceManager.storeFiles(username, multipartFiles);
    }

    @Transactional
    public String storeUserFile(String username, MultipartFile multipartFile) {
        return userResourceManager.storeFile(username, multipartFile);
    }

    @Transactional
    public String updateUserFile(String fileName, MultipartFile multipartFile) {
        return userResourceManager.updateFile(fileName, multipartFile);
    }

    @Transactional
    public boolean deleteUserFile(String fileName) {
        return userResourceManager.deleteFile(fileName);
    }
}
