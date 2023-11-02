package com.kmini.store.config.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
public class FileUploader {

    @Value("${file.dir}")
    private String fileDir;

    // 파일을 저장하고 경로를 반환
    public String storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = extractExt(originalFilename);
        String randomName = createRandomFileName(ext);
        String fullPath = getFullPath(randomName);
        log.info("fullPath = {}" ,fullPath);
        File destDir = new File(fullPath);

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        multipartFile.transferTo(destDir);
        return randomName;
    }

    private String getFullPath(String randomFileName) {
        return fileDir + randomFileName;
    }

    private String createRandomFileName(String ext) {
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
