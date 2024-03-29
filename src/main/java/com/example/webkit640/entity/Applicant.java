package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "t_applicant")
@DynamicInsert
public class Applicant extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String application;

    @ColumnDefault("false")
    private boolean isSelect;

    @ColumnDefault("false")
    private boolean isApply;

    @ColumnDefault("false")
    private boolean isAdminApply;

    @NotNull
    private String major;

    @NotNull
    private String school;

    @NotNull
    private String schoolNum;
    private String schoolYear;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(mappedBy = "applicant", fetch = FetchType.LAZY)
    private Trainee trainee;

    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FileEntity> files = new ArrayList<>();
}
