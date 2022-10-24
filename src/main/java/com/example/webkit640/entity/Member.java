package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Member extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String memberType;

    private String memberBelong;

    @OneToOne(mappedBy = "member", fetch = FetchType.EAGER)
    private Applicant applicant;
//
//    @OneToMany(mappedBy = "member")
//    private List<Counsel> counsels = new ArrayList<>();
//
//    @OneToMany(mappedBy = "member")
//    private List<Board> boards = new ArrayList<>();

}
