package com.example.fie_service.service;

import io.minio.GenericResponse;
import io.minio.GetObjectResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {
    void deleteFile(String bucketName, String path);

    GetObjectResponse downloadFile(String fileName, String bucketName);

    GenericResponse uploadFile(MultipartFile file, String bucketName, String path);
}
