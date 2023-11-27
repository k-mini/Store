package com.kmini.store.config.file;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceManager {

    /**
     * @param dirPath : 저장할 논리적 경로
     * @param multipartFile : 저장할 파일
     * @return 저장된 랜덤 값의 파일명
     */
    String storeFile(String dirPath, MultipartFile multipartFile);

    void deleteFile(String uri);

}
