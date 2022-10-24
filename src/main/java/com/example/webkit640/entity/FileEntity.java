package com.example.webkit640.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class FileEntity extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NotNull
    private String fileName;

    @NotNull
    private String fileExtension;

    @NotNull
    private String filePath;

    @NotNull
    private String fileType;

    @ManyToOne
    //@JoinColumn(name = "applicantId")
    @JoinColumn
    private Applicant applicant;

    @ManyToOne
    //@JoinColumn(name = "boardId")
    @JoinColumn
    private Board board;
}
