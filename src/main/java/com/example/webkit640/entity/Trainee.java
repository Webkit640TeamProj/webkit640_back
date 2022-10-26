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
@Entity(name = "t_trainee")
public class Trainee extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String cardinal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Applicant applicant;

    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
    private List<Attend> attends = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Employment employment;

    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
    private List<Counsel> counsels = new ArrayList<>();
}
