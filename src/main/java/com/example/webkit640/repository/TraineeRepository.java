package com.example.webkit640.repository;

import com.example.webkit640.entity.Applicant;
import com.example.webkit640.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    Applicant findByApplicant_Id(int applicantId);

    Trainee findByCardinal(String cardinal);

}
