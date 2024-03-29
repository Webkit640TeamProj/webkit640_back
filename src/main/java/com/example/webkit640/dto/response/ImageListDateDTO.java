package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageListDateDTO {
    private int id;
    private String title;
    private String writer;
    private String writeDate;
    private int cnt;
    private String imagePath;
}
