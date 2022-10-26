package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "t_empolyment")
public class Employment extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    private boolean employStatus;

    @NotNull
    private String company;

    @OneToOne(mappedBy = "employment", fetch = FetchType.LAZY)
    @JoinColumn(name = "traineeId")
    @NotNull
    private Trainee trainee;
}
