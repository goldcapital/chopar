package com.example.fie_service.repository;


import com.example.fie_service.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileManagementRepository extends JpaRepository<FileMetadata, UUID> {
}
