package com.example.webkit640.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponseDTO {
    private String name;
    private String email;
    private String memberType;
    private String memberBelong;
    private boolean isAdmin;
}
