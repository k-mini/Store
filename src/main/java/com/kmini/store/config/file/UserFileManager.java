package com.kmini.store.config.file;

import com.kmini.store.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Profile({"default","local","test"})
@Component
@Slf4j
public class UserFileManager implements ResourceManager {

    @Value("${file.dir}")
    private String fileDir;

    public String storeFileInUserDirectory(MultipartFile multipartFile) {
        return storeFile(User.getSecurityContextUser().getEmail(), multipartFile);
    }

    // 파일을 저장하고 경로를 반환
    @Override
    public String storeFile(String dirPath, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = extractExt(originalFilename);
        String randomName = createRandomFileName(ext);
        log.info("dirPath = {}" , dirPath);
        String realDirectoryPath = getRealDirectoryPath(dirPath);
        File directory = new File(realDirectoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file= getFullPath(realDirectoryPath, randomName);

        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            throw new IllegalStateException("파일을 저장하는데에 실패했습니다", e);
        }
        return randomName;
    }

    @Override
    public void deleteFile(String randomName) {
        File file = new File(getUserDirectoryPath() + randomName);
        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다.");
        }

        if (!file.delete()) {
            throw new IllegalStateException("파일이 제대로 지워지지 않았습니다.");
        }
    }

    private File getFullPath(String dirPath, String randomName) {
        return new File(dirPath + randomName);
    }

    private String getUserDirectoryPath() {
        User user = User.getSecurityContextUser();
        if (user == null) {
            throw new IllegalStateException("유저 디렉토리를 찾을 수 없습니다.");
        }
        return getRealDirectoryPath(user.getEmail());
    }

    private String getRealDirectoryPath(String path) {
        return fileDir + path + "/";
    }

    private String createRandomFileName(String ext) {
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
