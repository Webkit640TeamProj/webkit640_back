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
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String cardinal;

    @NotNull
    @OneToOne
    @JoinColumn(name = "applicantId")
    private Applicant applicant;

    @OneToMany(mappedBy = "trainee")
    private List<Attend> attends = new ArrayList<>();

    @OneToOne(mappedBy = "trainee")
    private Employment employment;

    @OneToMany(mappedBy = "trainee")
    private List<Counsel> counsels = new ArrayList<>();
}
