package com.example.fie_service.mapper;


import com.example.fie_service.dto.response.FileManagementDTO;
import com.example.fie_service.entity.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FileManagementMapper {
    @Mapping(target = "filePath", source = "filePath")
    @Mapping(target = "fileName", source = "fileName")
    @Mapping(target = "bucketName", source = "bucket")
    @Mapping(target = "extension", expression = "java(getExtension(originalFilename))")
    FileMetadata toEntity(String filePath, String fileName, String originalFilename, String bucket);


    @Named("getExtension")
    default String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return null;
        int lastDot = fileName.lastIndexOf(".");
        return lastDot != -1 ? fileName.substring(lastDot) : null;
    }


    FileManagementDTO toDto(FileMetadata saved);
}
