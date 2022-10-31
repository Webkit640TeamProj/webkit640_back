package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    FileEntity findByApplicant_Id(int applicantId);
    List<FileEntity> findByMember_Id(int memberId);
    List<FileEntity> findByMember_IdAndBoardIsNull(int memberId);
    Board findByBoard_Id(int boardId);
}
