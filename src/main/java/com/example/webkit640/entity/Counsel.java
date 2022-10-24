package com.example.webkit640.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Counsel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String counselType;

    @NotNull
    private String content;

    @NotNull
    private String progressStatus;

//    @NotNull
//    private int traineeId;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "traineeId")
    @JoinColumn
    private Trainee trainee;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "counselorId")
    @JoinColumn
    private Member member;
}
