package com.kmini.store.domain.file.dao;

import com.kmini.store.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Profile({"default", "local", "test"})
@Component
@Slf4j
public class UserFileLocalManager implements UserResourceManager {

    // 최상위 루트
    @Value("${file.dir}")
    private String fileDir;

    // 파일을 저장하고 파일명 반환
    @Override
    public String storeFile(String username, MultipartFile multipartFile) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String ext = extractExt(originalFilename);
        String randomName = createRandomFileName(ext);
        String realUserDirectoryPath = getRealPath(username);

        log.trace("[{}] 저장 될 디렉토리 = {}", MDC.get("transactionId"), realUserDirectoryPath);
        File directory = new File(realUserDirectoryPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(plusPath(realUserDirectoryPath, randomName));

        try {
            multipartFile.transferTo(file);
            log.info("[{}] 성공적으로 저장되었습니다. 파일 경로 : {}",MDC.get("transactionId"),
                    file.getCanonicalPath());
        } catch (Exception e) {
            throw new IllegalStateException("파일을 저장하는데에 실패했습니다", e);
        }
        return plusPath(username, randomName);
    }

    @Override
    public String updateFile(String fileName, MultipartFile multipartFile) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        if (!StringUtils.hasText(fileName)) {
            return storeFile(User.getSecurityContextUser().getUsername(), multipartFile);
        }

//        File file = new File(fileDir + User.getSecurityContextUser().getEmail() +  fileName);
        File file = new File(plusPath(fileDir, fileName));

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

    @Override
    public File getFile(String uri) {
        log.trace("uri = {}", uri);
        return new File(getRealPath(uri));
    }

    // ex1) abcd + eftg => abcd/eftg
    // ex) abcd/ + /zxcv/ => abcd/zxcv/
    private String plusPath(String path1, String path2) {
        if (path1.startsWith("/")) {
            return UriComponentsBuilder
                    .fromPath(path1)
                    .path(path2)
                    .toUriString();
        }
        return UriComponentsBuilder
                .fromPath(path1)
                .pathSegment(path2)
                .toUriString();
    }

    private String plusPaths(String... paths) {
        Assert.notEmpty(paths, "paths 값이 비어있습니다.");
        if (paths.length == 1) {
            return paths[0];
        }

        String answer = paths[0];
        for (int i = 1; i < paths.length; i++) {
            answer = plusPath(answer, paths[i]);
        }
        return answer;
    }

    private String getRealPath(String path) {
        return UriComponentsBuilder
                .fromPath(fileDir)
                .path(path)
                .toUriString();
    }

    private String getRealUserDirectoryPath() {
        return getRealPath(User.getSecurityContextUser().getEmail());
    }

    private String createRandomFileName(String ext) {
        return UUID.randomUUID().toString() + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
