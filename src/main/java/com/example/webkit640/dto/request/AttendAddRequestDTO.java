package com.example.webkit640.dto.request;

import com.example.webkit640.entity.Attend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendAddRequestDTO {
    private String name;
    private String major;
    private String school;
    private String schoolNum;

    private String attendType;
    private String exceptionReason;
    private String attendYear;
    private String attendMonth;
    private String attendDay;
    private String attendTime;
    private String overAllDate;

    public static Attend dtoToEntity(AttendAddRequestDTO dto) {
        return Attend.builder()
                .attendType(dto.getAttendType())
                .exceptionReason(dto.getExceptionReason())
                .attendYear(dto.getAttendYear())
                .attendMonth(dto.getAttendMonth())
                .attendDay(dto.getAttendDay())
                .attendTime(dto.getAttendTime())
                .overAllDate(dto.getOverAllDate())
                .build();
    }
}
