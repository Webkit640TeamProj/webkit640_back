package com.example.webkit640.dto;

import com.example.webkit640.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private int id;
    private String fileName;
    private String fileExtension;
    private String filePath;
    private String fileType;

    public static FileDTO entityToDTO(FileEntity file) {
        return FileDTO.builder()
                .filePath(file.getFilePath())
                .fileType(file.getFileType())
                .fileName(file.getFileName())
                .fileExtension(file.getFileExtension())
                .build();
    }
}
