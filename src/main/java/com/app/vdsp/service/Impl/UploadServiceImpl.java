package com.app.vdsp.service.Impl;

import com.app.vdsp.dto.PresignedResponseDto;
import com.app.vdsp.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UploadServiceImpl implements UploadService {

    //private final S3Client s3;
    private final S3Presigner presigner;

    @Value("${DO.CDN.BUCKET}")
    private String bucket;

    @Value("${DO.CDN.HOST}")
    private String cdnHost;


    @Autowired
    public UploadServiceImpl(S3Client s3, S3Presigner p) {
        //this.s3 = s3;
        this.presigner = p;
    }

    @Override
    public List<PresignedResponseDto> generatePresignedUrls(List<String> filenames) {
        return filenames.stream().map(name -> {
            String key = UUID.randomUUID().toString();
            String extension = ".webp";
            String objectKey = key + extension;


            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(objectKey)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            PresignedPutObjectRequest presigned = presigner.presignPutObject(
                    PutObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .putObjectRequest(putRequest)
                            .build());

            return new PresignedResponseDto(
                    objectKey,
                    presigned.url().toString(),
                    cdnHost + "/" + objectKey
            );
        }).collect(Collectors.toList());
    }
}

