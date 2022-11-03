package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyListDataResponseDTO {
    private String writer;
    private String content;
    private String createDate;
    private String updateDate;

}
