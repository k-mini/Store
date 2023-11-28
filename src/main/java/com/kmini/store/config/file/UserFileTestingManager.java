package com.kmini.store.config.file;

import com.kmini.store.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Profile({"default","local","test"})
@Component
@Slf4j
public class UserFileTestingManager implements UserResourceManager {

    // 최상위 루트
    @Value("${file.dir}")
    private String fileDir;

    public String storeFileInUserDirectory(MultipartFile multipartFile) {
        return storeFile(User.getSecurityContextUser().getEmail(), multipartFile);
    }

    // 파일을 저장하고 경로를 반환
    @Override
    public String storeFile(String email, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = extractExt(originalFilename);
        String randomName = createRandomFileName(ext);
        log.info("dirPath = {}" , email);
        String realDirectoryPath = getRealDirectoryPath(email);
        File directory = new File(realDirectoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file= new File(plusPath(realDirectoryPath ,randomName));

        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            throw new IllegalStateException("파일을 저장하는데에 실패했습니다", e);
        }
        return randomName;
    }

    @Override
    public String updateFile(String fileName, MultipartFile multipartFile) {

        if (!StringUtils.hasText(fileName)) {
            storeFile(User.getSecurityContextUser().getEmail(), multipartFile);
            return null;
        }

        File file = new File(fileDir + User.getSecurityContextUser().getEmail() +  fileName);

        boolean deleted = false;
        if (file.exists()) {
            deleted = file.delete();
            log.info("파일이 존재하여 삭제하였습니다. 경로 : {}", file.getAbsolutePath());
        }

        File newfile = new File(file.getAbsolutePath());
        try {
            multipartFile.transferTo(newfile);
        } catch (IOException e) {
            throw new IllegalStateException("파일을 저장하는데에 실패했습니다", e);
        }
        return fileName;
    }

    @Override
    public boolean deleteFile(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return false;
        }
        String path = plusPath(getRealUserDirectoryPath(), fileName);
        File file = new File(path);
        return file.delete();
    }


    private String plusPath(String dirPath, String fileName) {
        if (!fileName.startsWith("/") && !dirPath.endsWith("/")) {
            fileName = "/" + fileName;
        }
        if (fileName.startsWith("/") && dirPath.endsWith("/")) {
            fileName = fileName.substring(1);
        }
        return dirPath + fileName;
    }

    private String getRealDirectoryPath(String dirPath) {
        if (dirPath.startsWith("/")) {
            dirPath = dirPath.substring(1);
        }
        return fileDir + dirPath;
    }

    private String getRealUserDirectoryPath() {
        return getRealDirectoryPath(User.getSecurityContextUser().getEmail());
    }

    private String createRandomFileName(String ext) {
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
