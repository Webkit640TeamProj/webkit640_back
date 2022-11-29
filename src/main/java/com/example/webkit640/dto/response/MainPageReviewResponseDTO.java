package com.example.webkit640.dto.response;

import com.example.webkit640.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainPageReviewResponseDTO {
    private int id;
    private String title;
    private String writer;
    private String content;

    public MainPageReviewResponseDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.writer = board.getMember().getName();
        this.content = board.getContent();
    }
}
