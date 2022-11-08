package com.example.webkit640.repository;

import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    List<Board> findByMember_Id(int writerId);
    Board findById(int id);
    List<Board> findByBoardType(String type);
}
