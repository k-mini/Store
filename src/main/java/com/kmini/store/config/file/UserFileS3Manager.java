package com.kmini.store.config.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.IOException;

@Profile("aws")
@Component
@RequiredArgsConstructor
public class UserFileS3Manager implements UserResourceManager{

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String storeFile(String username, MultipartFile multipartFile) {

        String originalFilename = multipartFile.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        try {
            amazonS3.putObject(bucketName, originalFilename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        return amazonS3.getUrl(bucketName, originalFilename).toString();
        return originalFilename;
    }

    @Override
    public String updateFile(String fileName, MultipartFile multipartFile) {
        deleteFile(fileName);
        return storeFile(null, multipartFile);
    }

    @Override
    public boolean deleteFile(String fileName) {
//        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
        amazonS3.deleteObject(bucketName, fileName);
        return true;
    }

    @Override
    public File getFile(String uri) {
        try {
            throw new NotSupportedException("this method is not supported");
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
