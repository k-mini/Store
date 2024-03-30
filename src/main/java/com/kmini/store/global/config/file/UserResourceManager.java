package com.kmini.store.global.config.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

public interface UserResourceManager {

    // 각자 유저 디렉토리에 해당하는 경로에서
    // 파일을 저장한다.
    String storeFile(String username, MultipartFile multipartFile);

    default String storeFiles(String username, List<MultipartFile> multipartFiles) {
        StringBuilder itemImageURLBuilder = new StringBuilder();

        multipartFiles.stream()
                .filter(Objects::nonNull)
                .forEach((multipartFile) -> {
                    String imageUri = storeFile(username, multipartFile);
                    itemImageURLBuilder.append(imageUri).append(",");
                });

        return itemImageURLBuilder.toString();
    }

    // 해당 파일이 있으면 삭제하고 교체한다.
    // 없으면 삭제하지 않는다.
    String updateFile(String fileName, MultipartFile multipartFile);

    // 해당 파일을 삭제한다.
    boolean deleteFile(String fileName);

    // 해당 uri 찾기
    File getFile(String uri);

}
