package com.example.fie_service.service;

import com.example.fie_service.dto.response.FileManagementDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    FileManagementDTO uploadFile(MultipartFile file, String bucketName);

    FileManagementDTO downloadFile(UUID id, HttpServletResponse servletResponse);

    void deleteFile(UUID id);
}
