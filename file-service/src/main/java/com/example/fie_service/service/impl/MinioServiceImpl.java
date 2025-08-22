package com.example.fie_service.service.impl;

import com.example.fie_service.exp.FileException;
import com.example.fie_service.service.MinioService;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.example.fie_service.enums.Errors.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Override
    public void deleteFile(String bucketName, String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(checkBucketName(bucketName))
                            .object(path)
                            .build()
            );
            log.info("File deleted successfully from MINIO: {}/{}", bucketName, path);
        }catch (Exception e){
            log.error("Cannot delete file from MINIO -> {}", e.getMessage());
            throw new FileException(FILE_DELETE_FAIL);
        }
    }

    @Override
    public GetObjectResponse downloadFile(String fileName, String bucketName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(checkBucketName(bucketName))
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new FileException(FILE_DOWNLOAD_FAIL);
        }

    }

    @Override
    public GenericResponse uploadFile(MultipartFile file, String bucketName, String path) {
        try {
            return minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(checkBucketName(bucketName))
                            .object(path)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            log.error("Cannot upload file to MINIO -> {}", e.getMessage());
            throw new FileException(FILE_UPLOAD_FAIL);
        }

    }

    private String checkBucketName(String bucketName) {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                log.info("Bucket exists with same name.{}", bucketName);
                return bucketName;
            }
            log.info("Bucket doesn't exist.{}", bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return bucketName;
        } catch (Exception e) {
            log.error("Check on bucket exception: {}", e.getMessage());
            throw new FileException(INVALID_BUCKET_NAME);
        }

    }
}
