package com.kmini.store.config.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Profile({"default","local"})
@Component
@Slf4j
public class SystemFileManager implements ResourceManager {

    @Value("${file.dir}")
    private String fileDir;

    // 파일을 저장하고 경로를 반환
    @Override
    public String storeFile(MultipartFile multipartFile) {
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
        try {
            multipartFile.transferTo(destDir);
        } catch (Exception e) {
            throw new IllegalStateException("파일을 저장하는데에 실패했습니다", e);
        }
        return randomName;
    }

    @Override
    public void deleteFile(String randomName) {
        File file = new File(getFullPath(randomName));
        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        if (!file.delete()) {
            throw new IllegalStateException("파일이 제대로 지워지지 않았습니다.");
        }
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
