package com.example.webkit640.service;

import com.example.webkit640.entity.Calendar;
import com.example.webkit640.repository.CalendarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }
    public Calendar saveEvent(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    public List<Calendar> getCalendar() {
        return calendarRepository.findAll();
    }

    public void deleteCalendarEvent(String title, String start, String end) {
        calendarRepository.deleteByTitleAndStartAndEnd(title,start,end);
    }
}
