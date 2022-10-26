package com.example.webkit640.dto;

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
}
