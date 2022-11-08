package com.example.webkit640.service;

import com.example.webkit640.controller.MainPageController;
import com.example.webkit640.entity.MainPageEntity;
import com.example.webkit640.repository.MainPageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MainPageService {

    private final MainPageRepository mainPageRepository;

    @Autowired
    public MainPageService(MainPageRepository mainPageRepository) {
        this.mainPageRepository = mainPageRepository;
    }

    public MainPageEntity saveData(MainPageEntity mainPageEntity) {
        return mainPageRepository.save(mainPageEntity);
    }

    public MainPageEntity getById() {
        try {
            return mainPageRepository.findById(1);
        } catch (NullPointerException ne) {
            return null;
        }

    }
}
