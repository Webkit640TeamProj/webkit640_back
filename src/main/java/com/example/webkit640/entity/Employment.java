package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    private boolean employStatus;

    @NotNull
    private String company;

    @OneToOne
    //@JoinColumn(name = "traineeId")
    @JoinColumn
    @NotNull
    private Trainee trainee;
}
