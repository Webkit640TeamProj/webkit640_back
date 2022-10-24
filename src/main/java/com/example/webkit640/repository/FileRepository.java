package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    Applicant findByApplicant_Id(int applicantId);

    Board findByBoard_Id(int boardId);
}
