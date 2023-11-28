package com.kmini.store.config.file;

import org.springframework.web.multipart.MultipartFile;

public interface UserResourceManager {

    // 각자 유저 디렉토리에 해당하는 경로에서
    // 파일을 저장한다.
    String storeFile(String username, MultipartFile multipartFile);

    // 해당 파일이 있으면 삭제하고 교체한다.
    // 없으면 삭제하지 않는다.
    boolean updateFile(String fileName, MultipartFile multipartFile);

    // 해당 파일을 삭제한다.
    boolean deleteFile(String fileName);

}
