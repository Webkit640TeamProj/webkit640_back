package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardListDataResponseDTO {
    private int id;
    private String title;
    private String writer;
    private String writeDate;
}
