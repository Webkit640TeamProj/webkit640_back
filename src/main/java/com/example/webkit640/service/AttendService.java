package com.example.webkit640.service;

import com.example.webkit640.entity.Attend;
import com.example.webkit640.repository.AttendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendService {
    private final AttendRepository attendRepository;

    @Autowired
    public AttendService(AttendRepository attendRepository) {
        this.attendRepository = attendRepository;
    }

    public void saveAttend(Attend attend) {
        attendRepository.save(attend);
    }
}
