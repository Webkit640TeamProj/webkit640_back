package com.example.webkit640.service;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private FileRepository fileRepository;

    public FileEntity saveFile(MultipartFile files, Applicant applicant) throws IOException {
        if (files.isEmpty()) {
            return null;
        }
        File Folder = new File(fileDir);
        if (!Folder.exists()) {
            try {
                Folder.mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        log.info(fileDir);
        String originalName = files.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        String savedName = applicant.getMember().getEmail() + "_apply_" + extension;
        String savedPath = fileDir + savedName;
        FileEntity dbFile = FileEntity.builder()
                .applicant(applicant)
                .fileExtension(extension)
                .fileName(savedName)
                .filePath(fileDir)
                .fileType("APPLY")
                .build();
        files.transferTo(new File(savedPath));
        FileEntity savedFile = fileRepository.save(dbFile);
        log.info(savedFile.toString());
        return savedFile;
    }
}
