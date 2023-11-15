package com.kmini.store.config.file;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceManager {

    String storeFile(MultipartFile file);

    void deleteFile(String uri);
}
