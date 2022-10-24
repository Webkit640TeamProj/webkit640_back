package com.example.webkit640.repository;

import com.example.webkit640.entity.Board;
import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Member findByMember_Id(int writerId);
}
