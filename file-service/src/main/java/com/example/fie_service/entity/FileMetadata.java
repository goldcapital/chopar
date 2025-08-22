package com.example.fie_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name ="file")
@Accessors(chain = true)
public class FileMetadata extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, name = "file_path")
    private String filePath;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;
}
