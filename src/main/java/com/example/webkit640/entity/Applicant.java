package com.example.webkit640.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    private String application;

    private boolean isSelect;

    private boolean isApply;

    private String major;

    private String school;

    private String schoolNum;

    private int memberId;
}
