package com.example.webkit640.repository;

import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalMemberRepository extends JpaRepository<Member,String>  {
    boolean existsMemberByEmail(String email);
    Member findByEmail(String email);
    Member findByEmailAndPassword(String email, String password);
}
