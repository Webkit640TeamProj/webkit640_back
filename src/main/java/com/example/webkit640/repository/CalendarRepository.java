package com.example.webkit640.repository;

import com.example.webkit640.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    @Transactional
    @Modifying
    @Query("delete from t_calendar t where t.title = ?1 and t.start = ?2 and t.end = ?3")
    void deleteByTitleAndStartAndEnd(String title, String start, String end);
}
