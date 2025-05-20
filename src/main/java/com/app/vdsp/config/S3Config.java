package com.app.vdsp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${DO.SPACES.KEY}")          private String key;
    @Value("${DO.SPACES.SECRET}")       private String secret;
    @Value("${DO.SPACES.ENDPOINT}")     private String endpoint;
    @Value("${DO.SPACES.REGION}")       private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(key, secret)))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner(S3Client s3) {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create(key, secret)))
                .region(Region.of(region))
                .build();
    }
}

