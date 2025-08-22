package com.example.fie_service.controller;


import com.example.fie_service.config.Constants;
import com.example.fie_service.dto.response.FileManagementDTO;
import com.example.fie_service.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileManagementController {
    private final FileService fileService;

    @PostMapping(value = "/upload")
    public ResponseEntity<FileManagementDTO> uploadFile(@RequestParam(name = "file")
                                                          MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadFile(file, Constants.BUCKET_NAME));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<FileManagementDTO>downloadFile(@PathVariable UUID id,
                                                                            HttpServletResponse servletResponse) {
        return ResponseEntity.ok(fileService.downloadFile(id, servletResponse));
    }

    @DeleteMapping("/delete-by/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable("id") UUID id) {
        fileService.deleteFile(id);
        return ResponseEntity.accepted().build();
    }
}
