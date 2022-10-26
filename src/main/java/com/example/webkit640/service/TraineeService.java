package com.example.webkit640.service;

import com.example.webkit640.entity.Trainee;
import com.example.webkit640.repository.TraineeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraineeService {

    private final TraineeRepository traineeRepository;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public Trainee saveTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }
}
