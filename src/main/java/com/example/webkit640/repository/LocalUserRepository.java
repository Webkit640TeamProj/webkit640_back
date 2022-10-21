package com.example.webkit640.repository;

import com.example.webkit640.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalUserRepository extends JpaRepository<Member,String>  {
    public boolean existsMemberByEmail(String email);
}
