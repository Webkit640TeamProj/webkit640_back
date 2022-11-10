package com.example.webkit640.service;

import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.FileEntity;
import com.example.webkit640.entity.Member;
import com.example.webkit640.repository.BoardRepository;
import com.example.webkit640.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BoardService {
    @Value("${file.dir}")
    private String fileDir;

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BoardRepository boardRepository;
    public List<FileEntity> getEmptyMemberFileEntity(int memberId) {
        return fileRepository.findByMember_IdAndBoardIsNull(memberId);
    }

    public List<Board> getBoardAll(String type) {
        return boardRepository.findByBoardType(type);
    }

    public Board getBoardId(int boardId) {
        return boardRepository.findById(boardId);
    }

    public FileEntity boardImageSave(MultipartFile file, Member member) throws IOException {
        String where = fileDir+"board";
        File file_ = new File(where);
        if (!file_.exists()) {
            log.info("SERVER : NOT EXIST DIRECTORY, make dir");
            file_.mkdir();
            File memberDir = new File(where+"/"+member.getEmail());
            memberDir.mkdir();
        }
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        if (!(extension.equals(".png") || extension.equals(".jpg") || extension.equals(".jpeg") || extension.equals(".gif"))) {
            return null;
        }
        String savedName = member.getEmail() + "_board_"+ LocalDate.now() +extension;
        String savedPath = where + "/" + member.getEmail() + "/" + savedName;

        file.transferTo(new File(savedPath));
        log.info("SERVER : Save FileEntity");

        return fileRepository.save(FileEntity.builder()
                .member(member)
                .fileExtension(extension)
                .fileName(savedName)
                .filePath(where + "/" + member.getEmail())
                .fileType("BOARD")
                .build());
    }
    public Board changeShowBoard(int id) {
        Board board = null;
        try {
            board = boardRepository.findById(id);
            board.setAdd(!board.isAdd());
            return boardRepository.save(board);
        } catch (NullPointerException ne) {
            return null;
        }
    }
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board saveReply(Board board) { return boardRepository.save(board); }

    public void deleteBoard(int id) {
        if (boardRepository.existsById(id)) {
            boardRepository.deleteById(id);
        }
        else {
            throw new RuntimeException("id does not exist");
        }
        return;
    }

    public List<Board> getByWriter(int writerId) {
        return boardRepository.findByMember_Id(writerId);
    }
}
