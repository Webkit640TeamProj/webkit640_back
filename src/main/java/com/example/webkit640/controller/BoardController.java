package com.example.webkit640.controller;

import com.example.webkit640.dto.request.BoardRequestDTO;
import com.example.webkit640.dto.request.ModifiedBoardRequestDTO;
import com.example.webkit640.dto.request.ModifiedReplyDTO;
import com.example.webkit640.dto.request.ReplyRequestDTO;
import com.example.webkit640.dto.response.*;
import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Image;
import com.example.webkit640.entity.Member;
import com.example.webkit640.service.BoardService;
import com.example.webkit640.service.FileService;
import com.example.webkit640.service.ImageService;
import com.example.webkit640.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"})
@RequestMapping("/board")
@Slf4j
@RestController
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final FileService fileService;

    private final ImageService imageService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public BoardController(MemberService memberService, BoardService boardService, FileService fileService, ImageService imageService, ResourceLoader resourceLoader) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.fileService = fileService;
        this.imageService = imageService;
        this.resourceLoader = resourceLoader;
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
        return ResponseEntity.ok().body(savedBoard.getId());
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

    @PostMapping("/save-file/{boardId}")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal int id,@RequestParam MultipartFile file, @PathVariable("boardId") int boardId) throws IOException {
        log.info("ENTER USER UPLOAD BOARD IMAGE - Writer : "+memberService.findByid(id).getEmail());
        if (file == null) {
            log.error("FILE ERROR /board/save-file -Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("ERROR");
        }
        if (!file.isEmpty()) {
            Member member = memberService.findByid(id);
            Board board = null;
            List<Board> boards = boardService.getByWriter(id);
            for (Board b : boards) {
                if(b.getId() == boardId) {
                    board = b;
                }
            }
            FileEntity fileEntity = fileService.saveBoardFile(file, board, member);
            List<FileEntity> modifyMemberFile = member.getFile();
            modifyMemberFile.add(fileEntity);
            member.setFile(modifyMemberFile);
            memberService.save(member);

            List<FileEntity> modifyBoardFile = board.getFiles();
            modifyBoardFile.add(fileEntity);
            board.setFiles(modifyBoardFile);
            boardService.saveBoard(board);

            log.info("LEAVE /board/save-file - Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body("ok");
        } else {
            log.error("ERROR /board/save-file -Accessor: "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @PutMapping("/change-show")
    public ResponseEntity<?> changeShow(@AuthenticationPrincipal int id, @RequestBody int boardId) {
        try {
            log.info("ENTER /board/change-show - Accessor : "+memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            log.info("EXCEPTION /board/change-show");
            return ResponseEntity.badRequest().body("NO AUTH");
        }
        Board board = boardService.changeShowBoard(boardId);
        if (board == null) {
            log.info("BOARD NULL ERROR /board/change-show - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("NO BOARD");
        }
        return ResponseEntity.ok().body(board.getId() + "change View : " + board.isAdd());
    }

//    @PostMapping(value = "/download/{boardId}",headers = "HEADER")
//    public ResponseEntity<?> fileDownload(@RequestBody String fileName, @PathVariable("boardId") int boardId, @RequestHeader HttpHeaders request) {
//        log.info(request.getFirst("HEADER"));
//        try {
//            Board board = boardService.getBoardId(boardId);
//            FileEntity file = null;
//            List<FileEntity> files = fileService.findByBoardId(board);
//            for (FileEntity fe : files) {
//                if (fe.getFileName().equals(fileName)) {
//                    file = fe;
//                }
//            }
//
//            Resource resource = resourceLoader.getResource("file:"+file.getFilePath()+file.getFileName());
//            File binaryFile = resource.getFile();
//
//            //String encodeFileName = URLEncoder.encode(binaryFile.getName(),"UTF-8");
//            log.info("LEAVE /board/download");
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(binaryFile.getName(),"UTF-8").replaceAll("\\+","%20") + "\"" )
//                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryFile.length()))
//                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
//                    .body(resource);
//
//        } catch (FileNotFoundException e) {
//            log.error("FILE NOT FOUND EXCEPTION /board/download");
//            log.error(e.getMessage());
//            return ResponseEntity.badRequest().body(null);
//        } catch (IOException e) {
//            log.error("FILE NOT FOUND EXCEPTION /board/download");
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @GetMapping("/download-upload-file")
    public ResponseEntity<?> downloadUploadFile(@AuthenticationPrincipal int id, @RequestParam int boardId) {
        try {
            log.info("ENTER /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
        } catch (NullPointerException ne ) {
            log.error("NULl EXCEPTION /board/download-upload-file");
            return ResponseEntity.badRequest().body("NULl EXCEPTION /board/download-upload-file");
        }
        Board board = boardService.getBoardId(boardId);
        List<FileEntity> files = fileService.findByBoardId(board);
        FileEntity entity = files.get(0);

        Resource resource = resourceLoader.getResource("file:"+entity.getFilePath()+entity.getFileName());
        File resultFile = null;
        try {
            resultFile = resource.getFile();
        } catch (IOException e) {
            log.error("IO EXCEPTION /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("IO EXCEPTION /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
        }
        log.info("LEAVE /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
        try {
            String encodeFileName = new String(resultFile.getName().getBytes("KSC5601"),"ISO-8859-1");
            log.info(encodeFileName);
            log.info(resultFile.getName());
            log.info(URLEncoder.encode((resultFile.getName()),"UTF-8"));

            /*URLEncoder.encode(resultFile.getName(),"UTF-8").replaceAll("\\+","%20")*/
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode((resultFile.getName()),"UTF-8") + "\"" )
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resultFile.length()))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            log.error("ENCODING EXCEPTION /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
            return ResponseEntity.badRequest().body("ENCODING EXCEPTION /board/download-upload-file - Accessor : " + memberService.findByid(id).getEmail());
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
                    .writer(board.getMember().getName())
                    .cnt(board.getCnt())
                    .isAdd(board.isAdd())
                    .build();
            res.add(dto);
        }
        Collections.reverse(res);
        log.info("LEAVE VIEW ALL BOARD - USER : " + memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/list-review")
    public ResponseEntity<?> getReviewListData(@RequestParam String type) {
        log.info("ENTER /board/list-review ");
        List<Board> boards = boardService.getBoardAll(type);
        List<BoardListDataResponseDTO> res = new ArrayList<>();
        for (Board board : boards) {
            BoardListDataResponseDTO dto = BoardListDataResponseDTO.builder()
                    .id(board.getId())
                    .writeDate(board.getCreateDate().toString())
                    .title(board.getTitle())
                    .writer(board.getMember().getName())
                    .cnt(board.getCnt())
                    .isAdd(board.isAdd())
                    .build();
            res.add(dto);
        }
        Collections.reverse(res);
        log.info("LEAVE /board/list-review");
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/list-review/{boardId}")
    public ResponseEntity<?> getInspectReviewBoardData(@PathVariable("boardId") int boardId) {
        log.info("ENTER /board/list-review/" + boardId);
        Board board = boardService.getBoardId(boardId);
        List<String> fileNames = new ArrayList<>();
        List<FileEntity> files = fileService.findByBoardId(board);
        for (FileEntity fe : files) {
            if (fe.getFileType().equals("BOARD"))
                fileNames.add(fe.getFileName());
        }

        List<ReplyListDataResponseDTO> replies = new ArrayList<>();
        for (Board reply : board.getBoards()) {
            ReplyListDataResponseDTO replyDTO = ReplyListDataResponseDTO.builder()
                    .id(reply.getId())
                    .writer(reply.getMember().getName())
                    .content(reply.getContent())
                    .createDate(reply.getCreateDate().toString())
                    .updateDate(reply.getUpdateDate().toString())
                    .build();
            replies.add(replyDTO);
        }

        BoardInspectResponseDTO dto = BoardInspectResponseDTO.builder()
                .createDate(board.getCreateDate().toString())
                .fileNames(fileNames)
                .content(board.getContent())
                .title(board.getTitle())
                .cnt(board.getCnt())
                .writer(board.getMember().getName())
                .replies(replies)
                .build();

        int cnt = board.getCnt();
        cnt+=1;
        board.setCnt(cnt);
        boardService.saveBoard(board);
        log.info("ENTER /board/list-review/" + boardId);
        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/list/{boardId}")
    public ResponseEntity<?> getInspectBoardData(@AuthenticationPrincipal int id, @PathVariable("boardId") int boardId) {
        log.info("ENTER INSPECT BOARD - ENTER URL : /list/"+boardId+ "USER : "+memberService.findByid(id).getEmail());
        Board board = boardService.getBoardId(boardId);
        List<String> fileNames = new ArrayList<>();
        List<FileEntity> files = fileService.findByBoardId(board);
        for (FileEntity fe : files) {
            if (fe.getFileType().equals("BOARD"))
            fileNames.add(fe.getFileName());
        }

        List<ReplyListDataResponseDTO> replies = new ArrayList<>();
        for (Board reply : board.getBoards()) {
            ReplyListDataResponseDTO replyDTO = ReplyListDataResponseDTO.builder()
                    .id(reply.getId())
                    .writer(reply.getMember().getName())
                    .content(reply.getContent())
                    .createDate(reply.getCreateDate().toString())
                    .updateDate(reply.getUpdateDate().toString())
                    .build();
            replies.add(replyDTO);
        }

        BoardInspectResponseDTO dto = BoardInspectResponseDTO.builder()
                .createDate(board.getCreateDate().toString())
                .fileNames(fileNames)
                .content(board.getContent())
                .title(board.getTitle())
                .cnt(board.getCnt())
                .writer(board.getMember().getName())
                .replies(replies)
                .build();

        int cnt = board.getCnt();
        cnt+=1;
        board.setCnt(cnt);
        boardService.saveBoard(board);
        log.info("LEAVE INSPECT BOARD - LEAVE URL : /list/"+boardId+ "USER : "+memberService.findByid(id).getEmail());
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/update-board/{boardId}")
    public ResponseEntity<?> updateBoard(@AuthenticationPrincipal int id, @PathVariable("boardId") int boardId, @RequestBody ModifiedBoardRequestDTO dto) {
        Board board = boardService.getBoardId(boardId);
        if(board.getMember().getId() == id) {
            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            boardService.saveBoard(board);
        }
        return ResponseEntity.ok().body("update ok");
    }
    @DeleteMapping("/delete-board/{boardId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal int id, @PathVariable("boardId") int boardId ) {
        Board board = boardService.getBoardId(boardId);
        if(board.getMember().getId() == id) {
            boardService.deleteBoard(boardId);
        }
        return ResponseEntity.ok().body("delete ok");
    }

    @PostMapping("/save-reply")
    public ResponseEntity<?> saveReply(@AuthenticationPrincipal int id, @RequestBody ReplyRequestDTO dto) {
        Board Reply = null;
        if(dto.getType().equals("reply")) {
            Reply = boardService.saveReply(Board.builder()
                    .board(boardService.getBoardId(dto.getBoardId()))
                    .member(memberService.findByid(id))
                    .boardType(dto.getType())
                    .content(dto.getContent())
                    .title("reply")
                    .isAdd(true)
                    .build());
        }
        Board board = Reply.getBoard();
        List<Board> saveReplies = board.getBoards();
        saveReplies.add(Reply);
        board.setBoards(saveReplies);
        boardService.saveBoard(board);
        boardService.saveReply(Reply);
        return ResponseEntity.ok().body("add reply ok");
    }

    @PutMapping("/update-reply/{replyId}")
    public ResponseEntity<?> updateReply(@AuthenticationPrincipal int id, @PathVariable("replyId") int replyId, @RequestBody ModifiedReplyDTO dto) {
        Board Reply = boardService.getBoardId(replyId);
        if(Reply.getMember().getId() == id) {
            Reply.setContent(dto.getContent());
            boardService.saveReply(Reply);
        }
        return ResponseEntity.ok().body("update reply ok");
    }
    @PostMapping("/upload-image")
    public ResponseEntity<?> updateImage(@AuthenticationPrincipal int id ,@RequestParam MultipartFile file) {
        try {
            log.info("ENTER /board/upload-image - Accessor : " + memberService.findByid(id).getEmail());
        } catch (NullPointerException ne) {
            log.info("USER EXCEPTION /board/upload-image - Accessor : "+memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body("USER EXCEPTION /board/upload-image - Accessor : "+memberService.findByid(id).getEmail());
        }

        try{
            String image = fileService.saveImage(file);
            log.info("LEAVE /board/upload-image - Accessor : " + memberService.findByid(id).getEmail());
            return ResponseEntity.ok().body(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/save-onlyimage") //이미지경로 db에 저장
    public ResponseEntity<?> saveOnlyImage(@AuthenticationPrincipal int id ,@RequestParam String imagePath, @RequestParam String title) {
        log.info("ENTER USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
        try {
            Image img = imageService.ImageSave(imagePath, memberService.findByid(id), title);
            if (img != null) {
                log.info("LEAVE USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
                return ResponseEntity.ok().body("upload ok");
            } else {
                log.error("EXCEPTION USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
                throw new RuntimeException();
            }
        } catch (IOException ie) {
            log.error("EXCEPTION USER UPLOAD ONLY IMAGE - Writer : "+memberService.findByid(id).getEmail());
            log.error("EXCEPTION : "+ie.getStackTrace());
            throw new RuntimeException();
        }
    }

    @GetMapping("list-image") // 이미지 리스트 조회
    public ResponseEntity<?> getImageList() {
        List<Image> images = imageService.getImageAll();
        List<ImageListDateDTO> res = new ArrayList<>();
        for (Image image : images) {
            ImageListDateDTO dto = ImageListDateDTO.builder()
                    .id(image.getId())
                    .imagePath(image.getImagePath())
                    .writeDate(image.getCreateDate().toString())
                    .title(image.getTitle())
                    .writer(image.getMember().getName())
                    .build();
            res.add(dto);
        }
        Collections.reverse(res);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/list-image/{imageId}") //이미지 상세조회
    public ResponseEntity<?> getInspectImageData(@PathVariable("imageId") int imageId) {
        Image image = imageService.getImageId(imageId);
        ImageInspectResponseDTO dto = ImageInspectResponseDTO.builder()
                .createDate(image.getCreateDate().toString())
                .title(image.getTitle())
                .writer(image.getMember().getName())
                .imagePath(image.getImagePath())
                .build();
        imageService.saveImage(image);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/delete-image/{imageId}") //이미지 삭제
    public ResponseEntity<?> deleteImage(@AuthenticationPrincipal int id, @PathVariable("imageId") int imageId ) {
        Image image = imageService.getImageId(imageId);
        if(image.getMember().getId() == id) {
            imageService.deleteImage(imageId);
        }
        return ResponseEntity.ok().body("delete ok");
    }


}
