package com.example.webkit640.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OauthResponseDTO<T> {
    private boolean isUser;
    private String error;
    private List<T> data;
}
