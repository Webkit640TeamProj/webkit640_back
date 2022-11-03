package com.example.webkit640.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyRequestDTO {
    private int boardId;
    private String type;
    private String content;
}
