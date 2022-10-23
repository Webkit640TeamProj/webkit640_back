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
@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String boardType;

    private boolean isAdd;

    @NotNull
    @ManyToOne
    //@JoinColumn(name = "writerId")
    @JoinColumn
    private Member member;

    @ManyToOne
    //@JoinColumn(name = "replyId")
    @JoinColumn
    private Board board;

    @OneToMany(mappedBy = "board")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<File> files = new ArrayList<>();
}
