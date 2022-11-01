package com.example.webkit640.controller;

import com.example.webkit640.dto.request.BoardRequestDTO;
import com.example.webkit640.dto.response.BoardImageResponseDTO;
import com.example.webkit640.dto.response.BoardInspectResponseDTO;
import com.example.webkit640.dto.response.BoardListDataResponseDTO;
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
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/board")
@Slf4j
@RestController
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final FileService fileService;

    @Autowired
    public BoardController(MemberService memberService, BoardService boardService, FileService fileService) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @PostMapping("/save-board")
    public ResponseEntity<?> saveBoard(@AuthenticationPrincipal int id, @RequestBody BoardRequestDTO dto) {
        log.info("ENTER SAVE BOARD - Writer : " + memberService.findByid(id));
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
        log.info("LEAVE SAVE BOARD - Writer : " + memberService.findByid(id));
        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("/save-image")
    public ResponseEntity<?> uploadImage(@AuthenticationPrincipal int id, @RequestParam("image")MultipartFile image) {
        log.info("ENTER USER UPLOAD BOARD IMAGE - Writer : "+memberService.findByid(id).getEmail());
        try {
            FileEntity fileEntity = boardService.boardImageSave(image, memberService.findByid(id));
            if (fileEntity != null) {
                BoardImageResponseDTO res = BoardImageResponseDTO.builder().path(fileEntity.getFilePath()+"/"+fileEntity.getFileName()).build();
                log.info("LEAVE USER UPLOAD BOARD IMAGE - Writer : "+memberService.findByid(id).getEmail());
                return ResponseEntity.ok().body(res);
            } else {
                log.error("EXCEPTION USER UPLOAD BOARD IMAGE - Writer : "+memberService.findByid(id).getEmail());
                throw new RuntimeException();
            }
        } catch (IOException ie) {
            log.error("EXCEPTION USER UPLOAD BOARD IMAGE - Writer : "+memberService.findByid(id).getEmail());
            log.error("EXCEPTION : "+ie.getStackTrace());
            throw new RuntimeException();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllListData(@AuthenticationPrincipal int id, @RequestParam String type) {
        log.info("ENTER VIEW ALL BOARD - USER : "+memberService.findByid(id).getEmail());
        List<Board> boards = boardService.getBoardAll(type);
        List<BoardListDataResponseDTO> res = new ArrayList<>();
        for (Board board : boards) {
            BoardListDataResponseDTO dto = BoardListDataResponseDTO.builder()
                    .id(board.getId())
                    .writeDate(board.getCreateDate().toString())
                    .title(board.getTitle())
                    .writer(memberService.findByid(id).getName())
                    .build();
            res.add(dto);
        }
        log.info("LEAVE VIEW ALL BOARD - USER : " + memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(res);
    }
    @GetMapping("/list/{boardId}")
    public ResponseEntity<?> getInspectBoardData(@AuthenticationPrincipal int id, @PathVariable("boardId") int boardId) {
        log.info("ENTER INSPECT BOARD - ENTER URL : /list/"+boardId+ "USER : "+memberService.findByid(id).getEmail());
        Board board = boardService.getBoardId(boardId);
        BoardInspectResponseDTO dto = BoardInspectResponseDTO.builder()
                .createDate(board.getCreateDate().toString())
                .content(board.getContent())
                .title(board.getTitle())
                .writer(board.getMember().getName())
                .build();
        log.info("LEAVE INSPECT BOARD - LEAVE URL : /list/"+boardId+ "USER : "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(dto);
    }
}
