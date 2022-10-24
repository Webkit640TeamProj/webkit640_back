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
@Entity
@Data
public class Attend extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String attendType;

    private String exceptionReason;

    @NotNull
    private String attendYear;

    @NotNull
    private String attendMonth;

    @NotNull
    private String attendTime;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "traineeId")
    @JoinColumn
    private Trainee trainee;

}
