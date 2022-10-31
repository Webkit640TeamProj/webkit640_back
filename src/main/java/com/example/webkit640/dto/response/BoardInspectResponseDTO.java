package com.example.webkit640.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardInspectResponseDTO {
    private String title;
    private String content;
    private String writer;
    private String createDate;
}
