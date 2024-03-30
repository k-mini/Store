package com.kmini.store.global.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("aws")
@Configuration
public class S3Config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    // 로컬환경에서 테스트 시 autoconfigure를 비활성화 해주게 되는데
    // 그럴 경우 s3 client 빈 초기화가 제대로 이루어지지 않음 (region이 us-west로 설정되는 등)
    // 그런 상황에서 @primary 를 적용하면 빈 주입이 ap-northeast-2로 설정됨
    @Primary
    @Bean
    public AmazonS3 amazonS3Client() {
        log.info("amazonS3Client 빈 등록..");
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3Client amazonS3Client = (AmazonS3Client) AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        return amazonS3Client;
    }
}
