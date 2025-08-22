package com.example.fie_service.service.impl;

import com.example.fie_service.dto.response.FileManagementDTO;
import com.example.fie_service.entity.FileMetadata;
import com.example.fie_service.mapper.FileManagementMapper;
import com.example.fie_service.repository.FileManagementRepository;
import com.example.fie_service.service.FileService;
import com.example.fie_service.service.MinioService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.example.fie_service.exp.ErrorMessage.*;
import static com.example.fie_service.util.FilesUtils.*;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final MinioService minioService;
    private final FileManagementRepository fileManagementRepository;
    private final FileManagementMapper fileManagementMapper;

    private final BiConsumer<Boolean, String> throwIf = (condition, message) -> {
        if (condition) {
            throw new IllegalArgumentException(message);
        }
    };

    @Override
    public FileManagementDTO uploadFile(MultipartFile file, String bucketName) {

        throwIf.accept(Objects.isNull(file) || file.isEmpty(), FILE_MUST_NOT_NULL);
        throwIf.accept(Objects.isNull(bucketName) || bucketName.isEmpty(), BUCKET_NAME_MUST_NOT_NULL);

        var filename = getFileName(file.getOriginalFilename());
        var path = createPath(filename);

        var genericResponse = minioService.uploadFile(file, bucketName, filename);
        var saved = fileManagementRepository.save(fileManagementMapper.toEntity(path,
                filename, file.getOriginalFilename(), genericResponse.bucket()));

        return fileManagementMapper.toDto(saved);
    }

    @Override
    public FileManagementDTO downloadFile(UUID id, HttpServletResponse servletResponse) {
        var fileManagement = getFileOrThrow(id);
        var file = minioService.downloadFile(fileManagement.getFilePath(), fileManagement.getBucketName());
        byte[] byteContent = toByteArray(file);
        servletResponse.setContentType("application/octet-stream");
        servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileManagement.getFileName() + "\"");
        try (OutputStream os = servletResponse.getOutputStream()) {
            os.write(byteContent);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(format(THERE_WAS_AN_ERROR_UPLOADING_THE_FILE, id));
        }
        return null;
    }

    @Override
    public void deleteFile(UUID id) {
        var fileManagement = getFileOrThrow(id);
        try {
            minioService.deleteFile(fileManagement.getFilePath(), fileManagement.getBucketName());
        } catch (RuntimeException e) {
            log.error("Error deleting file : {}", fileManagement.getFilePath());
        }

    }

    private FileMetadata getFileOrThrow(UUID fileId) {
        return fileManagementRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException(format(FILE_NOT_FOUND, fileId)));
    }
}
