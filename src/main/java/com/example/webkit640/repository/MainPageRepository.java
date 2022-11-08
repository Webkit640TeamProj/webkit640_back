package com.example.webkit640.repository;

import com.example.webkit640.entity.MainPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MainPageRepository extends JpaRepository<MainPageEntity,Integer> {
    @Query("select t from t_mainpage t where t.id = ?1")
    MainPageEntity findById(int id);
}
