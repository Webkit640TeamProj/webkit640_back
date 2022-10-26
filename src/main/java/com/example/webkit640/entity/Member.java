package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "t_member")
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

    private boolean isAdmin;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Applicant applicant;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Counsel> counsels = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<FileEntity> file;

}
