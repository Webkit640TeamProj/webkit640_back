package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveTraineeResponseDTO {
    private String name;
    private String cardinal;
}
