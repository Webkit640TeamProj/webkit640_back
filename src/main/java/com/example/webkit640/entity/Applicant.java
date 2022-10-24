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
@Data
@Entity
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String application;

    private boolean isSelect;

    private boolean isApply;

    @NotNull
    private String major;

    @NotNull
    private String school;

    @NotNull
    private String schoolNum;

    @NotNull
    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

//    @OneToOne(mappedBy = "applicant")
//    private Trainee trainee;
//
//    @OneToMany(mappedBy = "applicant")
//    private List<File> files = new ArrayList<>();
}
