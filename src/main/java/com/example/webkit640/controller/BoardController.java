package com.example.webkit640.controller;

import com.example.webkit640.dto.request.BoardRequestDTO;
import com.example.webkit640.dto.response.BoardImageResponseDTO;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.service.BoardService;
import com.example.webkit640.service.FileService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/board")
@Slf4j
@RestController
public class BoardController {

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Autowired
    FileService fileService;


    @PostMapping("/save-board")
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal int id, @RequestBody BoardRequestDTO dto) {
        List<FileEntity> emptyMemberFileEntity = boardService.getEmptyMemberFileEntity(id);
        Board savedBoard = null;
        if (dto.getType().equals("notification")) {
            savedBoard = boardService.saveBoard(Board.builder()
                    .member(memberService.findByid(id))
                    .boardType(dto.getType())
                    .content(dto.getContent())
                    .title(dto.getTitle())
                    .isAdd(true)
                    .build());
        } else {
            savedBoard = boardService.saveBoard(Board.builder()
                    .member(memberService.findByid(id))
                    .boardType(dto.getType())
                    .content(dto.getContent())
                    .title(dto.getTitle())
                    .build());
        }
        for (FileEntity file : emptyMemberFileEntity) {
            file.setBoard(savedBoard);
            fileService.modifyBoardFile(file);
        }
        savedBoard.setFiles(emptyMemberFileEntity);
        boardService.saveBoard(savedBoard);
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/save-image")
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal int id, @RequestParam("image")MultipartFile image) {
        try {
            FileEntity fileEntity = boardService.boardImageSave(image, memberService.findByid(id));
            if (fileEntity != null) {
                BoardImageResponseDTO res = BoardImageResponseDTO.builder().path(fileEntity.getFilePath()+"/"+fileEntity.getFileName()).build();
                return ResponseEntity.ok().body(res);
            } else {
                throw new RuntimeException();
            }
        } catch (IOException ie) {
            throw new RuntimeException();
        }
    }
}
