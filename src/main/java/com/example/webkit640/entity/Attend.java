package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "t_attend")
@Data
public class Attend extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String attendType;

    private String exceptionReason;
    private String attendYear;
    private String attendMonth;
    private String attendDay;
    private String attendTime;
    private String overAllDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traineeId")
    private Trainee trainee;

}
