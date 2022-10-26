package com.example.webkit640.repository;

import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalMemberRepository extends JpaRepository<Member,String>  {
    boolean existsMemberByEmail(String email);
    @Query("select t from t_member t where t.email = ?1")
    Member findByEmail(String email);
    Member findByEmailAndPassword(String email, String password);
    Member findById(int id);
}
